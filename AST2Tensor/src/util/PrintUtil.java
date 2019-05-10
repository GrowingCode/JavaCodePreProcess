package util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PrintUtil {
	
	public static <K, V> void PrintMap(Map<K, V> map) {
		System.out.println("===== map print begin =====");
		Set<K> key = map.keySet();
		for (Iterator<K> it = key.iterator(); it.hasNext();) {
			K s = it.next();
			System.out.println(s+":"+map.get(s));
		}
		System.out.println("===== map print end =====");
	}
	
}
