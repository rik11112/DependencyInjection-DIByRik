package injection.componentswithbeansandviceversa;

import DIByRik.annotations.Component;
import DIByRik.annotations.ConstructorInjection;

@Component
public class RandomClassImpl implements RandomClass {
    private Weee weee;
    private BeanyClass klasseDieGeBeantWord;

    public RandomClassImpl() {
    }

    @ConstructorInjection
    public RandomClassImpl(Weee weee, BeanyClass beanyClass) {
        this.weee = weee;
        this.klasseDieGeBeantWord = beanyClass;
    }

    @Override
    public String randomMethod() {
        return "I'm a random method!" + weee.weee() + " " + klasseDieGeBeantWord.getTest();
    }
}
