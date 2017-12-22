package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapUtil {
	
	@SuppressWarnings("rawtypes")
	public static Map<Object, Object> ReverseKeyValueInMap(Map map) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		Set eset = map.entrySet();
		Iterator eitr = eset.iterator();
		while (eitr.hasNext()) {
			Entry entry = (Entry)eitr.next();
			result.put(entry.getValue(), entry.getKey());
		}
		return result;
	}

	public static Map<String, Object> CastKeyToString(Map<Object, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		Set<Object> kset = map.keySet();
		Iterator<Object> eitr = kset.iterator();
		while (eitr.hasNext()) {
			Object entry = eitr.next();
			result.put(entry.toString(), map.get(entry));
		}
		return result;
	}
	
}
