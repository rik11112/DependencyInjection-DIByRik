package proxyTesting;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;

public class ProxyTesting {
	public static void main(String[] _args) {
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setSuperclass(MyClass.class);
		proxyFactory.setFilter(m -> m.getName().equals("getName"));

		Object myClass = new MyClass("myClass1");

		MethodHandler methodHandler = (self, method, proceed, args) -> {
			System.out.println("Intercepted method");

			try {
				return method.invoke(myClass, args);
			} finally {
				System.out.println("Finished method");
			}
		};

		MyClass myClass2;
		try {
			myClass2 = (MyClass) proxyFactory.create(new Class<?>[0], new Object[0], methodHandler);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
				 InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		System.out.println("=== myClass2 ===");
		System.out.println("output:" + myClass2.getName());
		System.out.println("=== myClass ===");
		System.out.println("output:" + myClass2.getName2());
	}
}

