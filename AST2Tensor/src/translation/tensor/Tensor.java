package translation.tensor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Tensor {
	
	ArrayList<Integer> left_child = new ArrayList<Integer>();
	ArrayList<Integer> right_child = new ArrayList<Integer>();
	ArrayList<Integer> type = new ArrayList<Integer>();
	ArrayList<Integer> content = new ArrayList<Integer>();
	
	public Tensor() {
	}
	
	public void StoreOneASTNode(int node_idx, int left_child_node_idx, int right_child_node_idx, int type_id, int content_id) {
		left_child.set(node_idx, left_child_node_idx);
		right_child.set(node_idx, right_child_node_idx);
		type.set(node_idx, type_id);
		content.set(node_idx, content_id);
	}
	
	@Override
	public String toString() {
		String result = StringUtils.join(left_child.toArray(), " ") + " " + StringUtils.join(right_child.toArray(), " ") + " " + StringUtils.join(type.toArray(), " ") + " " + StringUtils.join(content.toArray(), " ");
		return result;
	}
	
}
