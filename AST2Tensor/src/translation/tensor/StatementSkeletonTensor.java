package translation.tensor;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Assert;

import main.MetaOfApp;
import translation.tensor.util.ConservedMemoryUtil;
import translation.tensor.util.RepetitionUtil;
import translation.tensor.util.TokenIndex;
import translation.tensor.util.TokenIndexUtil;

public class StatementSkeletonTensor extends Tensor {

	String sig = null;
	ArrayList<String> stmt_token_str = new ArrayList<String>();

	ArrayList<Integer> stmt_token_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_leaf_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_leaf_relative_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_conserved_memory_length = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_info_start = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_info_end = new ArrayList<Integer>();

	TreeMap<String, Integer> token_index_record = new TreeMap<String, Integer>();
	TokenIndex ti = new TokenIndex();
	
	public StatementSkeletonTensor(TensorInfo tinfo) {
		super(tinfo);
	}	

	public void StoreStatementSkeletonInfo(String sig, ArrayList<String> info_str, ArrayList<Integer> info,
			ArrayList<Integer> is_var) {
		this.sig = sig;
		stmt_token_str.addAll(info_str);

		stmt_token_info_start.add(stmt_token_info.size());
		stmt_token_info.addAll(info);
		stmt_token_info_end.add(stmt_token_info.size() - 1);
		ArrayList<Integer> leaf_info = new ArrayList<Integer>();
		for (int i = 0; i < info.size(); i++) {
			int leaf_id = -1;
			if (is_var.get(i) > 0) {
				leaf_id = TokenIndexUtil.AssignID(token_index_record, info.get(i) + "", ti);
			}
			leaf_info.add(leaf_id);
		}
		stmt_token_leaf_info.addAll(leaf_info);
//		System.out.println(" ==== stmt_token_variable_info begin! ==== ");
//		for (int vi : leaf_info) {
//			System.out.println(vi);
//		}
//		System.out.println(" ==== stmt_token_variable_info end! ==== ");
//		new Exception("haha").printStackTrace();
//		System.exit(1);
	}

	public void HandleAllInfo() {
		stmt_token_leaf_relative_info.addAll(RepetitionUtil.GenerateRepetitionRelative(stmt_token_leaf_info));
		stmt_token_conserved_memory_length.addAll(ConservedMemoryUtil.GenerateConservedMemory(stmt_token_leaf_info,
				stmt_token_leaf_relative_info, MetaOfApp.ConservedContextLength));
	}

	@Override
	public int getSize() {
		return stmt_token_info.size();
	}

	private String ToStmtInfo(String separator) {
		return StringUtils.join(stmt_token_info.toArray(), " ") + separator
				+ StringUtils.join(stmt_token_leaf_info.toArray(), " ") + separator
				+ StringUtils.join(stmt_token_leaf_relative_info.toArray(), " ") + separator
				+ StringUtils.join(stmt_token_conserved_memory_length.toArray(), " ") + separator
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
		StringBuilder result = new StringBuilder();
		int s_size = stmt_token_info_start.size();
		int e_size = stmt_token_info_end.size();
		Assert.isTrue(s_size == e_size);
		result.append(MetaOfApp.MethodDeclarationSignaturePrefix + this.sig + separator);
		for (int i = 0; i < e_size; i++) {
			String r = "";
			Integer s = stmt_token_info_start.get(i);
			Integer e = stmt_token_info_end.get(i);
			for (int j = s; j <= e; j++) {
				r += stmt_token_str.get(j) + "\t";
			}
			result.append(r.trim() + separator);
		}
		return result.toString();
	}

}
