package tree;

import org.eclipse.jdt.core.dom.IBinding;

public class MergedTreeNode extends TreeNode {
	
	TreeNode origin_base_parent = null;
	
	TreeNode base_parent = null;
	TreeNode merged_child = null;
	
	public MergedTreeNode(Class<?> clazz, IBinding bind, String content, String tree_whole_content) {
		super(clazz, bind, content, tree_whole_content);
	}
	
	public void SetUpMergedInformation(TreeNode base_parent, TreeNode merged_child) {
		this.base_parent = base_parent;
		this.merged_child = merged_child;
		
		if (base_parent instanceof MergedTreeNode) {
			origin_base_parent = ((MergedTreeNode) base_parent).origin_base_parent;
		} else {
			origin_base_parent = base_parent;
		}
	}
	
	public TreeNode GetOriginBaseParent() {
		return origin_base_parent;
	}
	
	public TreeNode GetMergeBaseParent() {
		return base_parent;
	}
	
	public TreeNode GetMergeChild() {
		return merged_child;
	}
	
}
