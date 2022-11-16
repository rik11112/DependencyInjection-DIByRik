package demo;

import DIByRik.DIByRikApplication;
import DIByRik.DependencyContainer;

public class Main {
    public static void main(String[] args) {
        DependencyContainer diContainer = DIByRikApplication.run(Main.class);
    }
}
