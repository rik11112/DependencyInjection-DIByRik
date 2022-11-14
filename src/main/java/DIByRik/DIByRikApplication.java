package DIByRik;

import DIByRik.annotations.Component;
import DIByRik.annotations.EagerInit;
import java.util.Set;

public class DIByRikApplication {


    public static void main(String[] args) {
        DependencyContainer dependencyContainer = new DependencyContainer();
        dependencyContainer.init();
    }

}
