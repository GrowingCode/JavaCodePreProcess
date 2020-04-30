package translation.tensor.util;

import java.util.ArrayList;
import java.util.TreeMap;

public class TokenIndexUtil {
	
	public static ArrayList<Integer> GenerateTokenIndex(ArrayList<String> local_token_str) {
		ArrayList<Integer> stmt_token_variable_info = new ArrayList<Integer>();
		TreeMap<String, Integer> token_index_record = new TreeMap<String, Integer>();
		TokenIndex ti = new TokenIndex();
//		Set<Integer> l_ts = new TreeSet<Integer>();
		for (String l_t_str : local_token_str) {
			int l_tid = -1;
			if (l_t_str != null) {
				l_tid = AssignID(token_index_record, l_t_str, ti);
			}
//			Assert.isTrue(l_tid <= stmt_token_variable_info.size()+1,
//					"last_stmt:" + last_stmt.stmt + "#last_stmt.local_token_str.size():"
//							+ last_stmt.local_token_str.size() + "#stmt_token_variable_info.size():"
//							+ stmt_token_variable_info.size() + "#origin_file:" + "CommonName");// origin_file
			stmt_token_variable_info.add(l_tid);
			/*int first_encounter = 0;
			if (l_tid >= 0 && !l_ts.contains(l_tid)) {
				l_ts.add(l_tid);
				first_encounter = 1;
			}
			stmt_token_first_encounter_info.add(first_encounter);*/
		}
		return stmt_token_variable_info;
	}
	
	public static Integer AssignID(TreeMap<String, Integer> token_index_record, String key, TokenIndex ti) {
		Integer index_record = token_index_record.get(key);
		if (index_record == null) {
			index_record = ti.NewIndex();
			token_index_record.put(key, index_record);
		}
		return index_record;
	}
	
}
