package DIByRik.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * A specialization of the {@link Component} annotation that indicates that the annotated class is a "Service".
 * What that means can be decided by the user.
 *
 * @author Rik Puts
 * @see Component
 */
@Component
@Target(ElementType.TYPE)
public @interface Service {
}
