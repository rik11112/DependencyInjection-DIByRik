package injection;

import DIByRik.DIByRikApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class InjectionTests {
    @Test
    void normalConstructorInjectionShouldWork() {
        try {
            var container = DIByRikApplication.run(injection.normalconstructorinjectionshouldwork.A.class);
            var a = container.getInstanceOfClass(injection.normalconstructorinjectionshouldwork.A.class);
            assertEquals("I am C", a.cContent());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void constructorInjectionWithInterfacesShouldWork() {
        try {
            var container = DIByRikApplication.run(injection.constructorInjectionwithinterfacesshouldwork.A.class);
            var a = container.getInstanceOfClass(injection.constructorInjectionwithinterfacesshouldwork.A.class);
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

    /**
     * This test tests the following scenario:
     * A depends on an interfaces which is implemented by a component,
     * which depends on another component and a bean.
     * The bean then depends on a component and another bean.
     * Some components are annotated with specializations of the component annotation instead of the component annotation itself.
     * Every class in question contributes to the return string of the first class. (Of which we assert the output)
     * Basically if this test works, all other tests should work as well.
     */
    @Test
    void componentsWithBeansAndViceVersaShouldWork() {
        try {
            var container = DIByRikApplication.run(injection.componentswithbeansandviceversa.Config.class);
            var testClass = container.getInstanceOfClass(injection.componentswithbeansandviceversa.RandomEagerInitClass.class);
            assertEquals("I'm a random method!weee 123-weee", testClass.getTheString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
