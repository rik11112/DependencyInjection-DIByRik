package injection.constructorInjectionwithinterfacesshouldwork;

import DIByRik.annotations.Component;

@Component
public class Cimpl implements C {
    @Override
    public String myContent() {
        return "I am C with interfaces";
    }
}
