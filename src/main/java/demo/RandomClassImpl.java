package demo;

import DIByRik.annotations.ConstructorInjection;
import DIByRik.annotations.Component;

@Component
public class RandomClassImpl implements RandomClass {
    private Weee weee;

    public RandomClassImpl() {
    }

    @ConstructorInjection
    public RandomClassImpl(Weee weee) {
        this.weee = weee;
    }

    @Override
    public void randomMethod() {
        System.out.println("I'm a random method!" + weee.weee());
    }
}
