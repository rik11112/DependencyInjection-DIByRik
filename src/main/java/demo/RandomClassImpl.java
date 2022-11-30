package demo;

import DIByRik.annotations.*;
import DIByRik.annotations.interception.Logged;
import DIByRik.annotations.interception.Timed;

@Repository
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

    @Timed
    @Override
    public void randomMethod(String foo) {
        System.out.println("I'm a random method!" + weee.weee() + " <" + foo + "> " + klasseDieGeBeantWord.getTest());
    }
}
