package translation.tensor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import main.MetaOfApp;
import translation.helper.TypeContentID;
import translation.tensor.util.TokenKindUtil;
import tree.TreeNode;

public class StatementInfo {

	String stmt = null;

	public StatementInfo(String stmt) {
		this.stmt = stmt;
	}

	ArrayList<Integer> type_content_id = new ArrayList<Integer>();
	ArrayList<Integer> type_content_kind = new ArrayList<Integer>();
	ArrayList<String> local_token_str = new ArrayList<String>();
	ArrayList<TreeNode> self = new ArrayList<TreeNode>();
	ArrayList<TreeNode> parent = new ArrayList<TreeNode>();
//	ArrayList<Integer> is_variable = new ArrayList<Integer>();
	ArrayList<Integer> api_group = new ArrayList<Integer>();
	ArrayList<Integer> api_relative = new ArrayList<Integer>();

	ArrayList<String> type_content_str = new ArrayList<String>();

	Map<String, Integer> var_or_type_id_with_position_in_this_stmt = new TreeMap<String, Integer>();
	// legal means agree to the variable-define-use graph
	List<Integer> following_stmts_same_legal_as_this = new LinkedList<Integer>();
//	ArrayList<Boolean> depend_record = new ArrayList<Boolean>();

	public void StoreOneNode(TypeContentID t_c, String token_var, TreeNode self_n, TreeNode parent_n, int api_comb_id, int api_relative_id) {
		// base data
		type_content_id.add(t_c.GetTypeContentID());
		type_content_kind.add(TokenKindUtil.GetTokenKind(self_n));
		if (MetaOfApp.VariableNoLimit) {
			token_var = t_c.GetTypeContent();
		}
		local_token_str.add(token_var);
		self.add(self_n);
		parent.add(parent_n);
//		is_variable.add(is_var);

		if (token_var == null) {
//			Assert.isTrue(token_var_id == is_var);
		} else {
//			System.out.println("var_type_content:" + t_c.GetTypeContent());
			int token_var_position_in_stmt = type_content_id.size() - 1;
			if (!var_or_type_id_with_position_in_this_stmt.containsKey(token_var)) {
				var_or_type_id_with_position_in_this_stmt.put(token_var, token_var_position_in_stmt);
			}
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
