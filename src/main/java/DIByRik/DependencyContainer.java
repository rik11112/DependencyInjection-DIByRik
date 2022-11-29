package DIByRik;

import DIByRik.annotations.*;
import DIByRik.exceptions.AmbigousMatchException;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Rik
 * <p>
 * This class is used to register and resolve dependencies.
 * It will look for dependencies in the package provided with the constructor.
 * When creating instances, it will prioritise the @ConstructorInjection annotation when choosing its constructor,
 * but if there still is ambiguity it will choose the first constructor (the highest in the document).
 */
public class DependencyContainer {
    private boolean isInitialised = false;
    private Set<Class<?>> components;
    private Set<Method> beans;
    private Set<Class<?>> eagerInitClasses;
    private final SimpleDirectedGraph<Class<?>, DefaultEdge> dependencyGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
    private final Map<Class<?>, Object> instances = new HashMap<>();
    private static final Logger log = Logger.getLogger(DependencyContainer.class.getName());

    public DependencyContainer(Class<?> mainClass) {
        if (isInitialised) {
            throw new IllegalStateException("DependencyContainer is already initialised");
        }
        log.info("DependencyContainer: Initialising with package name: " + mainClass.getPackageName());
        var resolver = new DependencyResolver(mainClass);
        components = resolver.getComponents();
        beans = resolver.getBeans();
        eagerInitClasses = resolver.getEagerInitClasses();
        fillDependencyGraph();
        checkForCircularDependencies();
        instantiateEagerInitInstances();
        isInitialised = true;
    }

    public <T> T getInstanceOfClass(Class<T> clazz) {
        checkInitialised();
        return clazz.cast(getInstance(clazz));
    }

    private Object getInstance(Class<?> clazz) {
        if (instances.containsKey(clazz)) {
            return instances.get(clazz);
        }
        return createInstance(clazz);
    }

    private void checkForCircularDependencies() {
        var cycleDetector = new CycleDetector<>(dependencyGraph);
        if (cycleDetector.detectCycles()) {
            var cycleClasses = cycleDetector.findCycles().stream()
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", "));
            throw new RuntimeException(String.format("""
                    Circular dependency detected between the following classes: %s""", cycleClasses));
        }
    }

    private void checkInitialised() {
        if (!isInitialised) {
            throw new IllegalStateException("DependencyContainer has not been initialized yet.");
        }
    }

    private void instantiateEagerInitInstances() {
        eagerInitClasses.forEach(this::getInstance);
    }

    private void fillDependencyGraph() {
        for (Class<?> component : components) {
            dependencyGraph.addVertex(component);
        }
        beans.stream().map(Method::getReturnType).forEach(dependencyGraph::addVertex);
        //Will sometimes call addVertex again with the same values, but JGraphT ignores the duplicates
        for (Class<?> component : components) {
            for (Class<?> dependency : getConstructor(component).getParameterTypes()) {
                try {
                    dependencyGraph.addEdge(dependency, component);
                } catch (IllegalArgumentException e) {

                    if (dependency == component) {
                        // If a class depends on itself, it will throw an IllegalArgumentException
                        throw new IllegalArgumentException(String.format("Class %s depends on itself. This is not allowed.", component.getSimpleName()));
                    } else {
                        // Otherwise it will throw an IllegalArgumentException because the dependency is not a component
                        throw new IllegalArgumentException(String.format("""
                                        Dependency %s is not a component, but is required by %s.
                                        Please add the @Component annotation to %s, or create a bean.""",
                                dependency.getSimpleName(), component.getSimpleName(), dependency.getSimpleName()));
                    }
                }
            }
        }
    }

    /**
     * Finds a constructor for a class, prioritising the @ConstructorInjection annotation.
     * Also looks in subclasses or implementations in case of interfaces.
     *
     * @param componentClass The class to find a constructor for
     * @return The created instance
     */
    private Constructor<?> getConstructor(Class<?> componentClass) {
        var constructors = componentClass.getConstructors();

        //If the class has itself has constructors use those (prioritizing @ConstructorInjection)
        if (constructors.length > 0) {
            return Arrays.stream(constructors)
                    .filter(c -> Arrays.stream(c.getAnnotations())
                            .anyMatch(a -> a.annotationType().equals(ConstructorInjection.class)))
                    .findFirst().orElse(constructors[0]);
        }

        List<Class<?>> subclassesOrImplementations;

        //TODO check for abstract class to, instead of just interfaces
        if (componentClass.isInterface()) {
            // If it's an interface check if we can find any of its implementations
            subclassesOrImplementations = components.stream()
                    .filter(c -> Arrays.asList(c.getInterfaces()).contains(componentClass)).toList();
        } else {
            // If it's a class check if we can find any of its subclasses
            subclassesOrImplementations = components.stream()
                    .filter(c -> {
                        while (c.getSuperclass() != null) {
                            if (c.getSuperclass().equals(componentClass)) {
                                return true;
                            }
                            c = c.getSuperclass();
                        }
                        return false;
                    }).toList();
        }

        Supplier<String> type = () -> componentClass.isInterface() ? "implementations" : "subclasses";  //Only execute when needed
        // Make sure there is only one implementation
        if (subclassesOrImplementations.size() > 1) {
            throw new AmbigousMatchException(String.format("%s has multiple %s marked as usable: %s",
                    type.get(), componentClass.getSimpleName(), subclassesOrImplementations));
        }
        if (subclassesOrImplementations.size() == 0) {
            throw new AmbigousMatchException("No public constructor found for " + componentClass.getName() + " or its " + type.get());
        }

        // Get the constructor of the implementation
        return getConstructor(subclassesOrImplementations.get(0));
    }

    private Object getBean(Class<?> clazz) {
        var bean = beans.stream()
                .filter(m -> m.getReturnType().equals(clazz))
                .findFirst()
                .orElse(null);

        // If it isn't a bean don't look further
        if (bean == null) {
            return null;
        }

        var methodParameters = Arrays.stream(bean.getParameterTypes())
                .map(this::getInstance)
                .toArray();

        try {
            return bean.invoke(getInstance(bean.getDeclaringClass()), methodParameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not create instance of " + clazz.getSimpleName() + " with bean " + bean.getName(), e);
        }
    }

    /**
     * Creates an instance of the given class.
     * First checks if there's a bean of the class, if so it will use that.
     * If not, it will try to find a constructor to create an instance. (see getConstructor)
     *
     * @param clazz The class to create an instance of
     * @return The created instance
     */
    private Object createInstance(Class<?> clazz) {
        log.log(Level.FINE, String.format("Creating instance of %s", clazz.getSimpleName()));
        Object instance = getBean(clazz);   //Check if there's a bean for the class

        if (instance == null) {
            // If there's no bean, create an instance the normal way
            try {
                //Initializing with constructor injection
                Constructor<?> constructor = getConstructor(clazz);
                var constructorParameters = Arrays.stream(constructor.getParameterTypes())
                        .map(this::getInstance)
                        .toArray();

                instance = constructor.newInstance(constructorParameters);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Could not create instance of " + clazz.getSimpleName() + ".", e);
            }
        }

        // Intercepting methods if needed
        //TODO refactor with interception handlers
        var interceptedMethods = Arrays.stream(instance.getClass().getMethods())
                .filter(m -> Arrays.stream(m.getAnnotations())
                        .anyMatch(a -> a.annotationType().equals(Logged.class))).toList();
        if (interceptedMethods.size() > 0) {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setSuperclass(instance.getClass());
            proxyFactory.setFilter(interceptedMethods::contains);
            Object finalInstance = instance;
            MethodHandler methodHandler = (self, thisMethod, proceed, args) -> {
//                log.log(Level.INFO, String.format("Intercepted method %s of %s", thisMethod.getName(), clazz.getSimpleName()));
                System.out.printf("Intercepted method %s of %s%n", thisMethod.getName(), clazz.getSimpleName());
                try {
                    return proceed.invoke(finalInstance, args);
                } finally {
//                    log.log(Level.INFO, String.format("Finished method %s of %s", thisMethod.getName(), clazz.getSimpleName()));
                    System.out.printf("Finished method %s of %s%n", thisMethod.getName(), clazz.getSimpleName());
                }
            };
            try {
                instance = proxyFactory.create(new Class<?>[0], new Object[0], methodHandler);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        instances.put(clazz, instance);
        if (clazz != instance.getClass()) {
            log.log(Level.FINE, String.format("Instance of %s is actually an instance of %s, putting extra reference in instances",
                    clazz.getSimpleName(), instance.getClass().getSimpleName()));
            instances.put(instance.getClass(), instance);
        }

        return instance;
    }
}
