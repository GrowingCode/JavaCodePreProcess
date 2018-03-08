package translation.tensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;

import statistic.id.IDManager;

public class TreeTensor extends Tensor {
	
	IDManager im = null;
	
	ArrayList<Integer> left_child = new ArrayList<Integer>();
	ArrayList<Integer> right_child = new ArrayList<Integer>();
	ArrayList<Integer> type = new ArrayList<Integer>();
	ArrayList<Integer> content = new ArrayList<Integer>();
	ArrayList<Integer> decode_type = new ArrayList<Integer>();
	
	ArrayList<String> type_oracle = new ArrayList<String>();
	ArrayList<String> content_oracle = new ArrayList<String>();
	
	public TreeTensor(int role, IDManager im) {
		super(role);
		this.im = im;
	}
	
	public void StoreOneASTNode(int node_idx, int left_child_node_idx, int right_child_node_idx, int type_id, int content_id, int decode_type_id) {
		SetValueAtIndex(left_child, node_idx, left_child_node_idx);
		SetValueAtIndex(right_child, node_idx, right_child_node_idx);
		SetValueAtIndex(type, node_idx, type_id);
		SetValueAtIndex(content, node_idx, content_id);
		SetValueAtIndex(decode_type, node_idx, decode_type_id);
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
	
	TreeMap<Integer, Integer> node_encode_index_to_decode_index_map = new TreeMap<Integer, Integer>();
	ArrayList<Integer> left = new ArrayList<Integer>();
	ArrayList<Integer> up = new ArrayList<Integer>();
	ArrayList<Integer> node_encode_index_in_decode_array = new ArrayList<Integer>();
	ArrayList<Integer> decode_type_node = new ArrayList<Integer>();
	
	public void GenerateTreeIterationSequence() {
		int encode_last_index = left_child.size()-1;
		GenerateTreeIterationSequence(encode_last_index, -1, -1);
	}
	
	private void GenerateTreeIterationSequence(int current_root, int left_of_root, int up_of_root) {
		if (current_root >= 0) {
			Integer lc = left_child.get(current_root);
			Integer rc = right_child.get(current_root);
			if (type.get(current_root) == im.GetTypeID(IDManager.VirtualChildrenConnectionNonLeafASTType)) {
				GenerateTreeIterationSequence(lc, left_of_root, up_of_root);
				GenerateTreeIterationSequence(rc, left_of_root, lc);
			} else {
				node_encode_index_to_decode_index_map.put(current_root, node_encode_index_in_decode_array.size());
				node_encode_index_in_decode_array.add(current_root);
				int left_of_root_index = -1;
				if (left_of_root >= 0) {
					left_of_root_index = node_encode_index_to_decode_index_map.get(left_of_root);
				}
				int up_of_root_index = -1;
				if (up_of_root >= 0) {
					up_of_root_index = node_encode_index_to_decode_index_map.get(up_of_root);
				}
				left.add(left_of_root_index);
				up.add(up_of_root_index);
				decode_type_node.add(decode_type.get(current_root));
				GenerateTreeIterationSequence(lc, current_root, -1);
				GenerateTreeIterationSequence(rc, current_root, lc);
			}
		}
	}
	
	@Override
	public String toString() {
		String result = StringUtils.join(left_child.toArray(), " ") + " " + StringUtils.join(right_child.toArray(), " ") + " " + StringUtils.join(type.toArray(), " ") + " " + StringUtils.join(content.toArray(), " ") + ", " + StringUtils.join(left.toArray(), " ") + " " + StringUtils.join(up.toArray(), " ") + " " + StringUtils.join(node_encode_index_in_decode_array.toArray(), " ") + " " + StringUtils.join(decode_type_node.toArray(), " ");
		return result.trim();
	}
	
	public String toDebugString() {
		String line_seperator = System.getProperty("line.separator");
		String result = StringUtils.join(left_child.toArray(), " ") + line_seperator + StringUtils.join(right_child.toArray(), " ") + line_seperator + StringUtils.join(type.toArray(), " ") + line_seperator + StringUtils.join(content.toArray(), " ") + line_seperator + StringUtils.join(left.toArray(), " ") + line_seperator + StringUtils.join(up.toArray(), " ") + line_seperator + StringUtils.join(node_encode_index_in_decode_array.toArray(), " ");
		return result;
	}
	
	public String toOracleString() {
		String line_seperator = System.getProperty("line.separator");
		String left_oracle = IDToTypeContent("Encode's Every node's left_child:", left_child);
		String right_oracle = IDToTypeContent("Encode's Every node's right_child:", right_child);
		String node_oracle = IDToTypeContent("Decode's Every node:", node_encode_index_in_decode_array);
		String ind_left_oracle = IndirectIDToTypeContent("Decode's Every node's left", left);
		String ind_up_oracle = IndirectIDToTypeContent("Decode's Every node's up", up);
		String result = StringUtils.join(type_oracle.toArray(), " ") + line_seperator + StringUtils.join(content_oracle.toArray(), " ") + line_seperator + left_oracle + line_seperator + right_oracle + line_seperator + node_oracle + line_seperator + ind_left_oracle + line_seperator + ind_up_oracle ;
		return result;
	}
	
	public String IndirectIDToTypeContent(String description, ArrayList<Integer> idxs) {
		String line_seperator = System.getProperty("line.separator");
		ArrayList<String> tp = new ArrayList<String>();
		ArrayList<String> cnt = new ArrayList<String>();
		Iterator<Integer> itr = idxs.iterator();
		while (itr.hasNext()) {
			Integer ii = itr.next();
			if (ii >= 0) {
				Integer iidx = node_encode_index_in_decode_array.get(ii);
				tp.add(type_oracle.get(iidx));
				cnt.add(content_oracle.get(iidx));
			} else {
				tp.add("-1");
				cnt.add("-1");
			}
		}
		return description + "#type:" + StringUtils.join(tp.toArray(), " ") + line_seperator + description + "#content:" + StringUtils.join(cnt.toArray(), " ");
	}
	
	public String IDToTypeContent(String description, ArrayList<Integer> indexs) {
		String line_seperator = System.getProperty("line.separator");
		ArrayList<String> tp = new ArrayList<String>();
		ArrayList<String> cnt = new ArrayList<String>();
		Iterator<Integer> iitr = indexs.iterator();
		while (iitr.hasNext()) {
			Integer iidx = iitr.next();
			if (iidx >= 0) {
				tp.add(type_oracle.get(iidx));
				cnt.add(content_oracle.get(iidx));
			} else {
				tp.add("-1");
				cnt.add("-1");
			}
		}
		return description + "#type:" + StringUtils.join(tp.toArray(), " ") + line_seperator + description + "#content:" + StringUtils.join(cnt.toArray(), " ");
	}
	
	private Map<ASTNode, Integer> node_idx = new HashMap<ASTNode, Integer>();
	private int current_max_idx = -1;
	
	public int GetNewIndex() {
		current_max_idx++;
		return current_max_idx;
	}

	public int GetASTNodeIndex(ASTNode node) {
		Integer idx = node_idx.get(node);
		if (idx == null) {
			current_max_idx++;
			idx = current_max_idx;
			node_idx.put(node, idx);
		}
		return idx;
	}
	
}
