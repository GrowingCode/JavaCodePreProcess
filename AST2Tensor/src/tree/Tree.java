package tree;

import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

import bpe.skt.ExactTreeNodeTwoMerge;
import bpe.skt.TreeNodeTwoMerge;
import eclipse.jdt.JDTASTHelper;
import statistic.id.ParentSktHintManager;
import translation.tensor.util.TokenKindUtil;
import tree.util.TreeNodePairUtil;
import unit.PairContainer;
import unit.util.PairContainerUtil;
import util.PrintUtil;
import util.ReflectUtil;
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

//	public void FlattenTree() {// TreeMap<String, ArrayList<String>> token_composes
////		if (tf.skt_one_struct.size() == 0) {
//		if (tf.skt_one_struct.isEmpty()) {
////			Assert.isTrue(tf == null);
////			tf.skt_one_struct_v_count.add(0);
//			FlattenTreeNode(tf, root);// , token_composes
//			if (flatten_one) {
//				FlattenTreeNodeIntoOne(tf, root);
//			}
////			Assert.isTrue(tf.skt_one_struct.size() == 0);
//			tf.skt_one_struct.add(root.GetContent());
//			tf.skt_one_struct_hint.add("1");
////			tf.skt_one_struct = root.GetContent();
//		}
////		return tf;
//	}
	
//	public void FlattenTree() {// TreeMap<String, ArrayList<String>> token_composes
////		if (tf.skt_one_struct.size() == 0) {
//		if (tf.skt_one_struct.isEmpty()) {
////			Assert.isTrue(tf == null);
////			tf.skt_one_struct_v_count.add(0);
//			FlattenTreeNode(tf, root);// , token_composes
//			if (flatten_one) {
//				FlattenTreeNodeIntoOne(tf, root);
//			}
////			Assert.isTrue(tf.skt_one_struct.size() == 0);
//			tf.skt_one_struct.add(root.GetContent());
//			tf.skt_one_struct_hint.add("1");
////			tf.skt_one_struct = root.GetContent();
//		}
////		return tf;
//	}
	
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
	
	public void SktMergeIntoOne() {
		ExactTreeNodeTwoMerge etntm = null;
		do {
			etntm = SearchForExactMergeIntoOneNodePair(root);
			if (etntm != null) {
				ApplyExactOneMerge(etntm);
			}
		} while (etntm != null);
	}
	
	private ExactTreeNodeTwoMerge SearchForExactMergeIntoOneNodePair(TreeNode rt) {
		ExactTreeNodeTwoMerge res = null;
		ArrayList<TreeNode> childs = rt.GetChildren();
		if (rt.GetContent().equals("{}")) {
			Assert.isTrue(childs.size() == 0);
		}
		if (childs.size() == 0) {
		} else {
			int i_len = childs.size();
			for (int i = i_len-1; i >= 0; i--) {
				TreeNode child = childs.get(i);
				
				boolean to_merge = true;
				if (JDTASTHelper.IsV(child.OriginIsNonCompositeLeaf(), child.GetClazz())) {
					to_merge = false;
				}
				if (to_merge) {
					Assert.isTrue(!rt.GetContent().equals("{}"));
					String mgd = YStringUtil.ReplaceSpecifiedContentInSpecifiedPosition(rt.GetContent(), child.GetContent(), i);
					res = new ExactTreeNodeTwoMerge(rt, child, mgd);
					break;
//					rt.SetContent(mgd);
//					childs.remove(i);
				}
			}
			
			if (res == null) {
				for (int i = i_len-1; i >= 0; i--) {
					TreeNode child = childs.get(i);
					res = SearchForExactMergeIntoOneNodePair(child);
					if (res != null) {
						break;
					}
				}
			}
		}
		return res;
	}

//	private void SktMergeIntoOne(TreeNode rt) {
//		ArrayList<TreeNode> childs = rt.GetChildren();
//		if (rt.GetContent().equals("{}")) {
////			System.err.println("rt {} children size:" + childs.size());
//			Assert.isTrue(childs.size() == 0);
//		}
////		Class<?> clz = rt.GetClazz();
//		if (childs.size() == 0) {// .IsIDLeafNode(clz)
////			tf.skt_one_struct_v_count = tf.skt_one_struct_v_count + 1;
////			tf.skt_one_struct_v_tree_uid.add(rt.GetTreeUid());
//		} else {
//			int i_len = childs.size();
//			for (int i = i_len-1; i >= 0; i--) {
//				TreeNode child = childs.get(i);
//				SktMergeIntoOne(child);
//				boolean to_merge = true;
//				if (JDTASTHelper.IsV(child.OriginIsNonCompositeLeaf(), child.GetClazz())) {
//					to_merge = false;
//				}
//				if (to_merge) {
////					Assert.isTrue(child.GetChildren().size() > 0, "wrong content:" + rt.GetContent() + "#wrong type:" + child.GetClazz());
//					Assert.isTrue(!rt.GetContent().equals("{}"));
////					System.err.println("==== before merge:" + rt.GetContent() + " ====");
//					String mgd = YStringUtil.ReplaceSpecifiedContentInSpecifiedPosition(rt.GetContent(), child.GetContent(), i);
////					System.err.println("==== after merge:" + mgd + " ====");
//					rt.SetContent(mgd);
//					childs.remove(i);
//				}
////				if (!JDTASTHelper.IsIDLeafNode(child.GetClazz())) {
//////					Assert.isTrue(child.GetChildren().size() > 0, "wrong content:" + rt.GetContent() + "#wrong type:" + child.GetClazz());
////					Assert.isTrue(!rt.GetContent().equals("{}"));
//////					System.err.println("==== before merge:" + rt.GetContent() + " ====");
////					String mgd = YStringUtil.ReplaceSpecifiedContentInSpecifiedPosition(rt.GetContent(), child.GetContent(), i);
//////					System.err.println("==== after merge:" + mgd + " ====");
////					rt.SetContent(mgd);
////					childs.remove(i);
////				}
//			}
//		}
////		if (tf.skt_one_struct.size() == 0) {
////			tf.skt_one_struct.add(rt.GetContent());
////		} else {
////			tf.skt_one_struct.set(0, tf.skt_one_struct.get(0) + "#" + rt.GetContent());
////		}
//	}
	// SktTensorTools stt, 
	public void TraverseAndRecordTreeNode(String pfx) {
		TraverseAndRecordTreeNode(pfx, root);// stt, 
	}
	// SktTensorTools stt, 
	private void TraverseAndRecordTreeNode(String pfx, TreeNode rt) {
		@SuppressWarnings("unchecked")
		ArrayList<String> struct_hint = (ArrayList<String>) ReflectUtil.ReflectField(pfx + "_struct_hint", tf);
		@SuppressWarnings("unchecked")
		ArrayList<String> struct = (ArrayList<String>) ReflectUtil.ReflectField(pfx + "_struct", tf);
		@SuppressWarnings("unchecked")
		ArrayList<Integer> struct_count = (ArrayList<Integer>) ReflectUtil.ReflectField(pfx + "_struct_count", tf);
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<String>> struct_e_struct = (ArrayList<ArrayList<String>>) ReflectUtil.ReflectField(pfx + "_e_struct", tf);
		@SuppressWarnings("unchecked")
		ArrayList<String> token_hint = (ArrayList<String>) ReflectUtil.ReflectField(pfx + "_token_hint", tf);
		@SuppressWarnings("unchecked")
		ArrayList<String> token = (ArrayList<String>) ReflectUtil.ReflectField(pfx + "_token", tf);
		@SuppressWarnings("unchecked")
		ArrayList<Integer> token_count = (ArrayList<Integer>) ReflectUtil.ReflectField(pfx + "_token_count", tf);
		@SuppressWarnings("unchecked")
		ArrayList<Integer> token_kind = (ArrayList<Integer>) ReflectUtil.ReflectField(pfx + "_token_kind", tf);
		@SuppressWarnings("unchecked")
		ArrayList<Integer> token_is_var = (ArrayList<Integer>) ReflectUtil.ReflectField(pfx + "_token_is_var", tf);
		
		String pinfo = pfx + "_par_info";
		rt.SetUpParentInfoOfChildren(pinfo);
//		ParentSktHintManager pshm = stt.pshm;
		@SuppressWarnings("unchecked")
		ArrayList<TreeNodeParentInfo> par_info = (ArrayList<TreeNodeParentInfo>) ReflectUtil.ReflectField(pinfo, rt);
		
		String rt_cnt = rt.GetContent();
		ArrayList<TreeNode> childs = rt.GetChildren();
		Class<?> clz = rt.GetClazz();
		String hint_str = ParentSktHintManager.GenParentHint(par_info);
		if (JDTASTHelper.IsV(rt.OriginIsNonCompositeLeaf(), clz)) {// rt.OriginIsNonCompositeLeaf() && (JDTASTHelper.IsIDLeafNode(clz) || MetaOfApp.SktNotOnlyExcludeIDLeafButAllLeaf)
			Assert.isTrue(childs.size() == 0);
			token_hint.add(hint_str);
			token.add(rt_cnt);
			token_count.add(rt.GetNodeCount());
			token_kind.add(TokenKindUtil.GetTokenKind(rt));
			token_is_var.add(TokenKindUtil.GetTokenIsVar(rt));
//			
//			Integer kind = tf.token_kind.get(rt_cnt);
//			if (kind == null) {
//				kind = TokenKindUtil.GetTokenKind(rt);
//				tf.token_kind.put(rt_cnt, kind);
//			} else {
//				Assert.isTrue(TokenKindUtil.GetTokenKind(rt) == kind, "strange_cnt:" + rt_cnt + "#TokenKindUtil.GetTokenKind(rt):" + TokenKindUtil.GetTokenKind(rt) + "#kind:" + kind);
//			}
//			Integer is_var = tf.token_is_var.get(rt_cnt);
//			if (is_var == null) {
//				is_var = TokenKindUtil.GetTokenIsVar(rt);
//				tf.token_is_var.put(rt_cnt, is_var);
//			} else {
//				Assert.isTrue(TokenKindUtil.GetTokenIsVar(rt) == is_var, "strange_cnt:" + rt_cnt + "#TokenKindUtil.GetTokenIsVar(rt):" + TokenKindUtil.GetTokenIsVar(rt) + "#is_var:" + is_var);
//			}
		} else {
			struct_hint.add(hint_str);
			struct.add(rt_cnt);
			struct_count.add(rt.GetNodeCount());
			
			struct_e_struct.add(new ArrayList<String>());
			if (rt instanceof MergedTreeNode) {
				FlattenMergedTreeNode(struct_e_struct, (MergedTreeNode) rt, rt.GetTreeUid());
			} else {
				int es_size = struct_e_struct.size();
				struct_e_struct.get(es_size-1).add(rt_cnt);
			}
			
			for (TreeNode child : childs) {
				TraverseAndRecordTreeNode(pfx, child);// stt, 
			}
			
			// validate
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
				if (child.OriginIsNonCompositeLeaf()) {// JDTASTHelper.IsIDLeafNode(child.GetClazz())
//					v_tids.add(r_cid);
					r_v_count++;
				} else {
//					h_tids.add(r_cid);
					r_h_count++;
				}
			}
			Assert.isTrue(r_h_count == h_count, "r_h_count:" + r_h_count + "#h_count:" + h_count + "#r_v_count:" + r_v_count + "#v_count:" + v_count + "#rt_cnt:" + rt_cnt);
			Assert.isTrue(r_v_count == v_count, "r_h_count:" + r_h_count + "#h_count:" + h_count + "#r_v_count:" + r_v_count + "#v_count:" + v_count + "#rt_cnt:" + rt_cnt);
		}
	}
	
//	private static void FlattenTreeNode(TreeFlatten tf, TreeNode rt) {// , TreeMap<String, ArrayList<String>> token_composes
//		String rt_cnt = rt.GetContent();
//		ArrayList<TreeNode> childs = rt.GetChildren();
//		Class<?> clz = rt.GetClazz();
//		if (rt.OriginIsNonCompositeLeaf() && (JDTASTHelper.IsIDLeafNode(clz) || MetaOfApp.SktNotOnlyExcludeIDLeafButAllLeaf)) {
//			Assert.isTrue(childs.size() == 0);
//			tf.skt_token.add(rt_cnt);
//			tf.skt_token_kind.add(TokenKindUtil.GetTokenKind(rt));
//			tf.skt_token_is_var.add(TokenKindUtil.GetTokenIsVar(rt));
//		} else {
//			tf.skt_pe_struct.add(rt_cnt);
//			tf.skt_pe_e_struct.add(new ArrayList<String>());
////			tf.skt_pe_e_struct_tree_uid.add(new ArrayList<String>());
//			if (rt instanceof MergedTreeNode) {
//				FlattenMergedTreeNode(tf, (MergedTreeNode) rt, rt.GetTreeUid());
//			} else {
//				int es_size = tf.skt_pe_e_struct.size();
////				int es_tu_size = tf.skt_pe_e_struct_tree_uid.size();
////				Assert.isTrue(es_size == es_tu_size);
//				tf.skt_pe_e_struct.get(es_size-1).add(rt_cnt);
////				tf.skt_pe_e_struct_tree_uid.get(es_tu_size-1).add("");
//				tf.skt_one_e_struct.add(rt_cnt);
////				tf.skt_one_e_struct_tree_uid.add(rt.GetTreeUid());
//			}
//			
//			int h_count = YStringUtil.CountSubStringInString(rt_cnt, "#h");
//			int v_count = YStringUtil.CountSubStringInString(rt_cnt, "#v");
////			tf.skt_pe_struct_h_count.add(h_count);
////			tf.skt_pe_struct_v_count.add(v_count);
//			Assert.isTrue(h_count + v_count == childs.size(), "h_count:" + h_count + "#v_count:" + v_count + "#childs.size():" + childs.size() + "#rt.GetContent():" + rt_cnt + "#childs:" + PrintUtil.PrintListToString(childs, "tns"));
//			int r_h_count = 0;
//			int r_v_count = 0;
////			 skt_pe_struct_v_tree_uid
////			ArrayList<String> h_tids = new ArrayList<String>();
////			tf.skt_pe_struct_h_tree_uid.add(h_tids);
////			ArrayList<String> v_tids = new ArrayList<String>();
////			tf.skt_pe_struct_v_tree_uid.add(v_tids);
//			for (TreeNode child : childs) {
////				String r_cid = ExtractRelativeTreeUid(child.GetTreeUid(), rt.GetTreeUid());
//				if (child.OriginIsNonCompositeLeaf()) {// JDTASTHelper.IsIDLeafNode(child.GetClazz())
////					v_tids.add(r_cid);
//					r_v_count++;
//				} else {
////					h_tids.add(r_cid);
//					r_h_count++;
//				}
//			}
//			Assert.isTrue(r_h_count == h_count, "r_h_count:" + r_h_count + "#h_count:" + h_count + "#r_v_count:" + r_v_count + "#v_count:" + v_count + "#rt_cnt:" + rt_cnt);
//			Assert.isTrue(r_v_count == v_count, "r_h_count:" + r_h_count + "#h_count:" + h_count + "#r_v_count:" + r_v_count + "#v_count:" + v_count + "#rt_cnt:" + rt_cnt);
//			
//			for (TreeNode child : childs) {
//				FlattenTreeNode(tf, child);// , token_composes
//			}
//		}
//	}
	
	private static void FlattenMergedTreeNode(ArrayList<ArrayList<String>> struct_e_struct, MergedTreeNode mtn, final String tree_uid) {
		TreeNode m_base_parent = mtn.GetMergeBaseParent();
		if (m_base_parent instanceof MergedTreeNode) {
			FlattenMergedTreeNode(struct_e_struct, (MergedTreeNode) m_base_parent, tree_uid);
		} else {
			HandleTreeNodeFlatten(struct_e_struct, m_base_parent, tree_uid);
		}
		
		TreeNode m_child = mtn.GetMergeChild();
		if (m_child instanceof MergedTreeNode) {
			FlattenMergedTreeNode(struct_e_struct, (MergedTreeNode) m_child, tree_uid);
		} else {
			HandleTreeNodeFlatten(struct_e_struct, m_child, tree_uid);
		}
	}
	
	private static void HandleTreeNodeFlatten(ArrayList<ArrayList<String>> struct_e_struct, TreeNode m_child, final String tree_uid) {
		Assert.isTrue(!(m_child instanceof MergedTreeNode));
		String cnt = m_child.GetContent();
		int es_size = struct_e_struct.size();
		struct_e_struct.get(es_size-1).add(cnt);
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
	
//	public void EnsureLeafNodesInTree() {
//		root.EnsureLeafNode();
//	}
	
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
	
	public int ApplyExactOneMerge(ExactTreeNodeTwoMerge pair) {
		ApplyOneMergeInDetail(pair.parent, pair.node, pair.merged_cnt);
		return 1;
	}
	
	public int ApplyOneMerge(TreeNodeTwoMerge pair) {
		PairContainer<TreeNode, TreeNode> match_pc = GetMatchedPC(pair);
		if (match_pc != null) {
			TreeNode tn_par = match_pc.k;
			TreeNode tn = match_pc.v;
			ApplyOneMergeInDetail(tn_par, tn, pair.GetMerged());
			return 1;
		}
		return 0;
	}
	
	public void ApplyOneMergeInDetail(TreeNode tn_par, TreeNode tn, String merged_tn_par_cnt) {
//		int really_merged = 0;
//		if (match_pc != null) {
		int tn_sib_index = tn_par.GetChildren().indexOf(tn);
		Assert.isTrue(tn_sib_index > -1);
		// exactly matched
		TreeNode tn_par_par = tn_par.GetParent();
		if (tn_par_par != null) {
			RemoveFromParentChildNodePairs(tn_par_par, tn_par);
		}
		MergedTreeNode m_tn_par = new MergedTreeNode(tn_par.GetClazz(), tn_par.OriginIsNonCompositeLeaf(), tn_par.GetBinding(), merged_tn_par_cnt, tn_par.GetTreeWholeContent(), tn_par.GetNodeCount() + tn.GetNodeCount());
		
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
		
//		really_merged = 1;
//		}
//		return really_merged;
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
