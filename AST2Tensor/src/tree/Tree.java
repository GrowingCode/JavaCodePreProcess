package tree;

import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

import eclipse.jdt.JDTASTHelper;
import main.MetaOfApp;
import translation.tensor.util.TokenKindUtil;

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
	
	public TreeMap<String, TreeNode> GetAllNodes() {
		TreeMap<String, TreeNode> nodes = new TreeMap<String, TreeNode>();
		GetAllNodes(root, nodes);
		return nodes;
	}
	
	private void GetAllNodes(TreeNode r_node, TreeMap<String, TreeNode> nodes) {
		nodes.put(r_node.GetContent(), r_node);
		ArrayList<TreeNode> childs = r_node.GetChildren();
		for (TreeNode child : childs) {
			GetAllNodes(child, nodes);
		}
	}
	
	public TreeFlatten FlattenTree(TreeMap<String, ArrayList<String>> token_composes) {
		if (tf == null) {
			tf = new TreeFlatten();
			FlattenTreeNode(tf, root, token_composes);
		}
		return tf;
	}
	
	public TreeFlatten ReFlattenTree(TreeMap<String, ArrayList<String>> token_composes) {
		tf = new TreeFlatten();
		FlattenTreeNode(tf, root, token_composes);
		return tf;
	}
	
	private static void FlattenTreeNode(TreeFlatten tf, TreeNode rt, TreeMap<String, ArrayList<String>> token_composes) {
		ArrayList<TreeNode> childs = rt.GetChildren();
		Class<?> clz = rt.GetClazz();
		if (JDTASTHelper.IsIDLeafNode(clz)) {
			Assert.isTrue(childs.size() == 0);
			tf.skt_token.add(rt.GetContent());
			int tk = TokenKindUtil.GetTokenKind(rt);
			tf.skt_token_kind.add(tk);
			
			int token_is_var = rt.GetBinding()!= null ? 1 : 0;
			if (MetaOfApp.UseApproximateVariable) {
				int base = 1;
				if (MetaOfApp.FurtherUseStrictVariable) {
					base = token_is_var;
				}
				token_is_var = base * (TokenKindUtil.IsApproximateVar(tk) ? 1 : 0);
			}
			tf.skt_token_is_var.add(token_is_var);
		} else {
			if (tf.skt_one_struct.size() == 0) {
				tf.skt_one_struct.add(rt.GetContent());
			} else {
				tf.skt_one_struct.set(0, tf.skt_one_struct.get(0) + "#" + rt.GetContent());
			}
			tf.skt_pe_struct.add(rt.GetContent());
			ArrayList<String> composes = token_composes.get(rt.GetContent());
			if (composes == null) {
				tf.skt_e_struct.add(rt.GetContent());
			} else {
				tf.skt_e_struct.addAll(composes);
			}
			for (TreeNode child : childs) {
				FlattenTreeNode(tf, child, token_composes);
			}
		}
	}
	
}
