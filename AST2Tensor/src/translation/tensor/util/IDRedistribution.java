package translation.tensor.util;

import java.util.TreeMap;

public class IDRedistribution {
	
	public static Integer AssignID(TreeMap<String, Integer> token_index_record, String key, TokenIndex ti) {
		Integer index_record = token_index_record.get(key);
		if (index_record == null) {
			index_record = ti.NewIndex();
			token_index_record.put(key, index_record);
		}
		return index_record;
	}
	
}
