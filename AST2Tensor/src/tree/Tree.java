package tree;

import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

import bpe.skt.TreeNodeTwoMerge;
import eclipse.jdt.JDTASTHelper;
import translation.tensor.util.TokenKindUtil;
import tree.util.TreeNodePairUtil;
import unit.PairContainer;
import unit.util.PairContainerUtil;
import util.PrintUtil;
import util.YStringUtil;

public class Tree implements Comparable<Tree> {
	
	TreeFlatten tf = new TreeFlatten();
	TreeNode root = null;
//	TreeMap<String, TreeNode> nodes = new TreeMap<String, TreeNode>();
	TreeMap<String, ArrayList<PairContainer<TreeNode, TreeNode>>> parent_child_node_pairs = null;
	
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
	
	public void SetRootNode(TreeNode rt_node) {
		this.root = rt_node;
	}

	public TreeNode GetRootNode() {
		return root;
	}
	
//	public TreeMap<String, ArrayList<TreeNode>> GetAllContentNodeMap() {
//		TreeMap<String, ArrayList<TreeNode>> map = new TreeMap<String, ArrayList<TreeNode>>();
//		ArrayList<TreeNode> nodes = GetAllNodes();
//		for (TreeNode node : nodes) {
//			ArrayList<TreeNode> inners = map.get(node.GetContent());
//			if (inners == null) {
//				inners = new ArrayList<TreeNode>();
//				map.put(node.GetContent(), inners);
//			}
//			inners.add(node);
//		}
//		return map;
//	}
	
	public ArrayList<TreeNode> GetAllNodes() {
//		TreeMap<String, TreeNode> nodes = new TreeMap<String, TreeNode>();
		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
		GetAllNodes(root, nodes);
		return nodes;
	}
	
	public TreeMap<String, ArrayList<PairContainer<TreeNode, TreeNode>>> GetParentChildNodePairs() {
		return parent_child_node_pairs;
	}

	private void GetAllNodes(TreeNode r_node, ArrayList<TreeNode> nodes) {
//		nodes.put(r_node.GetContent(), r_node);
		nodes.add(r_node);
		ArrayList<TreeNode> childs = r_node.GetChildren();
		for (TreeNode child : childs) {
			GetAllNodes(child, nodes);
		}
	}
	
//	public void FlattenOriginTree() {// TreeMap<String, ArrayList<String>> token_composes
//		if (tf.skt_e_struct_token.size() == 0) {
//			FlattenOriginTreeNode(tf, root);
//		}
//	}

	public void FlattenTree(boolean flatten_one) {// TreeMap<String, ArrayList<String>> token_composes
//		if (tf.skt_one_struct.size() == 0) {
		if (tf.skt_one_struct.isEmpty()) {
//			Assert.isTrue(tf == null);
//			tf.skt_one_struct_v_count.add(0);
			FlattenTreeNode(tf, root);// , token_composes
			if (flatten_one) {
				FlattenTreeNodeIntoOne(tf, root);
			}
//			Assert.isTrue(tf.skt_one_struct.size() == 0);
			tf.skt_one_struct.add(root.GetContent());
//			tf.skt_one_struct = root.GetContent();
		}
//		return tf;
	}
	
	public TreeFlatten GetTreeFlattenResult() {
//		Assert.isTrue(tf.skt_one_struct != null && tf.skt_e_struct_token.size() > 0);
		Assert.isTrue(tf.skt_one_struct.size() > 0);
		return tf;
	}
	
	public void Accept(TreeVisitor tv) {
		RecursiveAccept(tv, root);
	}
	
	private void RecursiveAccept(TreeVisitor tv, TreeNode t_r) {
		boolean ctn = tv.PreVisit(t_r);
		if (ctn) {
			ArrayList<TreeNode> childs = t_r.GetChildren();
			for (TreeNode child : childs) {
				RecursiveAccept(tv, child);
			}
		}
		tv.PostVisit(t_r);
	}

//	public TreeFlatten ReFlattenTree(TreeMap<String, ArrayList<String>> token_composes) {
//		tf = new TreeFlatten();
//		FlattenTreeNode(tf, root, token_composes);
//		FlattenTreeNodeIntoOne(tf, root);
//		return tf;
//	}
	
//	private static void FlattenOriginTreeNode(TreeFlatten tf, TreeNode rt) {// , TreeMap<String, ArrayList<String>> token_composes
//		ArrayList<TreeNode> childs = rt.GetChildren();
//		if (JDTASTHelper.IsIDLeafNode(rt.GetClazz())) {
//			tf.skt_e_struct_token.add(rt.GetContent());
//			tf.skt_e_struct_token_tree_uid.add(rt.GetTreeUid());
//		} else {
//			tf.skt_e_struct.add(rt.GetContent());
//			tf.skt_e_struct_tree_uid.add(rt.GetTreeUid());
//			int h_count = YStringUtil.CountSubStringInString(rt.GetContent(), "#h");
//			tf.skt_e_struct_h_count.add(h_count);
//			ArrayList<String> h_tree_uids = new ArrayList<String>();
//			tf.skt_e_struct_h_tree_uid.add(h_tree_uids);
//			int v_count = YStringUtil.CountSubStringInString(rt.GetContent(), "#v");
//			tf.skt_e_struct_v_count.add(v_count);
//			ArrayList<String> v_tree_uids = new ArrayList<String>();
//			tf.skt_e_struct_v_tree_uid.add(v_tree_uids);
//			int index = -1;
//			for (TreeNode child : childs) {
//				index++;
//				if (JDTASTHelper.IsIDLeafNode(child.GetClazz())) {
//					v_tree_uids.add(index + "");
//				} else {
//					h_tree_uids.add(index + "");
//				}
//			}
//			Assert.isTrue(v_tree_uids.size() == v_count);
//			Assert.isTrue(h_tree_uids.size() == h_count);
//		}
////		tf.skt_e_struct_token_hv_count.add(YStringUtil.CountSubStringInString(rt.GetContent(), "#h") + YStringUtil.CountSubStringInString(rt.GetContent(), "#v"));
//		for (TreeNode child : childs) {
//			FlattenOriginTreeNode(tf, child);
//		}
//	}

	private static void FlattenTreeNodeIntoOne(TreeFlatten tf, TreeNode rt) {
		ArrayList<TreeNode> childs = rt.GetChildren();
		if (rt.GetContent().equals("{}")) {
//			System.err.println("rt {} children size:" + childs.size());
			Assert.isTrue(childs.size() == 0);
		}
		Class<?> clz = rt.GetClazz();
		if (JDTASTHelper.IsIDLeafNode(clz)) {
//			tf.skt_one_struct_v_count = tf.skt_one_struct_v_count + 1;
//			tf.skt_one_struct_v_tree_uid.add(rt.GetTreeUid());
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
		String rt_cnt = rt.GetContent();
		ArrayList<TreeNode> childs = rt.GetChildren();
		Class<?> clz = rt.GetClazz();
		if (JDTASTHelper.IsIDLeafNode(clz)) {
			Assert.isTrue(childs.size() == 0);
			tf.skt_token.add(rt_cnt);
			tf.skt_token_kind.add(TokenKindUtil.GetTokenKind(rt));
			tf.skt_token_is_var.add(TokenKindUtil.GetTokenIsVar(rt));
		} else {
			tf.skt_pe_struct.add(rt_cnt);
			tf.skt_pe_e_struct.add(new ArrayList<String>());
//			tf.skt_pe_e_struct_tree_uid.add(new ArrayList<String>());
			if (rt instanceof MergedTreeNode) {
				FlattenMergedTreeNode(tf, (MergedTreeNode) rt, rt.GetTreeUid());
			} else {
				int es_size = tf.skt_pe_e_struct.size();
//				int es_tu_size = tf.skt_pe_e_struct_tree_uid.size();
//				Assert.isTrue(es_size == es_tu_size);
				tf.skt_pe_e_struct.get(es_size-1).add(rt_cnt);
//				tf.skt_pe_e_struct_tree_uid.get(es_tu_size-1).add("");
				tf.skt_one_e_struct.add(rt_cnt);
//				tf.skt_one_e_struct_tree_uid.add(rt.GetTreeUid());
			}
			
			int h_count = YStringUtil.CountSubStringInString(rt_cnt, "#h");
			int v_count = YStringUtil.CountSubStringInString(rt_cnt, "#v");
//			tf.skt_pe_struct_h_count.add(h_count);
//			tf.skt_pe_struct_v_count.add(v_count);
			Assert.isTrue(h_count + v_count == childs.size(), "h_count:" + h_count + "#v_count:" + v_count + "#childs.size():" + childs.size() + "#rt.GetContent():" + rt_cnt + "#childs:" + PrintUtil.PrintListToString(childs, "tns"));
			int r_h_count = 0;
			int r_v_count = 0;
//			 skt_pe_struct_v_tree_uid
//			ArrayList<String> h_tids = new ArrayList<String>();
//			tf.skt_pe_struct_h_tree_uid.add(h_tids);
//			ArrayList<String> v_tids = new ArrayList<String>();
//			tf.skt_pe_struct_v_tree_uid.add(v_tids);
			for (TreeNode child : childs) {
//				String r_cid = ExtractRelativeTreeUid(child.GetTreeUid(), rt.GetTreeUid());
				if (JDTASTHelper.IsIDLeafNode(child.GetClazz())) {
//					v_tids.add(r_cid);
					r_v_count++;
				} else {
//					h_tids.add(r_cid);
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
		Assert.isTrue(!(m_child instanceof MergedTreeNode));
//		String m_child_tree_uid = m_child.GetTreeUid();
//		String m_child_relative_tree_uid = ExtractRelativeTreeUid(m_child_tree_uid, tree_uid);
		String cnt = m_child.GetContent();
		int es_size = tf.skt_pe_e_struct.size();
//		int es_tu_size = tf.skt_pe_e_struct_tree_uid.size();
//		Assert.isTrue(es_size == es_tu_size);
		tf.skt_pe_e_struct.get(es_size-1).add(cnt);
//		tf.skt_pe_e_struct_tree_uid.get(es_tu_size-1).add(m_child_relative_tree_uid);
		tf.skt_one_e_struct.add(cnt);
//		tf.skt_one_e_struct_tree_uid.add(m_child.GetTreeUid());
	}
	
//	private static String ExtractRelativeTreeUid(String child_tree_uid, String ancestor_tree_uid) {
//		String pfx = ancestor_tree_uid + " ";
//		Assert.isTrue(child_tree_uid.startsWith(pfx));
//		return child_tree_uid.substring(pfx.length(), child_tree_uid.length());
//	}
	
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
	
	public void PreProcessTree() {
		Assert.isTrue(parent_child_node_pairs == null);
		parent_child_node_pairs = new TreeMap<String, ArrayList<PairContainer<TreeNode, TreeNode>>>();
		root.PreProcessTreeNode("0", parent_child_node_pairs);
	}
	
	public int ApplyMerge(TreeNodeTwoMerge pair) {
		int count = 0;
		while (true) {
			int merge_count = ApplyOneMerge(pair);
			if (merge_count == 0) {
				break;
			}
			count += merge_count;
		}
		return count;
	}
	
	public int ApplyOneMerge(TreeNodeTwoMerge pair) {
		int really_merged = 0;
		PairContainer<TreeNode, TreeNode> match_pc = GetMatchedPC(pair);
		if (match_pc != null) {
			TreeNode tn_par = match_pc.k;
			TreeNode tn = match_pc.v;
			int tn_sib_index = tn_par.GetChildren().indexOf(tn);
			Assert.isTrue(tn_sib_index > -1);
			// exactly matched
			TreeNode tn_par_par = tn_par.GetParent();
			if (tn_par_par != null) {
				RemoveFromParentChildNodePairs(tn_par_par, tn_par);
			}
			MergedTreeNode m_tn_par = new MergedTreeNode(tn_par.GetClazz(), tn_par.GetBinding(), pair.GetMerged(), tn_par.GetTreeWholeContent());
			
//			if (m_tn_par.GetContent().equals("#h&&#h&&#h")) {
//				System.err.println("#h&&#h&&#h merge tree whole content:" + m_tn_par.GetTreeWholeContent());
//				System.exit(1);
//			}
			
			m_tn_par.SetParent(tn_par_par);
			m_tn_par.AppendAllChildren(tn_par.GetChildren());
			ArrayList<TreeNode> m_childs = m_tn_par.GetChildren();
			for (TreeNode m_child : m_childs) {
				m_child.SetParent(m_tn_par);
				RemoveFromParentChildNodePairs(tn_par, m_child);
			}
			TreeNode child = m_childs.remove(tn_sib_index);
			Assert.isTrue(child == tn);
			m_tn_par.SetUpMergedInformation(tn_par, child);
			ArrayList<TreeNode> ccs = child.GetChildren();
			int ccs_len = ccs.size();
			for (int cl = ccs_len - 1; cl >= 0; cl--) {
				TreeNode child_child = ccs.get(cl);
				m_childs.add(tn_sib_index, child_child);
				child_child.SetParent(m_tn_par);
				RemoveFromParentChildNodePairs(child, child_child);
			}
			
			for (TreeNode m_child : m_childs) {
				AddToParentChildNodePairs(m_tn_par, m_child);
			}
			
			if (tn_par_par == null) {
				// tn_par is the root node and tn_par_par is null. 
				Assert.isTrue(this.GetRootNode() == tn_par);
				this.SetRootNode(m_tn_par);
			} else {
				ArrayList<TreeNode> sibs = tn_par_par.GetChildren();
				int tn_sib_idx = sibs.indexOf(tn_par);
				Assert.isTrue(tn_sib_idx >= 0);
				sibs.set(tn_sib_idx, m_tn_par);
				
				AddToParentChildNodePairs(tn_par_par, m_tn_par);
			}
			
			really_merged = 1;
		}
		return really_merged;
	}
	
	public int PossibleMergeCount(TreeNodeTwoMerge pair) {
		ArrayList<PairContainer<TreeNode, TreeNode>> pairs = parent_child_node_pairs.get(pair.GetParentChildPairPresentation());
		if (pairs == null || pairs.size() == 0) {
			return 0;
		}
		int count = 0;
		for (PairContainer<TreeNode, TreeNode> pc : pairs) {
			String parent_str = pair.GetParent();
			String node_str = pair.GetNode();
			
			TreeNode tn_par = pc.k;
			TreeNode tn = pc.v;
			
			if (parent_str.equals(tn_par.GetContent()) && node_str.equals(tn.GetContent())) {
				int tn_sib_index = tn_par.GetChildren().indexOf(tn);
//				System.out.println("tn_sib_index:" + tn_sib_index);
				Assert.isTrue(tn_sib_index > -1);
				if (pair.GetNodeIndex() == tn_sib_index) {
					count++;
				}
			}
		}
		return count;
	}

	private PairContainer<TreeNode, TreeNode> GetMatchedPC(TreeNodeTwoMerge pair) {
		ArrayList<PairContainer<TreeNode, TreeNode>> pairs = parent_child_node_pairs.get(pair.GetParentChildPairPresentation());
		if (pairs == null || pairs.size() == 0) {
			return null;
		}
		
		PairContainer<TreeNode, TreeNode> match_pc = null;
		for (PairContainer<TreeNode, TreeNode> pc : pairs) {
			String parent_str = pair.GetParent();
			String node_str = pair.GetNode();
			
			TreeNode tn_par = pc.k;
			TreeNode tn = pc.v;
			
			if (parent_str.equals(tn_par.GetContent()) && node_str.equals(tn.GetContent())) {
				int tn_sib_index = tn_par.GetChildren().indexOf(tn);
//				System.out.println("tn_sib_index:" + tn_sib_index);
				Assert.isTrue(tn_sib_index > -1);
				if (pair.GetNodeIndex() == tn_sib_index) {
					match_pc = pc;
					break;
				}
			}
		}
		return match_pc;
	}
	
	private void RemoveFromParentChildNodePairs(TreeNode tn_par_par, TreeNode tn_par) {
		String key = TreeNodePairUtil.GetParentChildPairPresentation(tn_par_par.GetContent(), tn_par.GetContent());
		ArrayList<PairContainer<TreeNode, TreeNode>> pcnps = parent_child_node_pairs.get(key);
		PairContainer<TreeNode, TreeNode> rmd = PairContainerUtil.RemovePairContainerFromListAccordingToKeyValue(pcnps, tn_par_par, tn_par);
		Assert.isTrue(rmd != null);
		if (pcnps.size() == 0) {
			parent_child_node_pairs.remove(key);
		}
	}
	
	private void AddToParentChildNodePairs(TreeNode tn_par_par, TreeNode tn_par) {
		String n_pcp = TreeNodePairUtil.GetParentChildPairPresentation(tn_par_par.GetContent(), tn_par.GetContent());
		ArrayList<PairContainer<TreeNode, TreeNode>> pvnps = parent_child_node_pairs.get(n_pcp);
		if (pvnps == null) {
			pvnps = new ArrayList<PairContainer<TreeNode, TreeNode>>();
			parent_child_node_pairs.put(n_pcp, pvnps);
		}
		pvnps.add(new PairContainer<TreeNode, TreeNode>(tn_par_par, tn_par));
	}
	
}
