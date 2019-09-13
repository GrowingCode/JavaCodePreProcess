package tree;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;

public class TreeNode {
	
	String content = null;
	TreeNode parent = null;
	ArrayList<TreeNode> children = new ArrayList<TreeNode>();
	
	public TreeNode(String content) {
		this.content = content;
	}
	
	public void AppendToChildren(TreeNode tn) {
		children.add(tn);
		if (tn.parent == null) {
			tn.parent = this;
		} else {
			Assert.isTrue(tn.parent == this);
		}
	}
	
}
