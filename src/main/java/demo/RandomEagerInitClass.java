package demo;

import DIByRik.annotations.Controller;
import DIByRik.annotations.InputMapping;

@Controller
public class RandomEagerInitClass {
    private final RandomClass randomClass;

    public RandomEagerInitClass(RandomClass randomClass) {
        this.randomClass = randomClass;
        doSomething();
    }

    @InputMapping(input = "doSomething")
    public void doSomething() {
        randomClass.randomMethod("foo");
    }
}
