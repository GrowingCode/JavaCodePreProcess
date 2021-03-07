package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
	
	public static Object ReflectField(String fieldName, Object object) {
		try {
			Field field = object.getClass().getField(fieldName);
			return field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object ReflectMethod(String methodName, Object object, Class<?>[] clz, Object[] obz) {
		try {
			Method method = object.getClass().getMethod(methodName, clz);
			return method.invoke(object, obz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
