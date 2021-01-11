package tree;

import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.IBinding;

import tree.util.TreeNodePairUtil;
import unit.PairContainer;

public class TreeNode {
	
	Class<?> clazz = null;
	IBinding bind = null;
	String content = null;
	String tree_whole_content = null;
//	int sib_index = -1;
	TreeNode parent = null;
	ArrayList<TreeNode> children = new ArrayList<TreeNode>();
	
	String tree_uid = null;
	
	public TreeNode(Class<?> clazz, IBinding bind, String content, String tree_whole_content) {//, int sib_index
		this.clazz = clazz;
		this.bind = bind;
		this.content = content;
		this.tree_whole_content = tree_whole_content;
//		this.sib_index = sib_index;
	}
	
	public void AppendToChildren(TreeNode tn) {
		children.add(tn);
		if (tn.parent == null) {
			tn.parent = this;
		} else {
			Assert.isTrue(tn.parent == this);
		}
	}
	
	public void AppendAllChildren(ArrayList<TreeNode> children) {
		this.children.addAll(children);
	}
	
	public Class<?> GetClazz() {
		return clazz;
	}
	
	public IBinding GetBinding() {
		return bind;
	}
	
	public void SetContent(String content) {
		this.content = content;
	}
	
	public String GetContent() {
		return content;
	}
	
	public String GetTreeWholeContent() {
		return tree_whole_content;
	}
	
	@Override
	public String toString() {
		return content + "#" + hashCode();
	}
	
//	public int GetSiblingIndex() {
//		return sib_index;
//	}
	
	public ArrayList<TreeNode> GetChildren() {
		return children;
	}
	
	public TreeNode GetParent() {
		return parent;
	}
	
	public void SetParent(TreeNode parent) {
		this.parent = parent ;
	}
	
	public void SetTreeUid(String tree_uid) {
		this.tree_uid = tree_uid;
	}
	
	public String GetTreeUid() {
		return tree_uid;
	}
	
	public void PreProcessTreeNode(String t_path, TreeMap<String, ArrayList<PairContainer<TreeNode, TreeNode>>> parent_child_node_pairs) {
		Assert.isTrue(this.GetTreeUid() == null);
//		Assert.isTrue(!this.GetContent().equals("#h&&#h&&#h"), "strange node clazz:" + GetClazz() + "#node whole content:" + GetTreeWholeContent());
		this.SetTreeUid(t_path);
		ArrayList<TreeNode> childs = this.GetChildren();
		int sib_index = -1;
		for (TreeNode child : childs) {
			sib_index++;
			String pp = TreeNodePairUtil.GetParentChildPairPresentation(this.GetContent(), child.GetContent());
			
			ArrayList<PairContainer<TreeNode, TreeNode>> pp_ll = parent_child_node_pairs.get(pp);
			if (pp_ll == null) {
				pp_ll = new ArrayList<PairContainer<TreeNode, TreeNode>>();
				parent_child_node_pairs.put(pp, pp_ll);
			}
			pp_ll.add(new PairContainer<TreeNode, TreeNode>(this, child));
			
			child.PreProcessTreeNode(t_path + " " + sib_index, parent_child_node_pairs);
		}
	}
	
}
