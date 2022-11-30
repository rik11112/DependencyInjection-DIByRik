package DIByRik.annotations.interception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When this annotation is used on a method of a Component, the result of the method will be cached.
 * The method will only be invoked once for each unique set of arguments.
 * So only use this annotation on methods that are pure functions.
 *
 * @author Rik Puts
 */
@Intercepted()
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
}
