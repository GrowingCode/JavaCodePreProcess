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
		double v_acc = 0;
		for (Iterator<K> it = key.iterator(); it.hasNext();) {
			K s = it.next();
			V v = map.get(s);
			System.out.println(s+":"+v);
			count++;
			if (count >= max_print_num) {
				break;
			}
			try {
				v_acc += Double.parseDouble(v+"");
			} catch (Exception e) {
			}
		}
		System.out.println("===== map " + extra_info + " print end; print_num:" + count + " v_acc:" + v_acc + " =====");
	}

	public static <K, V> void PrintMap(Map<K, V> map, Set<K> ks, String extra_info, int max_print_num) {
		System.out.println("===== map " + extra_info + " print begin; max_print_num:" + max_print_num + " =====");
		int count = 0;
		double v_acc = 0;
		Iterator<K> it = ks.iterator();
		for (; it.hasNext();) {
			K s = it.next();
			V v = map.get(s);
			System.out.println(s+":"+v);
			count++;
			if (count >= max_print_num) {
				break;
			}
			try {
				v_acc += Double.parseDouble(v+"");
			} catch (Exception e) {
			}
		}
		System.out.println("===== map " + extra_info + " print end; print_num:" + count + " v_acc:" + v_acc + " =====");
	}
	
	public static <K> void PrintList(List<K> list, String extra_info) {
		System.out.println("===== list " + extra_info + " print begin =====");
		for (K k : list) {
			System.out.println(k);
		}
		System.out.println("===== list " + extra_info + " print end =====");
	}
	
	public static <S, K> void PrintThreeLists(List<S> list0, List<K> list1, List<K> list2, String extra_info, int one_ele_length) {
		System.out.println("===== two lists " + extra_info + " print begin =====");
		for (S k : list0) {
			System.out.printf("%"+one_ele_length+"s", YStringUtil.TrimString(k+"", one_ele_length));
		}
		System.out.println();
		for (K k : list1) {
			System.out.printf("%"+one_ele_length+"s", YStringUtil.TrimString(k+"", one_ele_length));
		}
		System.out.println();
		for (K k : list2) {
			System.out.printf("%"+one_ele_length+"s", YStringUtil.TrimString(k+"", one_ele_length));
		}
		System.out.println();
		System.out.println("===== two lists " + extra_info + " print end =====");
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
