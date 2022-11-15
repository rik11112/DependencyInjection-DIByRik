package demo;

public class RandomEagerInitClass {
    private final RandomClass randomClass;

    public RandomEagerInitClass(RandomClass randomClass) {
        this.randomClass = randomClass;
    }

    public void doSomething() {
        randomClass.randomMethod();
    }
}
