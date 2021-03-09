package bpe.skt;

import tree.TreeNode;

public class ExactTreeNodeTwoMerge {
	
	public TreeNode parent;
	public TreeNode node;
	public String merged_cnt;
	
	public ExactTreeNodeTwoMerge(TreeNode parent, TreeNode node, String merged_cnt) {
		this.parent = parent;
		this.node = node;
		this.merged_cnt = merged_cnt;
	}
	
}
