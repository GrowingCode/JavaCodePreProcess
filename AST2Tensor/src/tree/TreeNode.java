package tree;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.IBinding;

import main.MetaOfApp;
import statistic.id.IDManager;
import tree.util.TreeNodePairUtil;
import unit.PairContainer;
import util.YStringUtil;

public class TreeNode {

	Class<?> clazz = null;
	boolean ori_is_non_comp_leaf = false;
	IBinding bind = null;
	String content = null;
	String tree_whole_content = null;
//	int sib_index = -1;
	TreeNode parent = null;
	ArrayList<TreeNode> children = new ArrayList<TreeNode>();

	String tree_uid = null;

	public ArrayList<TreeNodeParentInfo> skt_e_par_info = new ArrayList<TreeNodeParentInfo>();
	public ArrayList<TreeNodeParentInfo> skt_pe_par_info = new ArrayList<TreeNodeParentInfo>();
	public ArrayList<TreeNodeParentInfo> skt_one_par_info = new ArrayList<TreeNodeParentInfo>();

	int node_count = 1;

	public TreeNode(Class<?> clazz, boolean ori_is_non_comp_leaf, IBinding bind, String content,
			String tree_whole_content) {// , int sib_index
		this.clazz = clazz;
		this.ori_is_non_comp_leaf = ori_is_non_comp_leaf;
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

	public boolean OriginIsNonCompositeLeaf() {
		return ori_is_non_comp_leaf;
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
		return content;// + "#" + hashCode()
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
		this.parent = parent;
	}

	public void SetTreeUid(String tree_uid) {
		this.tree_uid = tree_uid;
	}

	public String GetTreeUid() {
		return tree_uid;
	}

	public int GetNodeCount() {
		return node_count;
	}

	public void PreProcessTreeNode(String t_path,
			TreeMap<String, ArrayList<PairContainer<TreeNode, TreeNode>>> parent_child_node_pairs) {
		Assert.isTrue(this.GetTreeUid() == null);
		Assert.isTrue(!this.GetContent().contains("\n"));
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

//	public void EnsureLeafNode() {
//		if (ori_is_non_comp_leaf) {
//			Assert.isTrue(children.size() == 0);
//		}
//		if (children.size() == 0) {
//			if (!ori_is_non_comp_leaf) {
//				Assert.isTrue(clazz.equals(Block.class) || clazz.equals(AnonymousClassDeclaration.class), "Strange block:" + clazz);
//				ori_is_non_comp_leaf = true;
//			}
//		}
//		ArrayList<TreeNode> childs = this.GetChildren();
//		for (TreeNode child : childs) {
//			child.EnsureLeafNode();
//		}
//	}

	public void SetUpParentInfoOfChildren(String par_info) {
		try {
			Field field = this.getClass().getField(par_info);
			@SuppressWarnings("unchecked")
			ArrayList<TreeNodeParentInfo> this_p_info = (ArrayList<TreeNodeParentInfo>) field.get(this);
			if (this_p_info.size() == 0) {
				Assert.isTrue(parent == null);
				for (int i = 0; i < MetaOfApp.ParentInfoLength; i++) {
					this_p_info.add(new TreeNodeParentInfo(IDManager.ZDft, 0, 0));
				}
			} else {
				Assert.isTrue(parent != null);
				Assert.isTrue(this_p_info.size() == MetaOfApp.ParentInfoLength);
			}

			int csize = children.size();
			ArrayList<Integer> in_indexes = new ArrayList<Integer>();
			ArrayList<Integer> types = new ArrayList<Integer>();

			ArrayList<String> hv_list = YStringUtil.GenerateArrayListOfHV(content);

			int h = 0;
			int v = 0;
			for (int i = 0; i < csize; i++) {
				String hv = hv_list.get(i);
				if (hv.equals("#h")) {
					types.add(TreeNodeParentInfo.h_type);
					in_indexes.add(h);
					h++;
				}
				if (hv.equals("#v")) {
					types.add(TreeNodeParentInfo.v_type);
					in_indexes.add(v);
					v++;
				}
			}

			for (int i = 0; i < csize; i++) {
				SetUpParentInfoOfChildren(par_info, children.get(i), in_indexes.get(i), types.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void SetUpParentInfoOfChildren(String par_info, TreeNode child_tn, int in_index, int type) {
		try {
			Field field = this.getClass().getField(par_info);
			@SuppressWarnings("unchecked")
			ArrayList<TreeNodeParentInfo> this_p_info = (ArrayList<TreeNodeParentInfo>) field.get(this);
			@SuppressWarnings("unchecked")
			ArrayList<TreeNodeParentInfo> child_p_info = (ArrayList<TreeNodeParentInfo>) field.get(child_tn);
			Assert.isTrue(child_p_info.size() == 0);

			Assert.isTrue(this_p_info.size() > 0);
			child_p_info.addAll(this_p_info);
			child_p_info.remove(child_p_info.size() - 1);
			child_p_info.add(0, new TreeNodeParentInfo(content, in_index, type));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
