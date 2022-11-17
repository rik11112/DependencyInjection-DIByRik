package demo;

import DIByRik.annotations.ConstructorInjection;
import DIByRik.annotations.Component;

@Component
public class RandomClassImpl implements RandomClass {
    private Weee weee;
    private KlasseDieGeBeantWord klasseDieGeBeantWord;

    public RandomClassImpl() {
    }

    @ConstructorInjection
    public RandomClassImpl(Weee weee, KlasseDieGeBeantWord klasseDieGeBeantWord) {
        this.weee = weee;
        this.klasseDieGeBeantWord = klasseDieGeBeantWord;
    }

    @Override
    public void randomMethod() {
        System.out.println("I'm a random method!" + weee.weee() + " " + klasseDieGeBeantWord.getTest());
    }
}
