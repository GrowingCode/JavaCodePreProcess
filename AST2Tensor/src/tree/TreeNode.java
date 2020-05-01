package tree;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.IBinding;

public class TreeNode {
	
	Class<?> clazz = null;
	IBinding bind = null;
	String content = null;
	String tree_whole_content = null;
	TreeNode parent = null;
	ArrayList<TreeNode> children = new ArrayList<TreeNode>();
	
	public TreeNode(Class<?> clazz, IBinding bind, String content, String tree_whole_content) {
		this.clazz = clazz;
		this.bind = bind;
		this.content = content;
		this.tree_whole_content = tree_whole_content;
	}
	
	public void AppendToChildren(TreeNode tn) {
		children.add(tn);
		if (tn.parent == null) {
			tn.parent = this;
		} else {
			Assert.isTrue(tn.parent == this);
		}
	}
	
	public Class<?> GetClazz() {
		return clazz;
	}
	
	public IBinding GetBinding() {
		return bind;
	}
	
	public String GetContent() {
		return content;
	}
	
	public String GetTreeWholeContent() {
		return tree_whole_content;
	}
	
	public ArrayList<TreeNode> GetChildren() {
		return children;
	}
	
	public TreeNode GetParent() {
		return parent;
	}
	
}
