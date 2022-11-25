package DIByRik;

import DIByRik.annotations.Bean;
import DIByRik.annotations.Component;
import DIByRik.annotations.Configuration;
import DIByRik.annotations.EagerInit;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyResolver {
    private final Set<Class<?>> components;
    private final Set<Method> beans;
    private final Set<Class<?>> eagerInitClasses;
    private final Set<Method> interceptedMethods;
    private final SimpleDirectedGraph<Class<?>, DefaultEdge> dependencyGraph = new SimpleDirectedGraph<>(DefaultEdge.class);

    public DependencyResolver(Class<?> mainClass) {
        var annotationsPackageReflections = new Reflections("DIByRik.annotations");
        @SuppressWarnings("unchecked")  // This is safe because we filter for isAnnotation before casting
        var componentVariants = annotationsPackageReflections
                .getTypesAnnotatedWith(Component.class).stream()
                .filter(Class::isAnnotation)
                .map(c -> (Class<? extends Annotation>) c)
                .collect(Collectors.toSet());
        var reflections = new Reflections(mainClass.getPackageName());
        components = reflections.getTypesAnnotatedWith(Component.class, true);
        componentVariants.stream().map(reflections::getTypesAnnotatedWith).forEach(components::addAll);
        beans = reflections.getTypesAnnotatedWith(Configuration.class).stream()
                .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                .filter(m -> m.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet());
        eagerInitClasses = reflections.getTypesAnnotatedWith(EagerInit.class);
        interceptedMethods = components.stream()
                .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                .filter(m -> m.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet());
    }

    public Set<Class<?>> getComponents() {
        return components;
    }

    public Set<Method> getBeans() {
        return beans;
    }

    public Set<Class<?>> getEagerInitClasses() {
        return eagerInitClasses;
    }
}
