package tree;

import java.util.Collection;
import java.util.TreeMap;

public class Tree implements Comparable<Tree> {
	
	TreeNode root = null;
	TreeMap<String, TreeNode> nodes = new TreeMap<String, TreeNode>();
	
	public Tree(TreeNode root) {
		this.root = root;
	}
	
//	public void AddTreeNode(TreeNode node) {
//		nodes.add(node);
//	}
	
	public void AddTreeNodes(Collection<TreeNode> nds) {
		for (TreeNode nd : nds) {
			nodes.put(nd.GetContent(), nd);
		}
	}

	@Override
	public int compareTo(Tree o) {
		return root.GetTreeWholeContent().compareTo(o.root.GetTreeWholeContent());
	}
	
	public TreeNode GetRootNode() {
		return root;
	}
	
	public TreeMap<String, TreeNode> GetAllNodes() {
		return nodes;
	}
	
}
