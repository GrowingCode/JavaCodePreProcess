package translation.tensor;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Assert;

import com.google.gson.Gson;

import main.MetaOfApp;
import statistic.id.SktHintManager;
import translation.tensor.util.ConservedMemoryUtil;
import translation.tensor.util.IntsWrapper;
import translation.tensor.util.RepetitionUtil;
import translation.tensor.util.TokenIndex;
import translation.tensor.util.TokenIndexUtil;
import util.FileUtil;
import util.ReflectUtil;
import util.StringUtil;

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
	
	public IntsWrapper skt_batch_relative_part_max = null;
	public IntsWrapper skt_pe_batch_relative_part_max = null;
	public IntsWrapper skt_each_batch_relative_part_max = null;
	
	public SktBatchTensor skt_batch_info = null;
	public SktBatchTensor skt_pe_batch_info = null;
	public SktBatchTensor skt_each_batch_info = null;
	
	public static int max_skt_number_of_one_statement = Integer.MIN_VALUE;
	public static int max_token_number_of_one_statement = Integer.MIN_VALUE;
	public static int max_skt_number_of_pe_statement = Integer.MIN_VALUE;
	public static int max_token_number_of_pe_statement = Integer.MIN_VALUE;
	public static int max_skt_number_of_e_statement = Integer.MIN_VALUE;
	public static int max_token_number_of_e_statement = Integer.MIN_VALUE;
	
	public StatementSkeletonTensor(TensorInfo tinfo, String sig, IntsWrapper skt_batch_relative_part_max, IntsWrapper skt_pe_batch_relative_part_max, IntsWrapper skt_each_batch_relative_part_max) {
		super(tinfo);
		this.sig = sig;
		skt_batch_info = new SktBatchTensor(tinfo, true);
		skt_pe_batch_info = new SktBatchTensor(tinfo, true);
		skt_each_batch_info = new SktBatchTensor(tinfo, true);
		
		this.skt_batch_relative_part_max = skt_batch_relative_part_max;
		this.skt_pe_batch_relative_part_max = skt_pe_batch_relative_part_max;
		this.skt_each_batch_relative_part_max = skt_each_batch_relative_part_max;
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
				leaf_id = TokenIndexUtil.AssignID(token_index_record, info_str.get(i), t_idx);
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
	
	public void StoreStatementSkeletonBatchInfo(SktHintManager pshm, String ifx, int skt_hit_num, int skt_token_hit_num, ArrayList<String> skt_str, ArrayList<Integer> parent_skt_hint, ArrayList<Integer> skt, ArrayList<Integer> skt_exact, ArrayList<String> token_str, ArrayList<Integer> parent_token_hint, ArrayList<Integer> token) {
		SktBatchTensor one_stmt = HandleSktInfo(pshm, ifx, skt_batch_relative_part_max, skt_hit_num, skt_token_hit_num, skt_str, parent_skt_hint, skt, skt_exact, token_str, parent_token_hint, token);
		skt_batch_info.Merge(one_stmt);
		
		max_skt_number_of_one_statement = Math.max(max_skt_number_of_one_statement, skt_str.size());
		max_token_number_of_one_statement = Math.max(max_token_number_of_one_statement, token_str.size());
	}
	
	public void StoreStatementSkeletonPEBatchInfo(SktHintManager pshm, String ifx, int skt_hit_num, int skt_token_hit_num, ArrayList<String> skt_str, ArrayList<Integer> parent_skt_hint, ArrayList<Integer> skt, ArrayList<Integer> skt_exact, ArrayList<String> token_str, ArrayList<Integer> parent_token_hint, ArrayList<Integer> token) {
		SktBatchTensor one_stmt = HandleSktInfo(pshm, ifx, skt_pe_batch_relative_part_max, skt_hit_num, skt_token_hit_num, skt_str, parent_skt_hint, skt, skt_exact, token_str, parent_token_hint, token);
		skt_pe_batch_info.Merge(one_stmt);
		
		max_skt_number_of_pe_statement = Math.max(max_skt_number_of_pe_statement, skt_str.size());
		max_token_number_of_pe_statement = Math.max(max_token_number_of_pe_statement, token_str.size());
	}
	
	public void StoreStatementSkeletonEachBatchInfo(SktHintManager pshm, String ifx, int skt_hit_num, int skt_token_hit_num, ArrayList<String> skt_str, ArrayList<Integer> parent_skt_hint, ArrayList<Integer> skt, ArrayList<Integer> skt_exact, ArrayList<String> token_str, ArrayList<Integer> parent_token_hint, ArrayList<Integer> token) {
		SktBatchTensor one_stmt = HandleSktInfo(pshm, ifx, skt_each_batch_relative_part_max, skt_hit_num, skt_token_hit_num, skt_str, parent_skt_hint, skt, skt_exact, token_str, parent_token_hint, token);
		skt_each_batch_info.Merge(one_stmt);
		
		max_skt_number_of_e_statement = Math.max(max_skt_number_of_e_statement, skt_str.size());
		max_token_number_of_e_statement = Math.max(max_token_number_of_e_statement, token_str.size());
	}
	
	private SktBatchTensor HandleSktInfo(SktHintManager pshm, String ifx, IntsWrapper iw, int skt_hit_num, int skt_token_hit_num, ArrayList<String> skt_str, ArrayList<Integer> parent_skt_hint, ArrayList<Integer> skt, ArrayList<Integer> skt_exact, ArrayList<String> token_str, ArrayList<Integer> parent_token_hint, ArrayList<Integer> token) {
		
		SktBatchTensor sbt = new SktBatchTensor(ti, false);
		
		sbt.origin_sequence_str.addAll(skt_str);
		sbt.origin_sequence_str.addAll(token_str);
		
		ArrayList<Integer> r_tokens = new ArrayList<Integer>();
		for (int t : token) {
			int h_t = t + skt_hit_num;
			Assert.isTrue(h_t < skt_hit_num + skt_token_hit_num);
			r_tokens.add(h_t);
		}
		
		sbt.origin_sequence.addAll(skt);
		for (int j=0;j<skt.size();j++) {
			int os = skt.get(j);
			int pos_hint = j * 2 + 1;
			ReflectUtil.ReflectMethod("Regist" + ifx + "PositionHintID" + ifx + "TokenID", pshm, new Class<?>[] {int.class, int.class}, new Object[] {pos_hint, os});
			sbt.position_hint.add(pos_hint);
		}
		
		sbt.origin_sequence.addAll(r_tokens);
		for (int j=0;j<r_tokens.size();j++) {
			int os = r_tokens.get(j);
			int pos_hint = j * 2 + 2;
			ReflectUtil.ReflectMethod("Regist" + ifx + "PositionHintID" + ifx + "TokenID", pshm, new Class<?>[] {int.class, int.class}, new Object[] {pos_hint, os});
			sbt.position_hint.add(pos_hint);
		}
		
		int r = 0;
		for (int s : skt) {
			assert s >= 0;
			sbt.relative_to_part_first.add(r);
			r++;
		}
		if (iw.it1 == null) {
			iw.it1 = r;
		} else {
			iw.it1 = iw.it1 < r ? r : iw.it1;
		}
		r = 0;
		for (int t : token) {
			assert t >= 0;
			sbt.relative_to_part_first.add(r);
			r++;
		}
		if (iw.it2 == null) {
			iw.it2 = r;
		} else {
			iw.it2 = iw.it2 < r ? r : iw.it2;
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
		
		sbt.parent_hint.addAll(parent_skt_hint);
		sbt.parent_hint.addAll(parent_token_hint);
		
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
		
		for (int s : skt) {
			assert s >= 0;
			sbt.token_type.add(0);
		}
		for (int t : token) {
			assert t >= 0;
			sbt.token_type.add(1);
		}
		
		sbt.origin_sequence_exact.addAll(skt_exact);
		sbt.origin_sequence_exact.addAll(r_tokens);
		
		int os_size = sbt.origin_sequence.size();
		int ph_size = sbt.parent_hint.size();
		Assert.isTrue(os_size == ph_size);
		
		for (int j=0;j<os_size;j++) {
			int ph = sbt.parent_hint.get(j);
			int os = sbt.origin_sequence.get(j);
			
			ReflectUtil.ReflectMethod("Regist" + ifx + "ParentHintID" + ifx + "TokenID", pshm, new Class<?>[] {int.class, int.class}, new Object[] {ph, os});
		}
		
		return sbt;
	}
	
	public void HandleAllInfo() {
		stmt_token_leaf_relative_info.addAll(RepetitionUtil.GenerateRepetitionRelative(stmt_token_leaf_info));
		stmt_token_conserved_memory_length.addAll(ConservedMemoryUtil.GenerateConservedMemory(stmt_token_leaf_info,
				stmt_token_leaf_relative_info, MetaOfApp.ConservedContextLength));
		SetSize(stmt_token_info.size());
		skt_batch_info.HandleAllInfo();
		skt_pe_batch_info.HandleAllInfo();
		skt_each_batch_info.HandleAllInfo();
	}
	
	@Override
	public int GetSize() {
		return stmt_token_info.size();
	}
	
	@Override
	public void SetRole(int role) {
		super.SetRole(role);
		skt_batch_info.SetRole(role);
		skt_pe_batch_info.SetRole(role);
		skt_each_batch_info.SetRole(role);
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
	
	public static void GenerateSktTensorSummary(String dir) {
		Gson gson = new Gson();
		TreeMap<String, Integer> meta_of_skt = new TreeMap<String, Integer>();
		meta_of_skt.put("max_skt_number_of_one_statement", max_skt_number_of_one_statement);
		meta_of_skt.put("max_token_number_of_one_statement", max_token_number_of_one_statement);
		meta_of_skt.put("max_skt_number_of_pe_statement", max_skt_number_of_pe_statement);
		meta_of_skt.put("max_token_number_of_pe_statement", max_token_number_of_pe_statement);
		meta_of_skt.put("max_skt_number_of_e_statement", max_skt_number_of_e_statement);
		meta_of_skt.put("max_token_number_of_e_statement", max_token_number_of_e_statement);
		FileUtil.WriteToFile(new File(dir + "/" + "All_skt_tensor_summary.json"), gson.toJson(meta_of_skt));
		String skt_meta = "max_skt_number_of_one_statement:" + max_skt_number_of_one_statement
				+ "\n" + "max_token_number_of_one_statement:" + max_token_number_of_one_statement
				+ "\n" + "max_skt_number_of_pe_statement:" + max_skt_number_of_pe_statement
				+ "\n" + "max_token_number_of_pe_statement:" + max_token_number_of_pe_statement
				+ "\n" + "max_skt_number_of_e_statement:" + max_skt_number_of_e_statement
				+ "\n" + "max_token_number_of_e_statement:" + max_token_number_of_e_statement;
		System.out.println(skt_meta);
	}

}

class SktBatchTensor extends Tensor {
	
	ArrayList<String> origin_sequence_str = new ArrayList<String>();
	ArrayList<Integer> origin_sequence = new ArrayList<Integer>();
	ArrayList<Integer> relative_to_part_first = new ArrayList<Integer>();
	ArrayList<Integer> valid_mask = new ArrayList<Integer>();
	ArrayList<Integer> parent_hint = new ArrayList<Integer>();
	ArrayList<Integer> position_hint = new ArrayList<Integer>();
	ArrayList<Integer> seq_part_skip = new ArrayList<Integer>();
	ArrayList<Integer> token_type = new ArrayList<Integer>();
	ArrayList<Integer> origin_sequence_exact = new ArrayList<Integer>();
	
	int print_fixed_len = 25;
	
	public SktBatchTensor(TensorInfo tinfo, boolean insert_default_element) {
		super(tinfo);
		if (insert_default_element) {
			this.origin_sequence_str.add("dft");
			this.origin_sequence.add(0);
			this.relative_to_part_first.add(0);
			this.valid_mask.add(0);
			this.parent_hint.add(0);
			this.position_hint.add(0);
			this.seq_part_skip.add(0);
			this.token_type.add(-1);
			this.origin_sequence_exact.add(0);
		}
	}
	
	public void Merge(SktBatchTensor sbt) {
		origin_sequence_str.addAll(sbt.origin_sequence_str);
		origin_sequence.addAll(sbt.origin_sequence);
		relative_to_part_first.addAll(sbt.relative_to_part_first);
		valid_mask.addAll(sbt.valid_mask);
		parent_hint.addAll(sbt.parent_hint);
		position_hint.addAll(sbt.position_hint);
		seq_part_skip.addAll(sbt.seq_part_skip);
		token_type.addAll(sbt.token_type);
		origin_sequence_exact.addAll(sbt.origin_sequence_exact);
	}

	public void HandleAllInfo() {
		SetSize(origin_sequence.size());
	}
	
	@Override
	public int GetSize() {
		return origin_sequence.size();
	}

	@Override
	public String toString() {
		String separator = "#";
		String result = StringUtils.join(origin_sequence.toArray(), " ") + separator
				+ StringUtils.join(relative_to_part_first.toArray(), " ") + separator
				+ StringUtils.join(valid_mask.toArray(), " ") + separator
				+ StringUtils.join(parent_hint.toArray(), " ") + separator
				+ StringUtils.join(position_hint.toArray(), " ") + separator
				+ StringUtils.join(seq_part_skip.toArray(), " ") + separator
				+ StringUtils.join(token_type.toArray(), " ") + separator
				+ StringUtils.join(origin_sequence_exact.toArray(), " ");
		return result;
	}
	
	public String toDebugString() {
		String separator = System.getProperty("line.separator");
		String result = StringUtil.FixedLengthString("id:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(origin_sequence, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("relative_first:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(relative_to_part_first, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("valid_mask:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(valid_mask, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("parent_hint:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(parent_hint, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("position_hint:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(position_hint, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("seq_part_skip:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(seq_part_skip, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("token_type:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(token_type, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("id_exact:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(origin_sequence_exact, print_fixed_len).toArray(), " ");
		return result;
	}
	
	@Override
	public String toOracleString() {
		String separator = System.getProperty("line.separator");
		String result = StringUtil.FixedLengthString("id:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(origin_sequence_str, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("relative_first:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(relative_to_part_first, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("valid_mask:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(valid_mask, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("parent_hint:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(parent_hint, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("position_hint:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(position_hint, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("seq_part_skip:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(seq_part_skip, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("token_type:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(token_type, print_fixed_len).toArray(), " ") + separator
				+ StringUtil.FixedLengthString("id_exact:", 20) + " " + StringUtils.join(StringUtil.ArrayElementToFixedLength(origin_sequence_exact, print_fixed_len).toArray(), " ");
		return result;
	}
	
}



