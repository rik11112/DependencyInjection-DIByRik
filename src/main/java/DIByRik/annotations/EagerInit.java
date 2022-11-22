package DIByRik.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * A specialization of the {@link Component} annotation that indicates that the annotated class is to be initialised right after the DependencyContainer.
 * When using this annotation, the class will be instantiated by the DIByRik framework upon startup.
 *
 * @author Rik Puts
 * @see Component
 */
@Component
@Target(ElementType.TYPE)
public @interface EagerInit {
}
