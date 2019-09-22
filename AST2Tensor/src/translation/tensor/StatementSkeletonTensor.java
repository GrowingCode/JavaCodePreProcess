package translation.tensor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class StatementSkeletonTensor extends Tensor {

	ArrayList<Integer> stmt_token_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_info_start = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_info_end = new ArrayList<Integer>();
	
	public void StoreStatementSkeletonInfo(ArrayList<Integer> info) {
		stmt_token_info_start.add(stmt_token_info.size());
		stmt_token_info.addAll(info);
		stmt_token_info_end.add(stmt_token_info.size()-1);
	}
	
	@Override
	public int getSize() {
		return stmt_token_info.size();
	}
	
	private String ToStmtInfo(String separator) {
		return StringUtils.join(stmt_token_info.toArray(), " ") + separator
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
