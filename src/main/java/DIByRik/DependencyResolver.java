package DIByRik;

import DIByRik.annotations.*;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyResolver {
    private final Set<Class<?>> components;
    private final Set<Method> beans;
    private final Set<Class<?>> eagerInitClasses;

    private final Map<String, Method> routes = new HashMap<>();

    public DependencyResolver(Class<?> mainClass) {
        // Getting component variants
        var annotationsPackageReflections = new Reflections("DIByRik.annotations");
        @SuppressWarnings("unchecked")  // This is safe because we filter for isAnnotation before casting
        var componentVariants = annotationsPackageReflections
                .getTypesAnnotatedWith(Component.class).stream()
                .filter(Class::isAnnotation)
                .map(c -> (Class<? extends Annotation>) c)
                .collect(Collectors.toSet());

        // Getting all annotated classes in the given package
        var reflections = new Reflections(mainClass.getPackageName());
        components = reflections.getTypesAnnotatedWith(Component.class, true);
        componentVariants.stream().map(reflections::getTypesAnnotatedWith).forEach(components::addAll);
        beans = reflections.getTypesAnnotatedWith(Configuration.class).stream()
                .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                .filter(m -> m.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet());
        eagerInitClasses = reflections.getTypesAnnotatedWith(EagerInit.class);

        // Getting all routes
        reflections.getTypesAnnotatedWith(Controller.class).stream()
                .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                .filter(m -> m.isAnnotationPresent(InputMapping.class))
                .forEach(m -> {
                    var route = m.getAnnotation(InputMapping.class).route();
                    if (route.contains(" ")) {
                        throw new RuntimeException();
                    }
                    routes.put(route, m);
                });
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

    public Map<String, Method> getRoutes() {
        return routes;
    }
}
