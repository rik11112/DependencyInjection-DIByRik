package injection.constructorInjectionwithinterfacesshouldwork;

import DIByRik.annotations.Component;

@Component
public class Bimpl implements B {
    private final C c;

    public Bimpl(C c) {
        this.c = c;
    }

    @Override
    public String cContent() {
        return c.myContent();
    }
}
