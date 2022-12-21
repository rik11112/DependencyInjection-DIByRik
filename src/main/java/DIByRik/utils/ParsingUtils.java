package DIByRik.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ParsingUtils {
	public static Map<Class<?>, Function<String, ?>> paramParsers() {
		return Map.ofEntries(
				Map.entry(String.class, (Function<String, String>) s -> s),
				Map.entry(int.class, Integer::parseInt),
				Map.entry(double.class, Double::parseDouble),
				Map.entry(boolean.class, Boolean::parseBoolean),
				Map.entry(char.class, s -> s.charAt(0)),
				Map.entry(byte.class, Byte::parseByte),
				Map.entry(short.class, Short::parseShort),
				Map.entry(long.class, Long::parseLong),
				Map.entry(float.class, Float::parseFloat),
				Map.entry(Integer.class, Integer::parseInt),
				Map.entry(Double.class, Double::parseDouble),
				Map.entry(Boolean.class, Boolean::parseBoolean),
				Map.entry(Character.class, s -> s.charAt(0)),
				Map.entry(Byte.class, Byte::parseByte),
				Map.entry(Short.class, Short::parseShort),
				Map.entry(Long.class, Long::parseLong),
				Map.entry(Float.class, Float::parseFloat));
	}
}
