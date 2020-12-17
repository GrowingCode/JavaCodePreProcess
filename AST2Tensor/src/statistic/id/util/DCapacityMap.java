package statistic.id.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

public class DCapacityMap<K, V> {
	
	int capacity = -1;
	
	TreeMap<K, V> kv = new TreeMap<K, V>();
	TreeMap<V, HashSet<K>> vk = new TreeMap<V, HashSet<K>>();
	
	public DCapacityMap(int cap) {
		this.capacity = cap;
	}
	
	public void PutKeyValue(K k, V v) {
		// remove old
		V ht_v = kv.get(k);
		HashSet<K> set_k = null;
		if (ht_v != null) {
			set_k = vk.get(ht_v);
			Assert.isTrue(set_k != null && set_k.contains(k));
			set_k.remove(k);
		}
		// add new
		kv.put(k, v);
		HashSet<K> v_set_k = vk.get(v);
		if (v_set_k == null) {
			v_set_k = new HashSet<K>();
			vk.put(v, v_set_k);
		}
		v_set_k.add(k);
		
		while (kv.size() > capacity) {
			RemoveOneKeyBasedOnValueNaturalOrder();
		}
	}
	
	public V RemoveKey(K k) {
		V v = kv.remove(k);
		Assert.isTrue(v != null, "Inexisted kv, k:" + k +"#v:" + v);
		HashSet<K> set_k = vk.get(v);
		Assert.isTrue(set_k != null);
		set_k.remove(k);
		if (set_k.isEmpty()) {
			vk.remove(v);
		}
		return v;
	}

	@SuppressWarnings("unchecked")
	public void TrimBasedOnValueInNaturalOrder(V trim_threshold_inclusive) {
		Set<V> vs = vk.keySet();
		for (V v : vs) {
			if (((Comparable<V>)v).compareTo(trim_threshold_inclusive) <= 0) {
				HashSet<K> set_k = vk.remove(v);
				if (set_k != null) {
					for (K k : set_k) {
						V ov = kv.remove(k);
						Assert.isTrue(ov.equals(v));
					}
				}
			} else {
				break;
			}
		}
//		IntStream int_stream = IntStream.rangeClosed(0, trim_threshold_inclusive);
//		Stream<Integer> integer_stream = int_stream.boxed();
//		Integer[] trim_vs = integer_stream.toArray(Integer[]::new);
//		RemoveValueMatchedKeys(trim_vs);
	}
	
	public boolean ContainsKey(K k) {
		return kv.containsKey(k);
	}
	
	public Set<K> GetKeys() {
		return kv.keySet();
	}
	
	public V GetValueBasedOnKey(K k) {
		return kv.get(k);
	}
	
	public Map<K, V> GetOriginMap() {
		return kv;
	}
	
//	public void RemoveValueMatchedKeys(V[] vs) {
//		for (V v : vs) {
//			HashSet<K> set_k = vk.remove(v);
//			if (set_k != null) {
//				for (K k : set_k) {
//					V ov = kv.remove(k);
//					Assert.isTrue(ov.equals(v));
//				}
//			}
//		}
//	}
	
	private void RemoveOneKeyBasedOnValueNaturalOrder() {
		Set<V> vs = vk.keySet();
		Assert.isTrue(vs.size() > 0);
		V nt_v = vs.iterator().next();
		HashSet<K> set_k = vk.get(nt_v);
		Assert.isTrue(set_k != null && set_k.size() > 0);
		K fk = set_k.iterator().next();
		set_k.remove(fk);
		if (set_k.isEmpty()) {
			vk.remove(nt_v);
		}
		kv.remove(fk);
	}

	public void Clear() {
		kv.clear();
		vk.clear();
	}
	
}
