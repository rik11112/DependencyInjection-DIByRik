package injection.circulardependency;

import DIByRik.annotations.Component;

@Component
public record C(A a) {

    public String myContent() {
        return "I am C";
    }
}
