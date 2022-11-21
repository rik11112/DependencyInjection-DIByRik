package DIByRik.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author Rik Puts
 *
 * This annotation is a form of component, but it tells the DIByRik framework that this class is a configuration class.
 * This means that it contains @Beans, which are used to create instances of classes.
 */
@Component
@Target(ElementType.TYPE)
public @interface Configuration {
}
