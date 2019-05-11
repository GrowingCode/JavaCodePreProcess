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

import eclipse.search.JDTSearchForChildrenOfASTNode;
import util.FileUtil;

public class GrammarRecorder {
	
	Map<String, Integer> node_type_id = new TreeMap<String, Integer>();
	
	Map<String, TreeSet<String>> self_children_node_type_map = new TreeMap<String, TreeSet<String>>();
	
	public GrammarRecorder() {
	}
	
	public void RecordGrammar(ASTNode node) {
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		String sim = node.getClass().getSimpleName();
		EncounterNodeType(sim);
		TreeSet<String> children_nt = self_children_node_type_map.get(sim);
		if (children_nt == null) {
			children_nt = new TreeSet<String>();
			self_children_node_type_map.put(sim, children_nt);
		}
		for (ASTNode nc : children) {
			String nc_sim = nc.getClass().getSimpleName();
			children_nt.add(nc_sim);
		}
	}
	
	private int EncounterNodeType(String node_type) {
		Integer id = node_type_id.get(node_type);
		if (id == null) {
			id = node_type_id.size();
			node_type_id.put(node_type, id);
		}
		return id;
	}
	
	public void SaveToDirectory(String dir) {
		ArrayList<LinkedList<Integer>> raw = new ArrayList<LinkedList<Integer>>();
		int nti_size = node_type_id.size();
		for (int i=0;i<nti_size;i++) {
			raw.add(new LinkedList<Integer>());
		}
		Set<String> sc_keys = self_children_node_type_map.keySet();
		Iterator<String> sc_itr = sc_keys.iterator();
		while (sc_itr.hasNext()) {
			String sc = sc_itr.next();
			TreeSet<String> children_types = self_children_node_type_map.get(sc);
			Assert.isTrue(children_types.size() > 0);
			Integer tid = node_type_id.get(sc);
			LinkedList<Integer> ll = raw.get(tid);
			for (String child_type : children_types) {
				Integer child_nti = node_type_id.get(child_type);
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
