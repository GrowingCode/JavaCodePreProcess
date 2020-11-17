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
	ArrayList<Integer> stmt_token_kind = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_info_start = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_info_end = new ArrayList<Integer>();
//	ArrayList<Integer> stmt_token_struct_info_end = new ArrayList<Integer>();
	
	TreeMap<String, Integer> token_index_record = new TreeMap<String, Integer>();
	TokenIndex t_idx = new TokenIndex();

	public SktBatchTensor skt_batch_info = null;
	public SktBatchTensor skt_pe_batch_info = null;
	public SktBatchTensor skt_each_batch_info = null;
	
	public StatementSkeletonTensor(TensorInfo tinfo, String sig) {
		super(tinfo);
		this.sig = sig;
	}
	
	public void StoreStatementSkeletonInfo(ArrayList<String> info_str, ArrayList<Integer> info, ArrayList<Integer> kind, 
			ArrayList<Integer> is_var) {
		Assert.isTrue(info.size() == is_var.size() && info.size() == kind.size(), "info.size():" + info.size() + "#is_var.size():" + is_var.size() + "#kind.size():" + kind.size());
		stmt_token_str.addAll(info_str);
		
		stmt_token_info_start.add(stmt_token_info.size());
		stmt_token_info.addAll(info);
		stmt_token_kind.addAll(kind);
		stmt_token_info_end.add(stmt_token_info.size() - 1);
		ArrayList<Integer> leaf_info = new ArrayList<Integer>();
		for (int i = 0; i < info.size(); i++) {
			int leaf_id = -1;
			if (is_var.get(i) > 0) {
				leaf_id = TokenIndexUtil.AssignID(token_index_record, info.get(i) + "", t_idx);
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
	
	/**
	 * In the following 3 functions, 4 inputs are needed: 
	 * origin_sequence, relative_to_part_first, valid_mask, seq_part_skip
	 * all these 4 arrays need to insert 0th element in the array
	 */
	
	public void StoreStatementSkeletonBatchInfo(int skt_hit_num, ArrayList<Integer> skt, ArrayList<Integer> token) {
		SktBatchTensor one_stmt = HandleSktInfo(skt_hit_num, skt, token);
		skt_batch_info.Merge(one_stmt);
	}
	
	public void StoreStatementSkeletonPEBatchInfo(int skt_hit_num, ArrayList<Integer> skt, ArrayList<Integer> token) {
		SktBatchTensor one_stmt = HandleSktInfo(skt_hit_num, skt, token);
		skt_pe_batch_info.Merge(one_stmt);
	}
	
	public void StoreStatementSkeletonEachBatchInfo(int skt_hit_num, ArrayList<Integer> skt, ArrayList<Integer> token) {
		SktBatchTensor one_stmt = HandleSktInfo(skt_hit_num, skt, token);
		skt_each_batch_info.Merge(one_stmt);
	}
	
	private SktBatchTensor HandleSktInfo(int skt_hit_num, ArrayList<Integer> skt, ArrayList<Integer> token) {
		SktBatchTensor sbt = new SktBatchTensor(ti);
		
		sbt.origin_sequence.add(0);
		sbt.relative_to_part_first.add(0);
		sbt.valid_mask.add(0);
		sbt.seq_part_skip.add(1);
		
		sbt.origin_sequence.addAll(skt);
		for (int t : token) {
			sbt.origin_sequence.add(t + skt_hit_num);
		}
		
		int r = 0;
		for (int s : skt) {
			assert s >= 0;
			sbt.relative_to_part_first.add(r);
			r++;
		}
		r = 0;
		for (int t : token) {
			assert t >= 0;
			sbt.relative_to_part_first.add(r);
			r++;
		}
		
		for (int s : skt) {
			if (s > 2) {
				sbt.valid_mask.add(1);
			} else {
				sbt.valid_mask.add(0);
			}
		}
		for (int t : token) {
			if (t > 2) {
				sbt.valid_mask.add(1);
			} else {
				sbt.valid_mask.add(0);
			}
		}
		
		int q = 0;
		for (int s : skt) {
			assert s >= 0;
			if (q == 0) {
				sbt.seq_part_skip.add(skt.size());
			} else {
				sbt.seq_part_skip.add(0);
			}
			q++;
		}
		q = 0;
		for (int t : token) {
			assert t >= 0;
			if (q == 0) {
				sbt.seq_part_skip.add(token.size());
			} else {
				sbt.seq_part_skip.add(0);
			}
		}
		
		return sbt;
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
				+ StringUtils.join(stmt_token_kind.toArray(), " ") + separator
				+ StringUtils.join(stmt_token_info_start.toArray(), " ") + separator
				+ StringUtils.join(stmt_token_info_end.toArray(), " ");
//				+ separator + StringUtils.join(stmt_token_struct_info_end.toArray(), " ");
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
				r += stmt_token_str.get(j) + "    ";
			}
			result.append(r.trim() + separator);
		}
		return result.toString();
	}

}

class SktBatchTensor extends Tensor {
	
	ArrayList<String> origin_sequence_str = new ArrayList<String>();
	ArrayList<Integer> origin_sequence = new ArrayList<Integer>();
	ArrayList<Integer> relative_to_part_first = new ArrayList<Integer>();
	ArrayList<Integer> valid_mask = new ArrayList<Integer>();
	ArrayList<Integer> seq_part_skip = new ArrayList<Integer>();
	
	public SktBatchTensor(TensorInfo tinfo) {
		super(tinfo);
	}
	
	public void Merge(SktBatchTensor sbt) {
		origin_sequence_str.addAll(sbt.origin_sequence_str);
		origin_sequence.addAll(sbt.origin_sequence);
		relative_to_part_first.addAll(sbt.relative_to_part_first);
		valid_mask.addAll(sbt.valid_mask);
		seq_part_skip.addAll(sbt.seq_part_skip);
	}

	@Override
	public int getSize() {
		return origin_sequence.size();
	}

	@Override
	public String toString() {
		String separator = "#";
		String result = StringUtils.join(origin_sequence.toArray(), " ") + separator
				+ StringUtils.join(relative_to_part_first.toArray(), " ") + separator
				+ StringUtils.join(valid_mask.toArray(), " ") + separator
				+ StringUtils.join(seq_part_skip.toArray(), " ");
		return result;
	}
	
	public String toDebugString() {
		String separator = System.getProperty("line.separator");
		String result = StringUtils.join(origin_sequence.toArray(), " ") + separator
				+ StringUtils.join(relative_to_part_first.toArray(), " ") + separator
				+ StringUtils.join(valid_mask.toArray(), " ") + separator
				+ StringUtils.join(seq_part_skip.toArray(), " ");
		return result;
	}
	
	@Override
	public String toOracleString() {
		String separator = System.getProperty("line.separator");
		String result = StringUtils.join(origin_sequence_str.toArray(), " ") + separator
				+ StringUtils.join(relative_to_part_first.toArray(), " ") + separator
				+ StringUtils.join(valid_mask.toArray(), " ") + separator
				+ StringUtils.join(seq_part_skip.toArray(), " ");
		return result;
	}
	
}



