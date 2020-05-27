package translation.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.Assert;

import statistic.id.GrammarRecorder;
import statistic.id.IDManager;
import translation.tensor.StringTensor;
import translation.tensor.TensorInfo;
import translation.tensor.TreeTensor;
import tree.TreeNode;
import tree.TreeVisitor;

public class TreeTensorGenerator extends TreeVisitor {
	
	public TreeTensorGenerator(IDManager im) {
		super(im);
		curr_tensor.StorePostOrderNodeInfo(0, new ArrayList<Integer>());
	}

	TreeTensor curr_tensor = null;
	
	Map<TreeNode, Integer> node_post_order_index = new HashMap<TreeNode, Integer>();
	
	@Override
	public boolean PreVisit(TreeNode node) {
		int type_content_id = im.GetTypeContentID(node.GetContent());
		ArrayList<TreeNode> children_nodes = node.GetChildren();
		if (children_nodes != null && children_nodes.size() > 0) {
			curr_tensor.StorePrePostOrderNodeInfo(type_content_id, 0, -1, im.GetGrammarID(GrammarRecorder.GetGrammar(node)));
		}
		return true;
	}

	@Override
	public void PostVisit(TreeNode node) {
		int type_content_id = im.GetTypeContentID(node.GetContent());
		ArrayList<TreeNode> children_nodes = node.GetChildren();
		ArrayList<Integer> children_index = new ArrayList<Integer>();
		boolean has_children = children_nodes != null && children_nodes.size() > 0;
		if (has_children) {
			Iterator<TreeNode> n_itr = children_nodes.iterator();
			while (n_itr.hasNext()) {
				TreeNode n = n_itr.next();
				Integer c_idx = node_post_order_index.get(n);
				Assert.isTrue(c_idx != null);
				children_index.add(c_idx);
			}
		}
		int post_order_index = curr_tensor.StorePostOrderNodeInfo(type_content_id, children_index);
		Assert.isTrue(post_order_index == node_post_order_index.size()+1);
		node_post_order_index.put(node, post_order_index);
		curr_tensor.StorePrePostOrderNodeInfo(type_content_id, has_children ? 2 : 1, post_order_index, im.GetGrammarID(GrammarRecorder.GetGrammar(node)));
	}

	@Override
	public StringTensor GetStringTensor() {
		StringTensor st = new StringTensor(curr_tensor.GetTensorInfo());
		curr_tensor.Validate();
		st.SetToString(curr_tensor.toString());
		st.SetToDebugString(curr_tensor.toDebugString());
		st.SetToOracleString(curr_tensor.toOracleString());
		st.SetSize(curr_tensor.getSize());
		return st;
	}

	@Override
	public void ClearAndInitialize(TensorInfo ti) {
		curr_tensor = new TreeTensor(ti);
		node_post_order_index.clear();
	}

}
