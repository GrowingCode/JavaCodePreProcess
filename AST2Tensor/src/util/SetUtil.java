package util;

import java.util.Set;
import java.util.TreeSet;

public class SetUtil {
	
	public static <T> Set<T> TheElementsInSetOneExistInSetTwo(Set<T> one, Set<T> two) {
		Set<T> result = new TreeSet<T>();
		for (T t : one) {
			if (two.contains(t)) {
				result.add(t);
			}
		}
		return result;
	}
	
}
