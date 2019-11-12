package translation.tensor;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import translation.tensor.util.IDRedistribution;
import translation.tensor.util.RepetitionUtil;
import translation.tensor.util.TokenIndex;

public class StatementSkeletonTensor extends Tensor {
	
	ArrayList<String> stmt_token_str = new ArrayList<String>();

	ArrayList<Integer> stmt_token_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_leaf_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_leaf_relative_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_info_start = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_info_end = new ArrayList<Integer>();
	
	TreeMap<String, Integer> token_index_record = new TreeMap<String, Integer>();
	TokenIndex ti = new TokenIndex();
	
	public void StoreStatementSkeletonInfo(ArrayList<String> info_str, ArrayList<Integer> info) {
		stmt_token_str.addAll(info_str);
		
		stmt_token_info_start.add(stmt_token_info.size());
		stmt_token_info.addAll(info);
		stmt_token_info_end.add(stmt_token_info.size()-1);
		ArrayList<Integer> leaf_info = new ArrayList<Integer>();
		leaf_info.add(-1);
		for (int i=1;i<info.size();i++) {
			Integer leaf_id = IDRedistribution.AssignID(token_index_record, info.get(i)+"", ti);
			leaf_info.add(leaf_id);
		}
		stmt_token_leaf_info.addAll(leaf_info);
		stmt_token_leaf_relative_info.addAll(RepetitionUtil.GenerateRepetitionRelative(leaf_info));
	}
	
	@Override
	public int getSize() {
		return stmt_token_info.size();
	}
	
	private String ToStmtInfo(String separator) {
		return StringUtils.join(stmt_token_info.toArray(), " ") + separator
				+ StringUtils.join(stmt_token_leaf_info.toArray(), " ") + separator
				+ StringUtils.join(stmt_token_leaf_relative_info.toArray(), " ") + separator
				+ StringUtils.join(stmt_token_info_start.toArray(), " ") + separator
				+ StringUtils.join(stmt_token_info_end.toArray(), " ");
	}

	@Override
	public String toString() {
		String separator = "#";
		String result = ToStmtInfo(separator);
		return result.trim();
	}

	@Override
	public String toDebugString() {
		String separator = System.getProperty("line.separator");
		String result = ToStmtInfo(separator);
		return result;
	}

	@Override
	public String toOracleString() {
		String separator = System.getProperty("line.separator");
		String result = ToStmtInfo(separator);
		return result;
	}
	
}
