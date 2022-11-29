package DIByRik.annotations;

import DIByRik.interceptionhandlers.LoggedInterceptionHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When this annotation is used on a method of a Component, the entry and exit of the method will be logged.
 *
 * @author Rik Puts
 */
@Intercepted(handler = LoggedInterceptionHandler.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logged {
}
