package tree;

import java.util.ArrayList;

public class Tree {
	
	TreeNode root = null;
	ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
	
	public Tree(TreeNode root) {
		this.root = root;
	}
	
	public void AddTreeNode(TreeNode node) {
		nodes.add(node);
	}
	
}
