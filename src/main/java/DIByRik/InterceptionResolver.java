package DIByRik;

import DIByRik.annotations.interception.Intercepted;
import DIByRik.annotations.interception.Logged;
import DIByRik.interceptionhandlers.InterceptionHandler;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class InterceptionResolver {
	private final ProxyFactory proxyFactory = new ProxyFactory();
	private final Collection<InterceptionHandler> interceptionHandlers;
	private final List<? extends Class<? extends Annotation>> interceptedAnnotations;

	public InterceptionResolver() {
		var handlerReflections = new Reflections(InterceptionHandler.class.getPackageName());
		interceptionHandlers = handlerReflections.getSubTypesOf(InterceptionHandler.class).stream()
				.map(c -> {
					var constructors = c.getConstructors();
					if (constructors.length != 1)
						throw new RuntimeException("InterceptionHandler implementations must have exactly one constructor");
					try {
						return (InterceptionHandler) constructors[0].newInstance();
					} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				})
				.toList();

		var annotationReflections = new Reflections(Intercepted.class.getPackageName());
		@SuppressWarnings("unchecked")  // This is safe because we filter for isAnnotation before casting
		var _interceptedAnnotations = annotationReflections.getTypesAnnotatedWith(Intercepted.class).stream()
				.filter(Class::isAnnotation)
				.map(c -> (Class<? extends Annotation>) c)
				.toList();
		interceptedAnnotations = _interceptedAnnotations;	// Can't suppress warning on field initialisation
	}

	public Object interceptMethods(Object instance) {
		var interceptedMethods = Arrays.stream(instance.getClass().getMethods())
				.filter(m -> Arrays.stream(m.getAnnotations())
						.anyMatch(a -> interceptedAnnotations.contains(a.annotationType()))).toList();

		if (interceptedMethods.isEmpty()) {
			return instance;
		}

		proxyFactory.setSuperclass(instance.getClass());
		proxyFactory.setFilter(interceptedMethods::contains);

		Object finalInstance = instance;
		// general method handler for all intercepted methods, which delegates to the correct interception handler
		MethodHandler methodHandler = (self, method, proceed, args) -> {
			// find interception annotation on method (max one allowed)
			var interceptionAnnotation = Arrays.stream(method.getAnnotations())
					.filter(a -> interceptedAnnotations.contains(a.annotationType()))
					.findFirst().orElseThrow();

			// find interception handler for annotation
			InterceptionHandler interceptionHandler = interceptionHandlers.stream()
					.filter(h -> h.appliesTo(interceptionAnnotation.annotationType()))
					.findFirst()
					.orElseThrow();

			// delegate to interception handler
			return interceptionHandler.getMethodHandler(finalInstance).invoke(self, method, proceed, args);
		};

		try {
			instance = proxyFactory.create(new Class<?>[0], new Object[0], methodHandler);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
				 InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		return instance;
	}



//	public Object createProxyWithInterceptions(Object o) {
//		return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), (proxy, method, args) -> resolveInterception(o, method, args));
//	}
}
