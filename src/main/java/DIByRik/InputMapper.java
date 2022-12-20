package DIByRik;

import DIByRik.annotations.InputMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class InputMapper {
	private final DependencyContainer dependencyContainer;
	private final Map<String, Method> routes;
	private static final Logger log = Logger.getLogger(DependencyContainer.class.getName());

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
				log.setParent(Logger.getLogger(InputMapper.class.getName()));
				log.info("No route found for input: " + input);
			} else {
				try {
					log.setParent(Logger.getLogger(routeHandler.getDeclaringClass().getName()));
					log.info(routeHandler.invoke(dependencyContainer.getInstanceOfClass(routeHandler.getDeclaringClass()), args).toString());
				} catch (NullPointerException e) {
					log.warning(String.format("method %s on route %s with args %s returned null", routeHandler.getName(), route, Arrays.toString(args)));
				} catch (Exception e) {
					//TODO: maak beter
					log.warning(e.getClass().getSimpleName() + " calling: " + routeHandler.getName() + ", message: " + e.getMessage());
				}
			}
		}
	}
}
