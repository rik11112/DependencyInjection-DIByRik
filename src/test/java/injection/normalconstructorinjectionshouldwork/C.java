package injection.normalconstructorinjectionshouldwork;

import DIByRik.annotations.Component;

@Component
public class C {
    public String myContent() {
        return "I am C";
    }
}
