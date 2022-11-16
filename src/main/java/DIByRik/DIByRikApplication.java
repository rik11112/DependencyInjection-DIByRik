package DIByRik;

import demo.RandomEagerInitClass;

public class DIByRikApplication {
    public static DependencyContainer run(Class<?> mainClass, String[] args) {
        DependencyContainer dependencyContainer = new DependencyContainer();
        dependencyContainer.init(mainClass);
        return dependencyContainer;
    }
}
