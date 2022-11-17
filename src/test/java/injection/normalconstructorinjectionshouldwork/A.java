package injection.normalconstructorinjectionshouldwork;

import DIByRik.annotations.Component;

@Component
public class A {
    private final B b;

    public A(B b) {
        this.b = b;
    }

    public String cContent() {
        return b.cContent();
    }
}
