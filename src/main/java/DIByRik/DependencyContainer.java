package DIByRik;

import DIByRik.annotations.ConstructorInjection;
import DIByRik.annotations.Component;
import DIByRik.annotations.EagerInit;
import DIByRik.exceptions.AmbigousMatchException;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.reflections.Reflections;

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
    private Reflections reflections;
    private Set<Class<?>> components;
    private Set<Class<?>> eagerInitClasses;
    private final SimpleDirectedGraph<Class<?>, DefaultEdge> dependencyGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
    private final Map<Class<?>, Object> instances = new HashMap<>();
    private static final Logger log = Logger.getLogger(DependencyContainer.class.getName());

    public void init(Class<?> mainClass) {
        if (reflections != null) {
            throw new IllegalStateException("DependencyContainer is already initialised");
        }
        log.info("DependencyContainer: Initialising with package name: " + mainClass.getPackageName());
        reflections = new Reflections(mainClass.getPackageName());
        components = reflections.getTypesAnnotatedWith(Component.class);
        eagerInitClasses = reflections.getTypesAnnotatedWith(EagerInit.class);
        fillDependencyGraph();
        checkForCircularDependencies();
        createEagerInitInstances();
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
        if (reflections == null) {
            throw new IllegalStateException("DependencyContainer has not been initialized yet.");
        }
    }

    private void createEagerInitInstances() {
        eagerInitClasses.forEach(this::getInstance);
    }

    private void fillDependencyGraph() {
        for (Class<?> component : components) {
            dependencyGraph.addVertex(component);
        }
        //Will sometimes call addVertex again with the same values, but JGraphT ignores the duplicates
        for (Class<?> component : components) {
            for (Class<?> dependency : getConstructor(component).getParameterTypes()) {
                dependencyGraph.addEdge(dependency, component);
            }
        }
    }

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

    private Object createInstance(Class<?> clazz) {
        log.log(Level.FINE, String.format("Creating instance of %s", clazz.getSimpleName()));
        Object instance = null;
        try {
            //Initializing with constructor injection
            Constructor<?> constructor = getConstructor(clazz);
            var constructorParameters = Arrays.stream(constructor.getParameterTypes())
                    .map(this::getInstance)
                    .toArray();

            instance = constructor.newInstance(constructorParameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not create instance of " + clazz.getSimpleName() + "." +
                    "\n^Did you try to access field or setter injected fields in the constructor?" +
                    "\n^They are injected after the constructor finishes so inside the constructor they are null", e);
        } catch (NullPointerException e) {
            throw new NullPointerException("Field and setter injections are injected after the constructor has finished," +
                    " so if you want to use them inside the constructor, constructor inject them " + clazz.getSimpleName());
        }

        //By putting the instance here already, we can handle some circular dependencies automatically
        instances.put(clazz, instance);
        if (clazz != instance.getClass()) {
            log.log(Level.FINE, String.format("Instance of %s is actually an instance of %s, putting extra reference in instances",
                    clazz.getSimpleName(), instance.getClass().getSimpleName()));
            instances.put(instance.getClass(), instance);
        }

        return instance;
    }
}
