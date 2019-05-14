package translation.tensor;

import java.util.ArrayList;

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
	
	ArrayList<String> type_content_str = new ArrayList<String>();
	
	public void StoreOneNode(IDManager im, TypeContentID t_c, int token_local_id, int is_var, int api_comb_id) {// , TypeContentID parent_t_c, int up_relative_use_num, int right_relative_use_num, int node_to_encode, int isExisted, int lastIndex, int node_is_real, int up_contingent_index, int right_contingent_index,
		// base data
		type_content_id.add(t_c.GetTypeContentID());
		local_token_id.add(token_local_id);
		is_variable.add(is_var);
		if (token_local_id == -1) {
			Assert.isTrue(token_local_id == is_var);
		}
		api_group.add(api_comb_id);
		
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
	
}
