package DIByRik.interceptionhandlers;

import DIByRik.DependencyContainer;
import DIByRik.annotations.interception.Logged;
import javassist.util.proxy.MethodHandler;

import java.lang.annotation.Annotation;
import java.util.logging.Logger;

public class LoggedInterceptionHandler implements InterceptionHandler {
	private static final Logger log = Logger.getLogger(DependencyContainer.class.getName());

	@Override
	public boolean appliesTo(Class<? extends Annotation> annotation) {
		return annotation.equals(Logged.class);
	}

	@Override
	public MethodHandler getMethodHandler(Object instance) {
		return (self, method, proceed, args) -> {
			log.info("Invoking: " + method.getName());

			try {
				return method.invoke(instance, args);
			} finally {
				log.info("Finished invoking: " + method.getName());
			}
		};
	}
}
