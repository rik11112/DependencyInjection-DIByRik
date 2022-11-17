package injection.normalconstructorinjectionshouldwork;

import DIByRik.annotations.Component;
import DIByRik.annotations.Service;

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
