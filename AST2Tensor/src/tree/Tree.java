package tree;

import java.util.ArrayList;
import java.util.Collection;

public class Tree {
	
	TreeNode root = null;
	ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
	
	public Tree(TreeNode root) {
		this.root = root;
	}
	
	public void AddTreeNode(TreeNode node) {
		nodes.add(node);
	}
	
	public void AddTreeNodes(Collection<TreeNode> nds) {
		nodes.addAll(nds);
	}
	
}
