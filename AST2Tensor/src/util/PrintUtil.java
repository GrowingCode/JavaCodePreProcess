package util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrintUtil {
	
	public static <K, V> void PrintMap(Map<K, V> map, String extra_info) {
		System.out.println("===== map " + extra_info + " print begin =====");
		Set<K> key = map.keySet();
		for (Iterator<K> it = key.iterator(); it.hasNext();) {
			K s = it.next();
			System.out.println(s+":"+map.get(s));
		}
		System.out.println("===== map " + extra_info + " print end =====");
	}
	
	public static <K> void PrintList(List<K> list, String extra_info) {
		System.out.println("===== list " + extra_info + " print begin =====");
		for (K k : list) {
			System.out.println(k);
		}
		System.out.println("===== list " + extra_info + " print end =====");
	}
	
}
