package statistic.id;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import tree.TreeNode;

public class GrammarRecorder {
	
//	Map<String, Integer> node_type_id = new TreeMap<String, Integer>();
	
//	TreeSet<String> fixed_tokens = new TreeSet<String>();
//	TreeSet<String> unfixed_tokens = new TreeSet<String>();
	
	Map<String, TreeSet<String>> self_children_map = new TreeMap<String, TreeSet<String>>();
	
//	Map<String, TreeMap<String, Integer>> self_children_inner_index_map = new TreeMap<String, TreeMap<String, Integer>>();
	
	public GrammarRecorder() {
	}
	
	public static String GetGrammar(TreeNode node) {
		TreeNode parent = node.GetParent();
		String parent_str = "null";
		String prev_sibling_str = "null";
		if (parent != null) {
			parent_str = parent.GetContent();
			ArrayList<TreeNode> siblings = parent.GetChildren();
			int sib_index = siblings.indexOf(node);
			if (sib_index > -1) {
				TreeNode prev_sibling = siblings.get(sib_index-1);
				prev_sibling_str = prev_sibling.GetContent();
			}
		}
		String m_part = parent_str + "#" + prev_sibling_str;
		return m_part;
	}
	
	public void RecordGrammar(TreeNode node) {
		String m_part = GetGrammar(node);
		TreeSet<String> children_nt = self_children_map.get(m_part);
		if (children_nt == null) {
			children_nt = new TreeSet<String>();
			self_children_map.put(m_part, children_nt);
		}
		children_nt.add(node.GetContent());
	}
	
//	public void RecordGrammar(ASTNode node) {
//		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
//		String tp = JDTASTHelper.GetTypeRepresentationForASTNode(node);
//		fixed_tokens.add(tp);
////		EncounterNodeType(sim);
//		TreeSet<String> children_nt = self_children_map.get(tp);
//		if (children_nt == null) {
//			children_nt = new TreeSet<String>();
//			self_children_map.put(tp, children_nt);
//		}
//		for (ASTNode nc : children) {
//			String nc_tp = JDTASTHelper.GetTypeRepresentationForASTNode(nc);
//			fixed_tokens.add(nc_tp);
//			children_nt.add(nc_tp);
//		}
//	}
	
//	public void RecordExtraGrammarForLeaf(ASTNode node, String synonym) {
//		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
//		Assert.isTrue(children.size() == 0);
//		String tp = JDTASTHelper.GetTypeRepresentationForASTNode(node);
//		TreeSet<String> children_nt = self_children_map.get(tp);
//		if (children_nt == null) {
//			children_nt = new TreeSet<String>();
//			self_children_map.put(tp, children_nt);
//		}
//		String cnt = synonym != null ? synonym : JDTASTHelper.GetContentRepresentationForASTNode(node);
//		if (TokenRecorder.LeafIsFixed(node)) {
//			fixed_tokens.add(cnt);
//		} else {
//			unfixed_tokens.add(cnt);
//		}
//		children_nt.add(cnt);
//	}
	
//	public Integer GetNodeRelativeIndexInGrammar(String parent_cnt, String node_cnt) {
//		Integer relative_id = self_children_inner_index_map.get(parent_cnt).get(node_cnt);
//		return relative_id;
//	}
	
//	private int EncounterNodeType(String node_type) {
//		Integer id = node_type_id.get(node_type);
//		if (id == null) {
//			id = node_type_id.size();
//			node_type_id.put(node_type, id);
//		}
//		return id;
//	}
	
//	public void ProcessNodeRelativeIndexInGrammar() {
//		Set<String> selfs = self_children_map.keySet();
//		Iterator<String> sf_itr = selfs.iterator();
//		while (sf_itr.hasNext()) {
//			String sf = sf_itr.next();
//			TreeSet<String> children = self_children_map.get(sf);
//			TreeMap<String, Integer> children_inner_index = self_children_inner_index_map.get(sf);
//			if (children_inner_index == null) {
//				children_inner_index = new TreeMap<String, Integer>();
//				self_children_inner_index_map.put(sf, children_inner_index);
//			}
//			int index = -1;
//			Iterator<String> c_itr = children.iterator();
//			while (c_itr.hasNext()) {
//				String c = c_itr.next();
//				index++;
//				children_inner_index.put(c, index);
//			}
//		}
//	}
	
//	public void SaveToDirectory(String dir, IDManager im) {
//		ArrayList<LinkedList<Integer>> raw = new ArrayList<LinkedList<Integer>>();
//		int nti_size = self_children_map.size() + IDManager.reserved_words.size();
//		for (int i=0;i<nti_size;i++) {
//			raw.add(new LinkedList<Integer>());
//		}
//		Set<String> sc_keys = self_children_map.keySet();
//		Iterator<String> sc_itr = sc_keys.iterator();
//		while (sc_itr.hasNext()) {
//			String sc = sc_itr.next();
//			TreeSet<String> childrens = self_children_map.get(sc);
//			Assert.isTrue(childrens.size() > 0);
//			Integer tid = im.GetTypeContentID(sc);
////			Assert.isTrue(tid < raw.size());
//			LinkedList<Integer> ll = raw.get(tid);
//			for (String child : childrens) {
//				Integer child_nti = im.GetTypeContentID(child);
//				ll.add(child_nti);
//			}
//		}
//		
//		ArrayList<Integer> self_children_grammar = new ArrayList<Integer>();
//		ArrayList<Integer> self_children_grammar_start = new ArrayList<Integer>();
//		ArrayList<Integer> self_children_grammar_end = new ArrayList<Integer>();
//
//		for (int i=0; i<raw.size(); i++) {
//			LinkedList<Integer> ll = raw.get(i);
//			self_children_grammar_start.add(self_children_grammar.size());
//			if (ll.size() == 0) {
//				self_children_grammar.add(-1);
//			} else {
//				for (Integer l : ll) {
//					self_children_grammar.add(l);
//				}
//			}
//			self_children_grammar_end.add(self_children_grammar.size()-1);
//		}
//		
//		Gson gson = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_self_children_grammar.json"), gson.toJson(self_children_grammar));
//		FileUtil.WriteToFile(new File(dir + "/" + "All_self_children_grammar_start.json"), gson.toJson(self_children_grammar_start));
//		FileUtil.WriteToFile(new File(dir + "/" + "All_self_children_grammar_end.json"), gson.toJson(self_children_grammar_end));
//	}
	
}
