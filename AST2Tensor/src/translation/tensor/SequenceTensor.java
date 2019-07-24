package translation.tensor;

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
	
	// String origin_file, origin_file, 
	public SequenceTensor(String origin_file, IDManager im, int role) {
		super(origin_file, im, role);
	}

	@Override
	public String toString() {
		return super.toString();
	}
	
	@Override
	public String toDebugString() {
		return super.toDebugString();
	}

}
