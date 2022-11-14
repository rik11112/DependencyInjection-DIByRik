package DIByRik;

import DIByRik.annotations.Autowired;
import DIByRik.annotations.Component;
import DIByRik.annotations.EagerInit;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DependencyContainer {
    private final Reflections reflections = new Reflections("demo");   //TODO package prefix configurable (or check if that's nessesary)
    private final Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);
    private final Set<Class<?>> eagerInitClasses = reflections.getTypesAnnotatedWith(EagerInit.class);
    private final SimpleDirectedGraph<Class<?>, DefaultEdge> dependencyGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
    private final Map<Class<?>, Object> instances = new HashMap<>();

    public void init() {
        fillDependencyGraph();
        checkForCircularDependencies();
    }

    public <T> T getInstanceOfClass(Class<T> clazz) {
        if (!instances.containsKey(clazz)) {
            instances.put(clazz, createInstance(clazz));
        }
        return clazz.cast(instances.get(clazz));
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
                    Circular dependency detected between the following classes: %s
                    This means that two classes depend on each other strongly.
                    You can fix this by marking one of the injections with @Lazy.
                    If they are constructor injections, consider changing one to a setter injection.""", cycleClasses));
        }
    }

    private void fillDependencyGraph() {
        for (Class<?> component : components) {
            dependencyGraph.addVertex(component);
        }
        //Will sometimes call addVertex again with the same values, but JGraphT ignores the duplicates
        for (Class<?> component : components) {
            // Constructor parameters (If present with @Autowired)
            for (Class<?> dependency : getConstructor(component).getParameterTypes()) {
                dependencyGraph.addEdge(dependency, component);
            }

            // Fields with @Autowired
            for (Field field : getFieldsWithAnnotation(component, Autowired.class)) {
                dependencyGraph.addEdge(field.getType(), component);
            }

            // Setter injection (setters with @Autowired)
            for (Method method : getMethodsWithAnnotation(component, Autowired.class)) {
                dependencyGraph.addEdge(method.getParameterTypes()[0], component);
            }
        }
    }

    private List<Method> getMethodsWithAnnotation(Class<?> component, Class<? extends Annotation> annotation) {
        return Arrays.stream(component.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    /**
     * Gets all fields with the given annotation.
     * This includes private fields, and fields from superclasses (and their private fields).
     */
    private List<Field> getFieldsWithAnnotation(Class<?> component, Class<? extends Annotation> annotation) {
        var thisClassFields = new ArrayList<>(Arrays.stream(component.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotation))
                .toList());
        var superClass = component.getSuperclass();
        if (superClass != Object.class) {
            var superClassFields = getFieldsWithAnnotation(superClass, annotation);
            thisClassFields.addAll(superClassFields);
        }
        return thisClassFields;
    }

    private Constructor<?> getConstructor(Class<?> component) {
        Constructor<?>[] constructors = component.getConstructors();
        if (constructors.length == 0) {
            throw new RuntimeException("No public constructor found for " + component.getName() + " or any of its superclasses");
        }
        return Arrays.stream(constructors)
                .filter(c -> Arrays.stream(c.getAnnotations())
                        .anyMatch(a -> a.annotationType().equals(Autowired.class)))
                .findFirst().orElse(constructors[0]);
    }

    private Object createInstance(Class<?> clazz) {
        log.trace("Creating instance of {}", clazz.getSimpleName());
        Object instance = null;
        try {
            //Initializing with constructor injection
            Constructor<?> constructor = getConstructor(clazz);
            var constructorParameters = Arrays.stream(constructor.getParameterTypes())
                    .map(this::getInstance)
                    .toArray();

            instance = constructor.newInstance(constructorParameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //By putting the instance here already, we can handle some circular dependencies automatically
        instances.put(clazz, instance);

        //Setter injection
        Object finalInstance = instance;    //suboptimal scope, but now only needs to be initialized once
        for (Method method : getMethodsWithAnnotation(clazz, Autowired.class)) {
            try {
                // array[0] because setters should only have one parameter
                method.invoke(finalInstance, getInstance(method.getParameterTypes()[0]));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        //Field injection
        for (Field field : getFieldsWithAnnotation(clazz, Autowired.class)) {
            try {
                field.setAccessible(true);
                field.set(instance, getInstance(field.getType()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }
}
