package translation.tensor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

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

	public void StorePostOrderNodeInfo() {
		
	}
	
	public void StorePreOrderNodeEnInfo() {
		
	}
	
	public void StorePreOrderNodePostOrderIndexInfo() {
		
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

}
