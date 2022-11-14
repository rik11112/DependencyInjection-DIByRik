package DIByRik;

import demo.RandomEagerInitClass;

public class DIByRikApplication {
    private static final DependencyContainer dependencyContainer = new DependencyContainer();

    public static void main(String[] args) {
        dependencyContainer.init();

        //TEMP
        var random = dependencyContainer.getInstanceOfClass(RandomEagerInitClass.class);
        random.doSomething();
    }
}
