package DIByRik.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * A specialization of the {@link Component} annotation that indicates that the annotated class is a "Controller".
 * Methods inside controllers can be annotated with {@link InputMapping} to indicate that they should be called with a certain input.
 *
 * @author Rik Puts
 * @see Component
 * @see InputMapping
 */
@Component
@Target(ElementType.TYPE)
public @interface Controller {
}
