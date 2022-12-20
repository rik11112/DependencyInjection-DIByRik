package DIByRik;

import java.lang.reflect.Method;
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

			// Find the method that handles the input and invoke it
			var route = routes.get(input);
			if (route == null) {
				log.info("No route found for input: " + input);
			} else {
				try {
					route.invoke(dependencyContainer.getInstanceOfClass(route.getDeclaringClass()));
				} catch (Exception e) {
					//TODO: maak beter
					log.warning("Failed to invoke route: " + route.getName() + " message: " + e.getMessage());
				}
			}
		}
	}
}
