package DIByRik.annotations;

import DIByRik.interceptionhandlers.InterceptionHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
public @interface Intercepted {
    Class<? extends InterceptionHandler> handler();
}
