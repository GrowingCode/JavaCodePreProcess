package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import unit.PairContainer;

public class MapUtil {

	public static <K, V> TreeMap<V, K> ReverseKeyValueInMap(Map<K, V> map) {
		TreeMap<V, K> result = new TreeMap<V, K>();
		Set<Entry<K, V>> eset = map.entrySet();
		Iterator<Entry<K, V>> eitr = eset.iterator();
		while (eitr.hasNext()) {
			Entry<K, V> entry = eitr.next();
			result.put(entry.getValue(), entry.getKey());
		}
		return result;
	}

	public static Map<String, Object> CastKeyToString(Map<Object, Object> map) {
		Map<String, Object> result = new TreeMap<String, Object>();
		Set<Object> kset = map.keySet();
		Iterator<Object> eitr = kset.iterator();
		while (eitr.hasNext()) {
			Object entry = eitr.next();
			result.put(entry.toString(), map.get(entry));
		}
		return result;
	}

	public static Map<Integer, Object> ListToIndexMap(List<?> obj_list) {
		Map<Integer, Object> result = new TreeMap<Integer, Object>();
		@SuppressWarnings("unchecked")
		Iterator<Object> ol_itr = (Iterator<Object>) obj_list.iterator();
		int index = -1;
		while (ol_itr.hasNext()) {
			index++;
			Object obj = ol_itr.next();
			result.put(index, obj);
		}
		return result;
	}

	public static <K, V> List<Entry<K, V>> SortMapByValue(Map<K, V> oriMap) {
		List<Map.Entry<K, V>> entryList = new ArrayList<Map.Entry<K, V>>(oriMap.entrySet());
		Collections.sort(entryList, new MapValueComparator<K, V>());
		return entryList;
	}
	
	public static <K, V> List<K> EntryListToKeyList(List<Entry<K, V>> entries) {
		List<K> result = new ArrayList<K>();
		for (Entry<K, V> e : entries) {
			result.add(e.getKey());
		}
		return result;
	}

	public static void main(String[] args) {
		Map<String, Integer> map = new TreeMap<String, Integer>();
		map.put("A", 100);
		map.put("B", 10);
		map.put("C", 1000);
		map.put("D", 1);
		List<Entry<String, Integer>> el = SortMapByValue(map);
		Iterator<Entry<String, Integer>> el_itr = el.iterator();
		while (el_itr.hasNext()) {
			Entry<String, Integer> el_ele = el_itr.next();
			System.out.println("key:" + el_ele.getKey() + "#" + "value:" + el_ele.getValue());
		}
		System.out.println(map.remove("A"));
	}

	public static <K, T> PairContainer<K, T> FindKeyValuePairWithMaxValue(Map<K, T> pairs) {
		K max_k = null;
		T max_t = null;
		Comparable<T> max = null;
		Set<K> ks = pairs.keySet();
		Iterator<K> k_itr = ks.iterator();
		while (k_itr.hasNext()) {
			K k = k_itr.next();
			T t = pairs.get(k);
			@SuppressWarnings("unchecked")
			Comparable<T> cmp = (Comparable<T>) t;
			if (max == null) {
				max_k = k;
				max_t = t;
				max = cmp;
			} else {
				if (max.compareTo(t) < 0) {
					max_k = k;
					max_t = t;
					max = cmp;
				}
			}
		}
		return new PairContainer<K, T>(max_k, max_t);
	}
	
	public static <K> void CountOneKey(Map<K, Integer> m, K key, int count) {
		Integer r_count = m.get(key);
		if (r_count == null) {
			r_count = 0;
		}
		r_count++;
		m.put(key, r_count);
	}

}

class MapValueComparator <K,V> implements Comparator<Map.Entry<K, V>> {
	@SuppressWarnings("unchecked")
	@Override
	public int compare(Entry<K, V> me1, Entry<K, V> me2) {
		return ((Comparable<V>)me1.getValue()).compareTo(me2.getValue());
	}
}
