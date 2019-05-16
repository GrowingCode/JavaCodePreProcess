package statistic.id;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;

import com.google.gson.Gson;

import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import util.FileUtil;

public class GrammarRecorder {
	
//	Map<String, Integer> node_type_id = new TreeMap<String, Integer>();
	
	TreeSet<String> fixed_tokens = new TreeSet<String>();
	TreeSet<String> unfixed_tokens = new TreeSet<String>();
	
	Map<String, TreeSet<String>> self_children_map = new TreeMap<String, TreeSet<String>>();
	
	public GrammarRecorder() {
	}
	
	public void RecordGrammar(ASTNode node) {
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		String tp = JDTASTHelper.GetTypeRepresentationForASTNode(node);
		fixed_tokens.add(tp);
//		EncounterNodeType(sim);
		TreeSet<String> children_nt = self_children_map.get(tp);
		if (children_nt == null) {
			children_nt = new TreeSet<String>();
			self_children_map.put(tp, children_nt);
		}
		for (ASTNode nc : children) {
			String nc_tp = JDTASTHelper.GetTypeRepresentationForASTNode(nc);
			fixed_tokens.add(nc_tp);
			children_nt.add(nc_tp);
		}
	}
	
	public void RecordExtraGrammarForLeaf(ASTNode node, String synonym) {
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		Assert.isTrue(children.size() == 0);
		String tp = JDTASTHelper.GetTypeRepresentationForASTNode(node);
		TreeSet<String> children_nt = self_children_map.get(tp);
		if (children_nt == null) {
			children_nt = new TreeSet<String>();
			self_children_map.put(tp, children_nt);
		}
		String cnt = synonym != null ? synonym : JDTASTHelper.GetContentRepresentationForASTNode(node);
		if (TokenRecorder.LeafIsFixed(node)) {
			fixed_tokens.add(cnt);
		} else {
			unfixed_tokens.add(cnt);
		}
		children_nt.add(cnt);
	}
	
//	private int EncounterNodeType(String node_type) {
//		Integer id = node_type_id.get(node_type);
//		if (id == null) {
//			id = node_type_id.size();
//			node_type_id.put(node_type, id);
//		}
//		return id;
//	}
	
	public void SaveToDirectory(String dir, IDManager im) {
		ArrayList<LinkedList<Integer>> raw = new ArrayList<LinkedList<Integer>>();
		int nti_size = self_children_map.size();
		for (int i=0;i<nti_size;i++) {
			raw.add(new LinkedList<Integer>());
		}
		Set<String> sc_keys = self_children_map.keySet();
		Iterator<String> sc_itr = sc_keys.iterator();
		while (sc_itr.hasNext()) {
			String sc = sc_itr.next();
			TreeSet<String> childrens = self_children_map.get(sc);
			Assert.isTrue(childrens.size() > 0);
			Integer tid = im.GetTypeContentID(sc);
			Assert.isTrue(tid < raw.size());
			LinkedList<Integer> ll = raw.get(tid);
			for (String child : childrens) {
				Integer child_nti = im.GetTypeContentID(child);
				ll.add(child_nti);
			}
		}
		
		ArrayList<Integer> self_children_grammar = new ArrayList<Integer>();
		ArrayList<Integer> self_children_grammar_start = new ArrayList<Integer>();
		ArrayList<Integer> self_children_grammar_end = new ArrayList<Integer>();

		for (int i=0; i<raw.size(); i++) {
			LinkedList<Integer> ll = raw.get(i);
			self_children_grammar_start.add(self_children_grammar.size());
			if (ll.size() == 0) {
				self_children_grammar.add(-1);
			} else {
				for (Integer l : ll) {
					self_children_grammar.add(l);
				}
			}
			self_children_grammar_end.add(self_children_grammar.size()-1);
		}
		
		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_self_children_grammar.json"), gson.toJson(self_children_grammar));
		FileUtil.WriteToFile(new File(dir + "/" + "All_self_children_grammar_start.json"), gson.toJson(self_children_grammar_start));
		FileUtil.WriteToFile(new File(dir + "/" + "All_self_children_grammar_end.json"), gson.toJson(self_children_grammar_end));
	}
	
}
