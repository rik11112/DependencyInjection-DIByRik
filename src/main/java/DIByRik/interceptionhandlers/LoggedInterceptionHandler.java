package DIByRik.interceptionhandlers;

import DIByRik.DependencyContainer;
import DIByRik.annotations.interception.Logged;
import javassist.util.proxy.MethodHandler;

import java.lang.annotation.Annotation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggedInterceptionHandler implements InterceptionHandler {
	private static final Logger log = LogManager.getLogger();

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
