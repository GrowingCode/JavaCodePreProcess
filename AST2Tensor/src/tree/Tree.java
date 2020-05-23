package tree;

import java.util.ArrayList;
import java.util.TreeMap;

public class Tree implements Comparable<Tree> {
	
	TreeNode root = null;
//	TreeMap<String, TreeNode> nodes = new TreeMap<String, TreeNode>();
	
	public Tree(TreeNode root) {
		this.root = root;
	}
	
//	public void AddTreeNode(TreeNode node) {
//		nodes.add(node);
//	}
	
//	public void AddTreeNodes(Collection<TreeNode> nds) {
//		for (TreeNode nd : nds) {
//			nodes.put(nd.GetContent(), nd);
//		}
//	}

	@Override
	public int compareTo(Tree o) {
		return root.GetTreeWholeContent().compareTo(o.root.GetTreeWholeContent());
	}
	
	public TreeNode GetRootNode() {
		return root;
	}
	
	public TreeMap<String, TreeNode> GetAllNodes() {
		TreeMap<String, TreeNode> nodes = new TreeMap<String, TreeNode>();
		GetAllNodes(root, nodes);
		return nodes;
	}
	
	private void GetAllNodes(TreeNode r_node, TreeMap<String, TreeNode> nodes) {
		nodes.put(r_node.GetContent(), r_node);
		ArrayList<TreeNode> childs = r_node.GetChildren();
		for (TreeNode child : childs) {
			GetAllNodes(child, nodes);
		}
	}
	
}
