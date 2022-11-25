package DIByRik.interceptionhandlers;

import java.lang.reflect.Method;

public class LoggedInterceptionHandler implements InterceptionHandler {
	@Override
	public Method getMethodHandler() {
		return null;
	}
//    public <T> T intercept(Method method, Object o, Object[] args) {
//        System.out.println("Entering " + method.getName());
//        T result = method.invoke(o, args);
//        System.out.println("Exiting " + method.getName());
//        return input;
//    }
}
