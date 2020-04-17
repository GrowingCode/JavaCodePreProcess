package util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class PrintUtil {
	
	public static <K, V> void PrintMap(Map<K, V> map, String extra_info, int max_print_num) {
		System.out.println("===== map " + extra_info + " print begin; max_print_num:" + max_print_num + " =====");
		Set<K> key = map.keySet();
		int count = 0;
		for (Iterator<K> it = key.iterator(); it.hasNext();) {
			K s = it.next();
			System.out.println(s+":"+map.get(s));
			count++;
			if (count >= max_print_num) {
				break;
			}
		}
		System.out.println("===== map " + extra_info + " print end; max_print_num:" + max_print_num + " =====");
	}
	
	public static <K> void PrintList(List<K> list, String extra_info) {
		System.out.println("===== list " + extra_info + " print begin =====");
		for (K k : list) {
			System.out.println(k);
		}
		System.out.println("===== list " + extra_info + " print end =====");
	}
	
	public static <K> void PrintSet(Set<K> set, String extra_info) {
		System.out.println("===== set " + extra_info + " print begin =====");
		for (K k : set) {
			System.out.println(k);
		}
		System.out.println("===== set " + extra_info + " print end =====");
	}
	
	
	public static <K, V> void PrintPartOfEntryList(List<Entry<K, V>> entries, int start, int end, String extra_info, String key_desc, String value_desc) {
		System.out.println("===== entry list " + extra_info + " print begin =====");
		for (int i = start; i < end; i++) {
			Entry<K, V> e = entries.get(i);
			System.out.println("#" + key_desc + ":" + e.getKey() + "#" + value_desc + ":" + e.getValue());
		}
		System.out.println("===== entry list " + extra_info + " print end =====");
	}

	
}
