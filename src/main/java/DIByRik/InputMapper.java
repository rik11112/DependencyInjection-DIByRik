package DIByRik;

import java.lang.reflect.Method;
import java.util.Map;

public class InputMapper {
	private final DependencyContainer dependencyContainer;
	private final Map<String, Method> routes;

	public InputMapper(DependencyContainer dependencyContainer, DependencyResolver resolver) {
		this.dependencyContainer = dependencyContainer;
		this.routes = resolver.getRoutes();
	}


	public void start() {
		//TODO: implement
	}
}
