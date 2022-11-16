package injection;

import DIByRik.DIByRikApplication;
import injection.normalConstructorInjectionShouldWork.A;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class InjectionTests {
    @Test
    void normalConstructorInjectionShouldWork() {
        try {
            var container = DIByRikApplication.run(injection.normalConstructorInjectionShouldWork.A.class);
            var a = container.getInstanceOfClass(injection.normalConstructorInjectionShouldWork.A.class);
            assertEquals(a.cContent(), "I am C");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void constructorInjectionWithInterfacesShouldWork() {
        try {
            var container = DIByRikApplication.run(injection.constructorInjectionWithInterfacesShouldWork.A.class);
            var a = container.getInstanceOfClass(injection.constructorInjectionWithInterfacesShouldWork.A.class);
            assertEquals(a.cContent(), "I am C with interfaces");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
