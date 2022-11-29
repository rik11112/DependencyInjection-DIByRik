package DIByRik;

import DIByRik.annotations.Intercepted;
import DIByRik.annotations.Logged;
import DIByRik.interceptionhandlers.InterceptionHandler;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class InterceptionResolver {
	private static final ProxyFactory proxyFactory = new ProxyFactory();

	public static Object interceptMethods(Object instance) {
		//TODO refactor with interception handlers
		var interceptedMethods = Arrays.stream(instance.getClass().getMethods())
				.filter(m -> Arrays.stream(m.getAnnotations())
						.anyMatch(a -> a.annotationType().equals(Logged.class))).toList();

		if (interceptedMethods.isEmpty()) {
			return instance;
		}

		proxyFactory.setSuperclass(instance.getClass());
		proxyFactory.setFilter(interceptedMethods::contains);

		Object finalInstance = instance;
		MethodHandler methodHandler = (self, method, proceed, args) -> {
			System.out.println("Intercepted method");

			try {
				return method.invoke(finalInstance, args);
			} finally {
				System.out.println("Finished method");
			}
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
