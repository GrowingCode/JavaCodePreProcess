package translation.tensor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class TreeTensor extends Tensor {

//	public TreeTensor(IDManager im) {// , int role
//		super(im);// , role
//	}

	ArrayList<Integer> post_order_node_type_content_en = new ArrayList<Integer>();
	ArrayList<Integer> post_order_node_child_start = new ArrayList<Integer>();
	ArrayList<Integer> post_order_node_child_end = new ArrayList<Integer>();
	ArrayList<Integer> post_order_node_children = new ArrayList<Integer>();

	ArrayList<Integer> pre_post_order_node_type_content_en = new ArrayList<Integer>();
	ArrayList<Integer> pre_post_order_node_state = new ArrayList<Integer>();
	ArrayList<Integer> pre_post_order_node_post_order_index = new ArrayList<Integer>();

	public int StorePostOrderNodeInfo(int en, ArrayList<Integer> children) {
		int index = post_order_node_type_content_en.size();
		post_order_node_type_content_en.add(en);
		int c_start = post_order_node_children.size();
		post_order_node_children.addAll(children);
		int c_end = post_order_node_children.size()-1;
//		if (children.size() == 0) {
//			c_start = 0;
//			c_end = -1;
//		}
		post_order_node_child_start.add(c_start);
		post_order_node_child_end.add(c_end);
		return index;
	}

	public int StorePrePostOrderNodeInfo(int en, int state, int post_order_index) {
		int index = pre_post_order_node_type_content_en.size();
		pre_post_order_node_type_content_en.add(en);
		pre_post_order_node_state.add(state);
		pre_post_order_node_post_order_index.add(post_order_index);
		return index;
	}

//	public void StorePreOrderNodePostOrderIndexInfo(int pre_index, int post_order_index) {
//		pre_order_node_post_order_index.set(pre_index, post_order_index);
//		Assert.isTrue(post_order_node_type_content_en.get(post_order_index) == pre_order_node_type_content_en.get(pre_index));
//		Assert.isTrue(post_order_node_pre_order_index.get(post_order_index) == pre_index);
//	}

	@Override
	public int getSize() {
		return post_order_node_type_content_en.size();
	}

	private String ToStmtInfo(String separator) {
		return StringUtils.join(post_order_node_type_content_en.toArray(), " ") + separator
				+ StringUtils.join(post_order_node_child_start.toArray(), " ") + separator
				+ StringUtils.join(post_order_node_child_end.toArray(), " ") + separator
				+ StringUtils.join(post_order_node_children.toArray(), " ") + separator
				+ StringUtils.join(pre_post_order_node_type_content_en.toArray(), " ") + separator
				+ StringUtils.join(pre_post_order_node_state.toArray(), " ") + separator
				+ StringUtils.join(pre_post_order_node_post_order_index.toArray(), " ");
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

	public void Validate() {
//		for (Integer p : pre_order_node_post_order_index) {
//			Assert.isTrue(p >= 0);
//		}
	}

}
