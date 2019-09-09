package translation.tensor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Assert;

import statistic.id.IDManager;

public class TreeTensor extends Tensor {

	public TreeTensor(IDManager im, int role) {
		super(im, role);
	}
	
	ArrayList<Integer> post_order_node_type_content_en = new ArrayList<Integer>();
	ArrayList<Integer> post_order_node_pre_order_index = new ArrayList<Integer>();

	ArrayList<Integer> post_order_node_child_start = new ArrayList<Integer>();
	ArrayList<Integer> post_order_node_child_end = new ArrayList<Integer>();
	ArrayList<Integer> post_order_node_children = new ArrayList<Integer>();
	
	ArrayList<Integer> pre_order_node_type_content_en = new ArrayList<Integer>();
	ArrayList<Integer> pre_order_node_post_order_index = new ArrayList<Integer>();	

	public void StorePostOrderNodeInfo(int en, int pre_order_index, int c_start, int c_end, ArrayList<Integer> children) {
		post_order_node_type_content_en.add(en);
		post_order_node_pre_order_index.add(pre_order_index);
		post_order_node_child_start.add(c_start);
		post_order_node_child_end.add(c_end);
		post_order_node_children.addAll(children);
	}
	
	public void StorePreOrderNodeInfo(int en) {
		pre_order_node_type_content_en.add(en);
		pre_order_node_post_order_index.add(-1);
	}
	
	public void StorePreOrderNodePostOrderIndexInfo(int pre_index, int post_order_index) {
		pre_order_node_post_order_index.set(pre_index, post_order_index);
		Assert.isTrue(post_order_node_type_content_en.get(post_order_index) == pre_order_node_type_content_en.get(pre_index));
		Assert.isTrue(post_order_node_pre_order_index.get(post_order_index) == pre_index);
	}
	
	@Override
	public int getSize() {
		return post_order_node_pre_order_index.size();
	}

	private String ToStmtInfo(String separator) {
		return  StringUtils.join(post_order_node_type_content_en.toArray(), " ") + separator
				+ StringUtils.join(post_order_node_pre_order_index.toArray(), " ") + separator
				+ StringUtils.join(post_order_node_child_start.toArray(), " ") + separator
				+ StringUtils.join(post_order_node_child_end.toArray(), " ") + separator
				+ StringUtils.join(post_order_node_children.toArray(), " ") + separator
				+ StringUtils.join(pre_order_node_type_content_en.toArray(), " ") + separator
				+ StringUtils.join(pre_order_node_post_order_index.toArray(), " ");
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
		for (Integer p : pre_order_node_post_order_index) {
			Assert.isTrue(p >= 0);
		}
	}

}
