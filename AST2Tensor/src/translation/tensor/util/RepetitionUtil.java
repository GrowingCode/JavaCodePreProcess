package translation.tensor.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

public class RepetitionUtil {
	
	public static ArrayList<Integer> GenerateRepetitionRelative(ArrayList<Integer> stmt_token_variable_info) {
		Map<Integer, Integer> latest_index = new TreeMap<Integer, Integer>();
		int i_len = stmt_token_variable_info.size();
		ArrayList<Integer> seq_var_info = new ArrayList<Integer>();
		ArrayList<Integer> real_var_info = new ArrayList<Integer>();
		for (int i = 0; i < i_len; i++) {
			Assert.isTrue(seq_var_info.size() == i);
			Integer ti = stmt_token_variable_info.get(i);
			if (ti >= 0) {
				Integer li = latest_index.get(ti);
				int curr_var_index = real_var_info.size();
				if (li != null) {
					int relative = curr_var_index - li;
					Assert.isTrue(relative > 0);
					seq_var_info.add(relative);// - i
//					System.out.println("token_i:" + i + "token_en:" + ti + "#relative:" + relative);
				} else {
					seq_var_info.add(-1);// Integer.MAX_VALUE
				}
				real_var_info.add(ti);
				latest_index.put(ti, curr_var_index);
			} else {
				seq_var_info.add(-1);
			}
		}
		Assert.isTrue(stmt_token_variable_info.size() == seq_var_info.size());
		return seq_var_info;
	}
	
}
