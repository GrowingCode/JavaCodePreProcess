package translation.tensor.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

public class RepetitionUtil {
	
	Map<Integer, Integer> latest_index = new TreeMap<Integer, Integer>();
	
	public RepetitionUtil() {
		
	}
	
	public ArrayList<Integer> GenerateRepetitionRelative(ArrayList<Integer> stmt_token_variable_info) {
		int i_len = stmt_token_variable_info.size();
		ArrayList<Integer> seq_var_info = new ArrayList<Integer>();
		for (int i = 0; i < i_len; i++) {
			Assert.isTrue(seq_var_info.size() == i);
			Integer ti = stmt_token_variable_info.get(i);
			if (ti >= 0) {
				Integer li = latest_index.get(ti);
				if (li != null) {
					int relative = i - li;
					seq_var_info.add(relative);// - i
//					System.out.println("token_i:" + i + "token_en:" + ti + "#relative:" + relative);
				} else {
					seq_var_info.add(-1);// Integer.MAX_VALUE
				}
				latest_index.put(ti, i);
			} else {
				seq_var_info.add(-1);
			}
		}
		Assert.isTrue(stmt_token_variable_info.size() == seq_var_info.size());
		return seq_var_info;
	}
	
}
