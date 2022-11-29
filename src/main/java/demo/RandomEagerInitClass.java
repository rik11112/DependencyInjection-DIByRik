package demo;

import DIByRik.annotations.Component;
import DIByRik.annotations.EagerInit;
import DIByRik.annotations.Logged;

@EagerInit
public class RandomEagerInitClass {
    private final RandomClass randomClass;

    public RandomEagerInitClass(RandomClass randomClass) {
        this.randomClass = randomClass;
        doSomething();
    }

    public void doSomething() {
        randomClass.randomMethod("foo");
    }
}
