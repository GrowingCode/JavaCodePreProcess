package translation.tensor;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Assert;

public class Tensor {
	
	List<Integer> left_child = new LinkedList<Integer>();
	List<Integer> right_child = new LinkedList<Integer>();
	List<Integer> type = new LinkedList<Integer>();
	List<Integer> content = new LinkedList<Integer>();
	
	public Tensor() {
	}
	
	public void AppendOneASTNode(int node_id, int left_child_node_id, int right_child_node_id, int type_id, int content_id) {
		Assert.isLegal(left_child.size() == node_id);
		left_child.add(left_child_node_id);
		right_child.add(right_child_node_id);
		type.add(type_id);
		content.add(content_id);
	}
	
	@Override
	public String toString() {
		String result = StringUtils.join(left_child.toArray(), " ") + " " + StringUtils.join(right_child.toArray(), " ") + " " + StringUtils.join(type.toArray(), " ") + " " + StringUtils.join(content.toArray(), " ");
		return result;
	}
	
}
