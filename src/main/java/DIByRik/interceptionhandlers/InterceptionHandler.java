package DIByRik.interceptionhandlers;

import javassist.util.proxy.MethodHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface InterceptionHandler {
    boolean appliesTo(Class<? extends Annotation> annotation);
    MethodHandler getMethodHandler(Object instance);
}
