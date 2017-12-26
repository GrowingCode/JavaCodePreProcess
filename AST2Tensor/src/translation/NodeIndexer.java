package translation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

public class NodeIndexer {
	
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
