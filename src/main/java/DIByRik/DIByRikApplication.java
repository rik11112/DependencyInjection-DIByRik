package DIByRik;
public class DIByRikApplication {
    public static DependencyContainer run(Class<?> mainClass) {
        return run(mainClass, true);
    }

    public static DependencyContainer run(Class<?> mainClass, boolean doInputMapping) {
        var resolver = new DependencyResolver(mainClass);
        var dependencyContainer = new DependencyContainer(mainClass, resolver);
        if (doInputMapping) {
            var inputMapper = new InputMapper(dependencyContainer);
            inputMapper.start();
        }
        return dependencyContainer;
    }
}
