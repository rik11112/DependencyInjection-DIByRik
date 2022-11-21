package DIByRik.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author Rik Puts
 * When using this annotation, the class will be instantiated by the DIByRik framework upon startup.
 */
@Component
@Target(ElementType.TYPE)
public @interface EagerInit {
}
