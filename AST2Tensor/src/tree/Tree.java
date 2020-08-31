package tree;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;

import eclipse.jdt.JDTASTHelper;
import main.MetaOfApp;
import translation.tensor.util.TokenKindUtil;
import util.YStringUtil;

public class Tree implements Comparable<Tree> {

	TreeFlatten tf = new TreeFlatten();
	TreeNode root = null;
//	TreeMap<String, TreeNode> nodes = new TreeMap<String, TreeNode>();

	public Tree(TreeNode root) {
		this.root = root;
	}

//	public void AddTreeNode(TreeNode node) {
//		nodes.add(node);
//	}

//	public void AddTreeNodes(Collection<TreeNode> nds) {
//		for (TreeNode nd : nds) {
//			nodes.put(nd.GetContent(), nd);
//		}
//	}

	@Override
	public int compareTo(Tree o) {
		return root.GetTreeWholeContent().compareTo(o.root.GetTreeWholeContent());
	}

	public TreeNode GetRootNode() {
		return root;
	}

	public ArrayList<TreeNode> GetAllNodes() {
//		TreeMap<String, TreeNode> nodes = new TreeMap<String, TreeNode>();
		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
		GetAllNodes(root, nodes);
		return nodes;
	}

	private void GetAllNodes(TreeNode r_node, ArrayList<TreeNode> nodes) {
//		nodes.put(r_node.GetContent(), r_node);
		nodes.add(r_node);
		ArrayList<TreeNode> childs = r_node.GetChildren();
		for (TreeNode child : childs) {
			GetAllNodes(child, nodes);
		}
	}
	
	public void FlattenOriginTree() {// TreeMap<String, ArrayList<String>> token_composes
		if (tf.skt_e_struct_token.size() == 0) {
			FlattenOriginTreeNode(tf, root);
		}
	}

	public void FlattenTree() {// TreeMap<String, ArrayList<String>> token_composes
		if (tf.skt_one_struct.size() == 0) {
//			Assert.isTrue(tf == null);
			tf.skt_one_struct_v_count.add(0);
			FlattenTreeNode(tf, root);// , token_composes
			FlattenTreeNodeIntoOne(tf, root);
			Assert.isTrue(tf.skt_one_struct.size() == 0);
			tf.skt_one_struct.add(root.GetContent());
		}
//		return tf;
	}
	
	public TreeFlatten GetTreeFlattenResult() {
		Assert.isTrue(tf.skt_one_struct.size() > 0 && tf.skt_e_struct_token.size() > 0);
		return tf;
	}

//	public TreeFlatten ReFlattenTree(TreeMap<String, ArrayList<String>> token_composes) {
//		tf = new TreeFlatten();
//		FlattenTreeNode(tf, root, token_composes);
//		FlattenTreeNodeIntoOne(tf, root);
//		return tf;
//	}
	
	private static void FlattenOriginTreeNode(TreeFlatten tf, TreeNode rt) {// , TreeMap<String, ArrayList<String>> token_composes
		tf.skt_e_struct_token.add(rt.GetContent());
		tf.skt_e_struct_token_tree_uid.add(rt.GetTreeUid());
		tf.skt_e_struct_token_hv_count.add(YStringUtil.CountSubStringInString(rt.GetContent(), "#h") + YStringUtil.CountSubStringInString(rt.GetContent(), "#v"));
		ArrayList<TreeNode> childs = rt.GetChildren();
		for (TreeNode child : childs) {
			FlattenOriginTreeNode(tf, child);
		}
	}

	private static void FlattenTreeNodeIntoOne(TreeFlatten tf, TreeNode rt) {
		ArrayList<TreeNode> childs = rt.GetChildren();
		if (rt.GetContent().equals("{}")) {
//			System.err.println("rt {} children size:" + childs.size());
			Assert.isTrue(childs.size() == 0);
		}
		Class<?> clz = rt.GetClazz();
		if (JDTASTHelper.IsIDLeafNode(clz)) {
			tf.skt_one_struct_v_count.set(0, tf.skt_one_struct_v_count.get(0) + 1);
			tf.skt_one_struct_v_tree_uid.add(rt.GetTreeUid());
		} else {
			int i_len = childs.size();
			for (int i = i_len-1; i >= 0; i--) {
				TreeNode child = childs.get(i);
				FlattenTreeNodeIntoOne(tf, child);
				if (!JDTASTHelper.IsIDLeafNode(child.GetClazz())) {
//					Assert.isTrue(child.GetChildren().size() > 0, "wrong content:" + rt.GetContent() + "#wrong type:" + child.GetClazz());
					Assert.isTrue(!rt.GetContent().equals("{}"));
//					System.err.println("==== before merge:" + rt.GetContent() + " ====");
					String mgd = YStringUtil.ReplaceSpecifiedContentInSpecifiedPosition(rt.GetContent(), child.GetContent(), i);
//					System.err.println("==== after merge:" + mgd + " ====");
					rt.SetContent(mgd);
					childs.remove(i);
				}
			}
		}
//		if (tf.skt_one_struct.size() == 0) {
//			tf.skt_one_struct.add(rt.GetContent());
//		} else {
//			tf.skt_one_struct.set(0, tf.skt_one_struct.get(0) + "#" + rt.GetContent());
//		}
	}
	
	private static void FlattenTreeNode(TreeFlatten tf, TreeNode rt) {// , TreeMap<String, ArrayList<String>> token_composes
		ArrayList<TreeNode> childs = rt.GetChildren();
		Class<?> clz = rt.GetClazz();
		if (JDTASTHelper.IsIDLeafNode(clz)) {
			Assert.isTrue(childs.size() == 0);
			tf.skt_token.add(rt.GetContent());
			int tk = TokenKindUtil.GetTokenKind(rt);
			tf.skt_token_kind.add(tk);
			int token_is_var = rt.GetBinding() != null ? 1 : 0;
			if (MetaOfApp.UseApproximateVariable) {
				int base = 1;
				if (MetaOfApp.FurtherUseStrictVariable) {
					base = token_is_var;
				}
				token_is_var = base * (TokenKindUtil.IsApproximateVar(tk) ? 1 : 0);
			}
			tf.skt_token_is_var.add(token_is_var);
		} else {
			tf.skt_pe_struct.add(rt.GetContent());
			tf.skt_pe_e_struct.add(new ArrayList<String>());
			tf.skt_pe_e_struct_tree_uid.add(new ArrayList<String>());
			if (rt instanceof MergedTreeNode) {
				FlattenMergedTreeNode(tf, (MergedTreeNode) rt, rt.GetTreeUid());
			} else {
				int es_size = tf.skt_pe_e_struct.size();
				int es_tu_size = tf.skt_pe_e_struct_tree_uid.size();
				Assert.isTrue(es_size == es_tu_size);
				tf.skt_pe_e_struct.get(es_size-1).add(rt.GetContent());
				tf.skt_pe_e_struct_tree_uid.get(es_tu_size-1).add("");
				tf.skt_one_e_struct.add(rt.GetContent());
				tf.skt_one_e_struct_tree_uid.add(rt.GetTreeUid());
			}
			
			int h_count = YStringUtil.CountSubStringInString(rt.GetContent(), "#h");
			int v_count = YStringUtil.CountSubStringInString(rt.GetContent(), "#v");
			tf.skt_pe_struct_h_count.add(h_count);
			tf.skt_pe_struct_v_count.add(v_count);
			Assert.isTrue(h_count + v_count == childs.size());
			int r_h_count = 0;
			int r_v_count = 0;
//			 skt_pe_struct_v_tree_uid
			ArrayList<String> h_tids = new ArrayList<String>();
			tf.skt_pe_struct_h_tree_uid.add(h_tids);
			ArrayList<String> v_tids = new ArrayList<String>();
			tf.skt_pe_struct_v_tree_uid.add(v_tids);
			for (TreeNode child : childs) {
				String r_cid = ExtractRelativeTreeUid(child.GetTreeUid(), rt.GetTreeUid());
				if (JDTASTHelper.IsIDLeafNode(child.GetClazz())) {
					v_tids.add(r_cid);
					r_v_count++;
				} else {
					h_tids.add(r_cid);
					r_h_count++;
				}
			}
			Assert.isTrue(r_h_count == h_count);
			Assert.isTrue(r_v_count == v_count);
			
			for (TreeNode child : childs) {
				FlattenTreeNode(tf, child);// , token_composes
			}
		}
	}
	
	private static void FlattenMergedTreeNode(TreeFlatten tf, MergedTreeNode mtn, final String tree_uid) {
		TreeNode m_base_parent = mtn.GetMergeBaseParent();
		if (m_base_parent instanceof MergedTreeNode) {
			FlattenMergedTreeNode(tf, (MergedTreeNode) m_base_parent, tree_uid);
		} else {
			HandleTreeNodeFlatten(tf, m_base_parent, tree_uid);
		}
		
		TreeNode m_child = mtn.GetMergeChild();
		if (m_child instanceof MergedTreeNode) {
			FlattenMergedTreeNode(tf, (MergedTreeNode) m_child, tree_uid);
		} else {
			HandleTreeNodeFlatten(tf, m_child, tree_uid);
		}
	}
	
	private static void HandleTreeNodeFlatten(TreeFlatten tf, TreeNode m_child, final String tree_uid) {
		if (m_child instanceof MergedTreeNode) {
			return;
		}
		String m_child_tree_uid = m_child.GetTreeUid();
		String m_child_relative_tree_uid = ExtractRelativeTreeUid(m_child_tree_uid, tree_uid);
		int es_size = tf.skt_pe_e_struct.size();
		int es_tu_size = tf.skt_pe_e_struct_tree_uid.size();
		Assert.isTrue(es_size == es_tu_size);
		tf.skt_pe_e_struct.get(es_size-1).add(m_child.GetContent());
		tf.skt_pe_e_struct_tree_uid.get(es_tu_size-1).add(m_child_relative_tree_uid);
		tf.skt_one_e_struct.add(m_child.GetContent());
		tf.skt_one_e_struct_tree_uid.add(m_child.GetTreeUid());
	}
	
	private static String ExtractRelativeTreeUid(String child_tree_uid, String ancestor_tree_uid) {
		String pfx = ancestor_tree_uid + " ";
		Assert.isTrue(child_tree_uid.startsWith(pfx));
		return child_tree_uid.substring(pfx.length(), child_tree_uid.length());
	}
	
	public void DebugPrintEachNode() {
		System.out.println("==== Debug Tree Each Node Begin ====");
		DebugPrintEachNode(root);
		System.out.println("==== Debug Tree Each Node End ====");
//		return "Debugging!";
	}
	
	private void DebugPrintEachNode(TreeNode rt) {
		System.out.println(rt.GetContent());
		ArrayList<TreeNode> childs = rt.GetChildren();
		for (TreeNode child : childs) {
			DebugPrintEachNode(child);
		}
	}

}
