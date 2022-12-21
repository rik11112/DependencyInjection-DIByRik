package DIByRik.interceptionhandlers;

import DIByRik.DependencyContainer;
import DIByRik.annotations.interception.Timed;
import javassist.util.proxy.MethodHandler;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimedInterceptionHandler implements InterceptionHandler {
	private static final Logger log = LogManager.getLogger();

	@Override
	public boolean appliesTo(Class<? extends Annotation> annotation) {
		return annotation.equals(Timed.class);
	}

	@Override
	public MethodHandler getMethodHandler(Object instance) {
		return (self, method, proceed, args) -> {
			LocalDateTime start = LocalDateTime.now();

			try {
				return method.invoke(instance, args);
			} finally {
				long diffInMillies = ChronoUnit.MILLIS.between(start, LocalDateTime.now());
				log.info(String.format("Invoking: %s took %d milliseconds", method.getName(), diffInMillies));
			}
		};
	}
}
