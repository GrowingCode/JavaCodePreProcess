package tree;

import org.eclipse.jdt.core.dom.IBinding;

public class MergedTreeNode extends TreeNode {
	
	TreeNode origin_base_parent = null;
	
	TreeNode base_parent = null;
	TreeNode merged_child = null;
	
	public MergedTreeNode(Class<?> clazz, boolean is_non_comp_leaf, IBinding bind, String content, String tree_whole_content, int node_count) {
		super(clazz, is_non_comp_leaf, bind, content, tree_whole_content);
		this.node_count = node_count;
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
