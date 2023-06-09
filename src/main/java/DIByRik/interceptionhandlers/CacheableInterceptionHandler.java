package DIByRik.interceptionhandlers;

import DIByRik.annotations.interception.Cacheable;
import javassist.util.proxy.MethodHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;


public class CacheableInterceptionHandler implements InterceptionHandler {
	private final Map<Method, Map<String, Object>> cache = new java.util.HashMap<>();
	private static final Logger log = LogManager.getLogger();

	@Override
	public boolean appliesTo(Class<? extends Annotation> annotation) {
		return annotation.equals(Cacheable.class);
	}

	@Override
	public MethodHandler getMethodHandler(Object instance) {
		return (self, method, proceed, args) -> {
			String argsKey = Arrays.toString(args);
			if (cache.containsKey(method)) {
				// method has been called before
				Map<String, Object> methodCache = cache.get(method);
				if (methodCache.containsKey(argsKey)) {
					// cache hit, return previous result
					log.trace(String.format("Cache hit for method: %s with args: %s", method.getName(), argsKey));
					return methodCache.get(argsKey);
				}
			} else {
				// method has not been called before, add it to the cache
				cache.put(method, new java.util.HashMap<>());
			}

			// cache miss, invoke method and store result
			Object result = method.invoke(instance, args);
			log.trace(String.format("Caching method: %s with args: %s", method.getName(), argsKey));

			cache.get(method).put(argsKey, result);

			return result;
		};
	}
}
