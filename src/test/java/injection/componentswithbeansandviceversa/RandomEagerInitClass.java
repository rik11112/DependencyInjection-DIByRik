package injection.componentswithbeansandviceversa;

import DIByRik.annotations.Component;
import DIByRik.annotations.EagerInit;

@EagerInit
public class RandomEagerInitClass {
    private final RandomClass randomClass;

    public RandomEagerInitClass(RandomClass randomClass) {
        this.randomClass = randomClass;
        getTheString();
    }

    public String getTheString() {
        return randomClass.randomMethod();
    }
}
