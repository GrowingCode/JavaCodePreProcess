package statistic.id;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class IDCounter {

	// private static IDManager unique = new IDManager();
	//
	// public static IDManager Instance() {
	// return unique;
	// }
	
	private TreeMap<String, Boolean> type_is_leaf = new TreeMap<String, Boolean>();
	
	private TreeMap<String, Integer> safe = new TreeMap<String, Integer>();

	private TreeMap<String, Integer> count = new TreeMap<String, Integer>();

	public IDCounter() {
	}
	
	public void EncounterANode(String type, boolean is_leaf, String content) {
		type_is_leaf.put(type, is_leaf);
		String cc = type + "&" + content;
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

	public void TempRefineAllStatistics(int minsupport, int maxcapacity) {
		if (count.size() + safe.size() <= maxcapacity) {
			return;
		}
		Set<String> ckeys = count.keySet();
		Iterator<String> ckitr = ckeys.iterator();
		while (ckitr.hasNext()) {
			String ck = ckitr.next();
			int ct = count.get(ck);
			if (ct >= minsupport || ck.startsWith("PrimordialNonLeafASTType#")) {
				safe.put(ck, ct);
			}
		}
		count.clear();
	}
	
	public void FinalRefineAllStatistics(int minsupport, int maxcapacity) {
		boolean skip_minsupport = count.size() + safe.size() <= maxcapacity;
		Set<String> ckeys = count.keySet();
		Iterator<String> ckitr = ckeys.iterator();
		while (ckitr.hasNext()) {
			String ck = ckitr.next();
			int ct = count.get(ck);
			if (ct >= minsupport || skip_minsupport || ck.startsWith("PrimordialNonLeafASTType#")) {
				safe.put(ck, ct);
			}
		}
		count.clear();
	}
	
	public boolean IsRefined() {
		return count.isEmpty();
	}
	
	public void FullFillIDManager(IDManager im) {
		im.EnsureDefaultValue();
		Set<String> skeys = safe.keySet();
		Iterator<String> skitr = skeys.iterator();
		while (skitr.hasNext()) {
			String sk = skitr.next();
			String[] tcs = sk.split("&");
			String type = tcs[0];
			String content = tcs[1];
			im.RegistTypeID(type, type_is_leaf.get(type), safe.get(sk));
			im.RegistContentID(content, safe.get(sk));
		}
	}
	
}
