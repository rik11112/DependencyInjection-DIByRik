package injection.normalConstructorInjectionShouldWork;

import DIByRik.annotations.Component;

@Component
public class B {
    private final C c;

    public B(C c) {
        this.c = c;
    }

    public String cContent() {
        return c.myContent();
    }
}
