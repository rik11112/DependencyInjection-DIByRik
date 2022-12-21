package DIByRik;

import DIByRik.utils.ParsingUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputMapper {
	private final DependencyContainer dependencyContainer;
	private final Map<String, Method> routes;
	private static final Logger log = LogManager.getLogger();

	private final Map<Class<?>, Function<String, ?>> paramParsers = ParsingUtils.paramParsers();

	public InputMapper(DependencyContainer dependencyContainer, DependencyResolver resolver) {
		this.dependencyContainer = dependencyContainer;
		this.routes = resolver.getRoutes();
	}

	public void start() {
		Scanner s = new Scanner(System.in);
		log.info("Now listening on: standard input");
		while (true) {
			// Wait for input
			String input = s.nextLine();

			// Converting to its components
			var components = input.split(" +");
			var route = components[0];
			Object[] args;
			if (components.length > 1) {
				args = Arrays.copyOfRange(components, 1, components.length);
			} else {
				args = new Object[0];
			}

			// Check if the stop command is given
			if (route.equals("stop")) {
				log.info("Stopping...");
				break;
			}

			// Find the method that handles the input and invoke it
			var routeHandler = routes.get(route);
			if (routeHandler == null) {
				log.info("No route found for input: " + input);
			} else {
				try {
					log.info("Response: " + routeHandler.invoke(dependencyContainer.getInstanceOfClass(routeHandler.getDeclaringClass()), convertArgs(routeHandler, args)).toString());
				} catch (ArrayIndexOutOfBoundsException e) {
					if (routeHandler.getParameterCount() != args.length) {
						log.warn(String.format("method %s on route %s expected %d arguments but got %d", routeHandler.getName(), route, routeHandler.getParameterCount(), args.length));
					} else {
						log.error(String.format("method %s on route %s with args %s threw an ArrayIndexOutOfBoundsException", routeHandler.getName(), route, Arrays.toString(args)));
					}
				} catch (Exception e) {
					log.error(String.format("method %s on route %s with args %s threw an exception: %s, stacktrace: %s",
							routeHandler.getName(), route, Arrays.toString(args), e.getClass().getSimpleName(), Arrays.toString(e.getStackTrace())));
				}
			}
		}
	}

	Object[] convertArgs(Method method, Object[] args) {
		var convertedArgs = new Object[args.length];
		var parameters = method.getParameterTypes();
		for (int i = 0; i < args.length; i++) {
			var parser = paramParsers.get(parameters[i]);
			if (parser == null) {
				throw new IllegalArgumentException(String.format("No parser found for type: %s on method: %s", method.getParameterTypes()[i], method.getName()));
			}
			convertedArgs[i] = paramParsers.get(method.getParameterTypes()[i]).apply((String) args[i]);
		}
		return convertedArgs;
	}
}
