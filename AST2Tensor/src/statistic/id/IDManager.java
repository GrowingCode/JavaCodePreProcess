package statistic.id;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

public class IDManager {

	// private static IDManager unique = new IDManager();
	//
	// public static IDManager Instance() {
	// return unique;
	// }

	public static String PrimordialNonLeafASTType = "PrimordialNonLeafASTType";
	
	private TreeMap<String, Integer> ast_type_id_map = new TreeMap<String, Integer>();

	private TreeMap<Integer, TreeMap<String, Integer>> ast_type_content_id_map = new TreeMap<Integer, TreeMap<String, Integer>>();

	private TreeMap<String, Integer> safe = new TreeMap<String, Integer>();

	private TreeMap<String, Integer> count = new TreeMap<String, Integer>();

	public IDManager() {
		// non leaf
		GetTypeID(PrimordialNonLeafASTType);
		// leaf
		GetTypeID(SimpleName.class.getSimpleName());
		GetTypeID(NumberLiteral.class.getSimpleName());
		GetTypeID(CharacterLiteral.class.getSimpleName());
		GetTypeID(StringLiteral.class.getSimpleName());
		GetTypeID(NullLiteral.class.getSimpleName());
	}

	public int GetTypeID(String type) {
		Integer id = ast_type_id_map.get(type);
		if (id == null) {
			id = ast_type_id_map.size();
			ast_type_id_map.put(type, id);
			TreeMap<String, Integer> content_id_map = ast_type_content_id_map.get(id);
			if (content_id_map == null) {
				content_id_map = new TreeMap<String, Integer>();
				ast_type_content_id_map.put(id, content_id_map);
			}
		}
		return id;
	}

	public int GetContentID(String type, String content) {
		int type_id = GetTypeID(type);
		TreeMap<String, Integer> contents_id = ast_type_content_id_map.get(type_id);
		if (contents_id == null) {
			contents_id = new TreeMap<String, Integer>();
			ast_type_content_id_map.put(type_id, contents_id);
		}
		Integer cnt_id = contents_id.get(content);
		if (cnt_id == null) {
			cnt_id = contents_id.size();
			contents_id.put(content, cnt_id);
		}
		return cnt_id;
	}

	public void EncounterANode(String type, String content) {
		String cc = type + "#" + content;
		Integer sc = safe.get(cc);
		if (sc != null) {
			sc++;
			safe.put(cc, sc);
		} else {
			Integer c = count.get(cc);
			if (c == null) {
				c = 0;
			}
			c++;
			count.put(cc, c);
		}
	}

	public void RefineAllStatistics(int minsupport, int maxcapacity) {
		if (count.size() + safe.size() <= maxcapacity) {
			return;
		}
		Set<String> ckeys = count.keySet();
		Iterator<String> ckitr = ckeys.iterator();
		while (ckitr.hasNext()) {
			String ck = ckitr.next();
			int ct = count.get(ck);
			if (ct >= minsupport || ck.startsWith("PrimordialNonLeafASTType#")) {
				String[] tcs = ck.split("#");
				GetContentID(tcs[0], tcs[1]);
				safe.put(ck, ct);
			}
			count.remove(ck);
		}
	}
	
	public boolean IsRefined() {
		return count.isEmpty();
	}
	
	public TreeMap<String, Integer> GetAstTypeIDMap() {
		return ast_type_id_map;
	}
	
	public TreeMap<Integer, TreeMap<String, Integer>> GetAstTypeContentIDMap() {
		return ast_type_content_id_map;
	}
	
}
