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
		SetValueAtIndex(left_child, node_idx, left_child_node_idx);
		SetValueAtIndex(right_child, node_idx, right_child_node_idx);
		SetValueAtIndex(type, node_idx, type_id);
		SetValueAtIndex(content, node_idx, content_id);
	}
	
	@Override
	public String toString() {
		String result = StringUtils.join(left_child.toArray(), " ") + " " + StringUtils.join(right_child.toArray(), " ") + " " + StringUtils.join(type.toArray(), " ") + " " + StringUtils.join(content.toArray(), " ");
		return result;
	}
	
	private void SetValueAtIndex(ArrayList<Integer> array_list, int index, Integer value) {
		int size = array_list.size();
		if (index >= size) {
			int gap = index - size + 1;
			for (int i=0;i<gap;i++) {
				array_list.add(-1);
			}
		}
		array_list.set(index, value);
	}
	
}
