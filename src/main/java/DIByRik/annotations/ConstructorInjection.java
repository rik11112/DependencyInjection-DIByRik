package DIByRik.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Rik Puts
 *
 * This annotation tells the DIByRik framework which constructor to use when creating an instance.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstructorInjection {
}
