package translation.tensor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Tensor {
	
	ArrayList<Integer> left_child = new ArrayList<Integer>();
	ArrayList<Integer> right_child = new ArrayList<Integer>();
	ArrayList<Integer> type = new ArrayList<Integer>();
	ArrayList<Integer> content = new ArrayList<Integer>();
	
	ArrayList<String> type_oracle = new ArrayList<String>();
	ArrayList<String> content_oracle = new ArrayList<String>();
	
	public Tensor() {
	}
	
	public void StoreOneASTNode(int node_idx, int left_child_node_idx, int right_child_node_idx, int type_id, int content_id) {
		SetValueAtIndex(left_child, node_idx, left_child_node_idx);
		SetValueAtIndex(right_child, node_idx, right_child_node_idx);
		SetValueAtIndex(type, node_idx, type_id);
		SetValueAtIndex(content, node_idx, content_id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void SetValueAtIndex(ArrayList array_list, int index, Object value) {
		int size = array_list.size();
		if (index >= size) {
			int gap = index - size + 1;
			for (int i=0;i<gap;i++) {
				array_list.add(null);
			}
		}
		array_list.set(index, value);
	}
	
	public void StoreOracle(int node_idx, String type, String content) {
		SetValueAtIndex(type_oracle, node_idx, type);
		SetValueAtIndex(content_oracle, node_idx, content);
	}
	
	@Override
	public String toString() {
		String result = StringUtils.join(left_child.toArray(), " ") + " " + StringUtils.join(right_child.toArray(), " ") + " " + StringUtils.join(type.toArray(), " ") + " " + StringUtils.join(content.toArray(), " ");
		return result;
	}
	
	public String toDebugString() {
		String line_seperator = System.getProperty("line.separator");
		String result = StringUtils.join(left_child.toArray(), " ") + line_seperator + StringUtils.join(right_child.toArray(), " ") + line_seperator + StringUtils.join(type.toArray(), " ") + line_seperator + StringUtils.join(content.toArray(), " ") + line_seperator;
		return result;
	}
	
	public String toOracleString() {
		String line_seperator = System.getProperty("line.separator");
		String result = StringUtils.join(type_oracle.toArray(), " ") + line_seperator + StringUtils.join(content_oracle.toArray(), " ") + line_seperator;
		return result;
	}
	
}
