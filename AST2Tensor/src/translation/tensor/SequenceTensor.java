package translation.tensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Assert;

import statistic.id.IDManager;

//public class SequenceTensor extends Tensor {
//	
//	public SequenceTensor(String origin_file, int role) {
//		super(origin_file, role);
//	}
//	
////	ArrayList<Integer> type_id = new ArrayList<Integer>();
////	ArrayList<Integer> content_id = new ArrayList<Integer>();
//	ArrayList<Integer> type_content_id = new ArrayList<Integer>();
//	ArrayList<Integer> inner_type_content_id = new ArrayList<Integer>();
//	ArrayList<Integer> parent_type_content_id = new ArrayList<Integer>();
//	ArrayList<Integer> relative_type_content_id = new ArrayList<Integer>();
//	ArrayList<Integer> api_group = new ArrayList<Integer>();
//	ArrayList<Integer> api_relative_id = new ArrayList<Integer>();
//	ArrayList<Integer> isExistedList = new ArrayList<>();
//	ArrayList<Integer> lastIndexList = new ArrayList<>();
//	ArrayList<Integer> is_real = new ArrayList<Integer>();
////	ArrayList<Integer> decode_type_id = new ArrayList<Integer>();
//	
////	ArrayList<String> type = new ArrayList<String>();
////	ArrayList<String> content = new ArrayList<String>();
//	
//	ArrayList<String> type_content_str = new ArrayList<String>();
//	
//	public void AppendOneToken(IDManager im, TypeContentID t_c, TypeContentID parent_t_c, int t_c_relative, int api_comb_id, int api_relative, int isExisted, int lastIndex, int t_c_is_real) {
////		type_id.add(type_content_id.GetTypeID());
////		content_id.add(type_content_id.GetContentID());
//		type_content_id.add(t_c.GetTypeContentID());
//		Integer inner = GenerateInnerIndexForTypeContent(t_c.GetTypeContentID());
//		inner_type_content_id.add(inner);
//		parent_type_content_id.add(parent_t_c.GetTypeContentID());
//		relative_type_content_id.add(t_c_relative);
//		api_group.add(api_comb_id);
//		api_relative_id.add(api_relative);
//		isExistedList.add(isExisted);
//		lastIndexList.add(lastIndex);
//		is_real.add(t_c_is_real);
////		decode_type_id.add(decode_type);
////		type.add(type_content_id.GetType());
////		content.add(type_content_id.GetContent());
//		type_content_str.add(t_c.GetTypeContent());
//	}
//	
////	public void SetTreeTensor(Tensor tree_tensor) {
////		this.tree_tensor = tree_tensor;
////	}
//	
//	private String ToStmtInfo(String separator) {
//		return StringUtils.join(type_content_id.toArray(), " ") + separator + StringUtils.join(inner_type_content_id.toArray(), " ") + separator + StringUtils.join(parent_type_content_id.toArray(), " ") + separator + StringUtils.join(relative_type_content_id.toArray(), " ") + separator + StringUtils.join(api_group.toArray(), " ") + separator + StringUtils.join(api_relative_id.toArray(), " ") + separator + StringUtils.join(isExistedList.toArray(), " ") + separator + StringUtils.join(lastIndexList.toArray(), " ") + separator + StringUtils.join(is_real.toArray(), " ");
//	}
//	
//	@Override
//	public String toString() {
//		// tree_tensor.toString() + "#" + 
//		// + " " + StringUtils.join(content_id.toArray(), " ") 
//		ArrayList<Integer> inner_id_type_content_id = GenerateInnerIndexesForTypeContents();
//		String result = StringUtils.join(inner_id_type_content_id.toArray(), " ") + "#" + ToStmtInfo("#");
//		return result.trim();
//	}
//
//	@Override
//	public String toDebugString() {
//		String line_seperator = System.getProperty("line.separator");
//		// StringUtils.join(content_id.toArray(), " ") + line_seperator + 
//		String result = ToStmtInfo(line_seperator);
//		return result;
//	}
//
//	@Override
//	public String toOracleString() {
//		String line_seperator = System.getProperty("line.separator");
//		// StringUtils.join(content.toArray(), " ") + line_seperator + 
//		String result = StringUtils.join(type_content_str.toArray(), " ") + line_seperator + ToStmtInfo(line_seperator);
//		return result;
//	}
//
//	@Override
//	public int getSize() {
//		return type_content_id.size();
//	}
//	
//}

public class SequenceTensor extends ASTTensor {

	ArrayList<Integer> sword_info = new ArrayList<Integer>();
	ArrayList<Integer> sword_relative_info = new ArrayList<Integer>();

	ArrayList<Integer> token_sword_start = new ArrayList<Integer>();
	ArrayList<Integer> token_sword_end = new ArrayList<Integer>();

	// String origin_file, origin_file, 
	public SequenceTensor(IDManager im, int role) {
		super(im, role);
	}

	@Override
	public void HandleAllDevoured() {
		super.HandleAllDevoured();
		int i_len = stmt_token_info.size();
		{
			Map<Integer, Integer> latest_index = new TreeMap<Integer, Integer>();
			ArrayList<Integer> seq_var_info = new ArrayList<Integer>();
			for (int i = 0; i < i_len; i++) {
				Assert.isTrue(seq_var_info.size() == i);
				Integer ti = stmt_token_info.get(i);
				Integer li = latest_index.get(ti);
				if (li != null) {
					seq_var_info.add(li - i);
				} else {
					seq_var_info.add(Integer.MAX_VALUE);
				}
				latest_index.put(ti, i);
			}
			Assert.isTrue(stmt_token_variable_info.size() == seq_var_info.size());
			stmt_token_variable_info.clear();
			stmt_token_variable_info.addAll(seq_var_info);
		}
		{
			Map<Integer, Integer> latest_index = new TreeMap<Integer, Integer>();
			for (int i = 0; i < i_len; i++) {
				Integer ti = stmt_token_info.get(i);
				Integer start = im.each_subword_sequence_start.get(ti);
				Integer end = im.each_subword_sequence_end.get(ti);
				List<Integer> seqs = im.subword_sequences.subList(start, end + 1);
				int j_len = seqs.size();
				Assert.isTrue(j_len > 0);
				for (int j = 0; j < j_len; j++) {
					Integer swi = seqs.get(j);
					Integer li = latest_index.get(swi);
					if (li != null) {
						sword_relative_info.add(li - sword_relative_info.size());
					} else {
						sword_relative_info.add(Integer.MAX_VALUE);
					}
					latest_index.put(swi, i);
				}
				token_sword_start.add(sword_info.size());
				sword_info.addAll(seqs);
				token_sword_end.add(sword_info.size()-1);
			}
			Assert.isTrue(token_sword_start.size() == stmt_token_info.size());
			Assert.isTrue(sword_info.size() == sword_relative_info.size());
		}
		ValidateStatements();
	}
	
	private void ValidateStatements() {
//		int all_token_size = stmt_token_info.size();
//		System.out.println("all_token_size:" + all_token_size);
		int stmt_size = stmt_token_info_start.size();
		int all_stmt_sword_length = 0;
		for (int i=0;i<stmt_size;i++) {
			Integer stmt_start = stmt_token_info_start.get(i);
			Integer stmt_end = stmt_token_info_end.get(i);
//			Integer start_token = stmt_token_info.get(stmt_start);
//			Assert.isTrue(start_token < all_token_size);
//			Integer end_token = stmt_token_info.get(stmt_end);
//			Assert.isTrue(end_token < all_token_size);
			Integer start_sword_idx = token_sword_start.get(stmt_start);
			Integer end_sword_idx = token_sword_end.get(stmt_end);
			int stmt_sword_length = end_sword_idx - start_sword_idx + 1;
			all_stmt_sword_length += stmt_sword_length;
		}
		Assert.isTrue(all_stmt_sword_length == sword_info.size());
	}

	@Override
	public String toString() {
		return super.toString() + "#" + StringUtils.join(sword_info.toArray(), " ") + "#"
				+ StringUtils.join(sword_relative_info.toArray(), " ") + "#"
				+ StringUtils.join(token_sword_start.toArray(), " ") + "#"
				+ StringUtils.join(token_sword_end.toArray(), " ");
	}
	
	@Override
	public String toDebugString() {
		String separator = System.getProperty("line.separator");
		return super.toDebugString() + separator + StringUtils.join(sword_info.toArray(), " ") + separator
				+ StringUtils.join(sword_relative_info.toArray(), " ") + separator
				+ StringUtils.join(token_sword_start.toArray(), " ") + separator
				+ StringUtils.join(token_sword_end.toArray(), " ") + separator + separator;
	}

}
