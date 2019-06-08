package translation.tensor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Assert;

import statistic.id.IDManager;
import translation.helper.TypeContentID;

public class StatementInfo {
	
	String stmt = null;
	int max_token_id_before_visiting_this_statement = -1;
	
	public StatementInfo(int max_token_id_before_visiting_this_statement, String stmt) {
		this.max_token_id_before_visiting_this_statement = max_token_id_before_visiting_this_statement;
		this.stmt = stmt;
	}
	
	ArrayList<Integer> type_content_id = new ArrayList<Integer>();
	ArrayList<Integer> local_token_id = new ArrayList<Integer>();
	ArrayList<Integer> is_variable = new ArrayList<Integer>();
	ArrayList<Integer> api_group = new ArrayList<Integer>();
	ArrayList<Integer> api_relative = new ArrayList<Integer>();
	
	ArrayList<String> type_content_str = new ArrayList<String>();
	
	Map<Integer, Integer> var_or_type_id_with_position_in_this_stmt = new TreeMap<Integer, Integer>();
	// legal means agree to the variable-define-use graph
	List<Integer> following_stmts_same_legal_as_this = new LinkedList<Integer>();
//	ArrayList<Boolean> depend_record = new ArrayList<Boolean>();
	
	public void StoreOneNode(IDManager im, TypeContentID t_c, int token_var_id, int is_var, int api_comb_id, int api_relative_id) {// , TypeContentID parent_t_c, int up_relative_use_num, int right_relative_use_num, int node_to_encode, int isExisted, int lastIndex, int node_is_real, int up_contingent_index, int right_contingent_index,
		// base data
		type_content_id.add(t_c.GetTypeContentID());
		local_token_id.add(token_var_id);
		is_variable.add(is_var);
		if (token_var_id == -1) {
			Assert.isTrue(token_var_id == is_var);
		} else {
			Assert.isTrue(is_var >= 0);
			int token_var_position_in_stmt = type_content_id.size()-1;
			var_or_type_id_with_position_in_this_stmt.put(token_var_id, token_var_position_in_stmt);
		}
		api_group.add(api_comb_id);
		api_relative.add(api_relative_id);
		
		type_content_str.add(t_c.GetTypeContent());
	}
	
	public int Size() {
		return type_content_id.size();
	}
	
	public String GetStatement() {
		return stmt;
	}
	
	public String GetTypeContentOfStatement() {
		return StringUtils.join(type_content_str.toArray(), '~');
	}
	
	@Override
	public String toString() {
		return stmt;
	}
	
}
