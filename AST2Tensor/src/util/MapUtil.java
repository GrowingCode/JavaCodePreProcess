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
	
}
