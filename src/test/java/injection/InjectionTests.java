package injection;

import DIByRik.DIByRikApplication;
import DIByRik.exceptions.AmbigousMatchException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class InjectionTests {
    @Test
    void normalConstructorInjectionShouldWork() {
        try {
            var container = DIByRikApplication.run(injection.normalConstructorInjectionShouldWork.A.class);
            var a = container.getInstanceOfClass(injection.normalConstructorInjectionShouldWork.A.class);
            assertEquals("I am C", a.cContent());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void constructorInjectionWithInterfacesShouldWork() {
        try {
            var container = DIByRikApplication.run(injection.constructorInjectionWithInterfacesShouldWork.A.class);
            var a = container.getInstanceOfClass(injection.constructorInjectionWithInterfacesShouldWork.A.class);
            assertEquals("I am C with interfaces", a.cContent());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void circularDependencyShouldThrowException() {
        try {
            DIByRikApplication.run(injection.circulardependency.A.class);
            fail("Exception not thrown");
        } catch (Exception e) {
            // Order of classes is not guaranteed so we this is the most specific check we can do
            assertEquals("Circular dependency detected between the following classes", e.getMessage().split(":")[0]);
        }
    }
}
