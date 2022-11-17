package injection.componentswithbeansandviceversa;

public class BeanyClass {
    private final String test;

    public BeanyClass(String test) {
        this.test = test;
    }

    public String getTest() {
        return test;
    }
}
