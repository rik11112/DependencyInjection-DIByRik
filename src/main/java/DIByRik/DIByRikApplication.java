package DIByRik;
public class DIByRikApplication {
    public static DependencyContainer run(Class<?> mainClass) {
        DependencyContainer dependencyContainer = new DependencyContainer();
        dependencyContainer.init(mainClass);
        return dependencyContainer;
    }
}
