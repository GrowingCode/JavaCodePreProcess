package tree;

import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

import eclipse.jdt.JDTASTHelper;
import main.MetaOfApp;
import translation.tensor.util.TokenKindUtil;
import util.YStringUtil;

public class Tree implements Comparable<Tree> {

	TreeFlatten tf = null;
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

	public TreeFlatten FlattenTree() {// TreeMap<String, ArrayList<String>> token_composes
		if (tf == null) {
//			Assert.isTrue(tf == null);
			tf = new TreeFlatten();
			FlattenTreeNode(tf, root);// , token_composes
			FlattenTreeNodeIntoOne(tf, root);
			Assert.isTrue(tf.skt_one_struct.size() == 0);
			tf.skt_one_struct.add(root.GetContent());
		}
		return tf;
	}

//	public TreeFlatten ReFlattenTree(TreeMap<String, ArrayList<String>> token_composes) {
//		tf = new TreeFlatten();
//		FlattenTreeNode(tf, root, token_composes);
//		FlattenTreeNodeIntoOne(tf, root);
//		return tf;
//	}

	private static void FlattenTreeNodeIntoOne(TreeFlatten tf, TreeNode rt) {
		ArrayList<TreeNode> childs = rt.GetChildren();
		if (rt.GetContent().equals("{}")) {
//			System.err.println("rt {} children size:" + childs.size());
			Assert.isTrue(childs.size() == 0);
		}
		Class<?> clz = rt.GetClazz();
		if (JDTASTHelper.IsIDLeafNode(clz)) {
			// do nothing.
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
//			ArrayList<String> composes = token_composes.get(rt.GetContent());
			if (rt instanceof MergedTreeNode) {
//				tf.skt_e_struct.addAll(composes);
				FlattenMergedTreeNode(tf, (MergedTreeNode) rt, rt.GetTreeUid());
			} else {
				tf.skt_e_struct.add(rt.GetContent());
				
			}
			for (TreeNode child : childs) {
				FlattenTreeNode(tf, child);// , token_composes
			}
		}
	}
	
	private static void FlattenMergedTreeNode(TreeFlatten tf, MergedTreeNode mtn, final String tree_uid) {
		TreeNode m_base_parent = mtn.GetMergeBaseParent();
		TreeNode m_child = mtn.GetMergeChild();
		String m_child_tree_uid = m_child.GetTreeUid();
		String pfx = tree_uid + " ";
		Assert.isTrue(m_child_tree_uid.startsWith(pfx));
		String m_child_relative_tree_uid = m_child_tree_uid.substring(pfx.length(), m_child_tree_uid.length());
		tf.skt_e_struct.add();
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
