package DIByRik.interceptionhandlers;

import DIByRik.annotations.Logged;
import javassist.util.proxy.MethodHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class LoggedInterceptionHandler implements InterceptionHandler {
	@Override
	public boolean appliesTo(Class<? extends Annotation> annotation) {
		return annotation.equals(Logged.class);
	}

	@Override
	public MethodHandler getMethodHandler(Object instance) {
		return (self, method, proceed, args) -> {
			System.out.println("Intercepted method");

			try {
				return method.invoke(instance, args);
			} finally {
				System.out.println("Finished method");
			}
		};
	}
}
