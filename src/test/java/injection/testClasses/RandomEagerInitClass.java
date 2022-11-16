package injection.testClasses;

import DIByRik.annotations.Component;
import DIByRik.annotations.EagerInit;

@EagerInit
@Component
public class RandomEagerInitClass {
    private final RandomClass randomClass;

    public RandomEagerInitClass(RandomClass randomClass) {
        this.randomClass = randomClass;
        doSomething();
    }

    public void doSomething() {
        randomClass.randomMethod();
    }
}
