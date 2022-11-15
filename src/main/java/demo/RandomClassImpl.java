package demo;

import DIByRik.annotations.Component;

@Component
public class RandomClassImpl implements RandomClass {
    @Override
    public void randomMethod() {
        System.out.println("I'm a random method!");
    }
}
