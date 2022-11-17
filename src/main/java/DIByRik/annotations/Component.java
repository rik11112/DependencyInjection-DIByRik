package DIByRik.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author Rik Puts
 * This annotation is used to mark a class as a component.
 * A component is a class that will be instantiated by the DIByRik framework.
 * And thus can be dependency injected.
 */
@Target(ElementType.TYPE)
public @interface Component {
}
