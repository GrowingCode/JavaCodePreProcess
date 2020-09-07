package util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class IntegerMapUtil {
	
	public static ArrayList<ArrayList<Integer>> MapToNestedList(Map<Integer, ArrayList<Integer>> one_to_each) {
		ArrayList<ArrayList<Integer>> oe_container = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> oe = new ArrayList<Integer>();
		ArrayList<Integer> oe_start = new ArrayList<Integer>();
		ArrayList<Integer> oe_end = new ArrayList<Integer>();
		oe_container.add(oe);
		oe_container.add(oe_start);
		oe_container.add(oe_end);
		Set<Integer> oe_keys = one_to_each.keySet();
		for (Integer oe_k : oe_keys) {
			int origin_oe_size = oe_start.size();
			for (int i=origin_oe_size;i<oe_k;i++) {
				oe_start.add(-1);
				oe_end.add(-2);
			}
			oe_start.add(one_to_each.size());
			ArrayList<Integer> oe_v = one_to_each.get(oe_k);
			oe.addAll(oe_v);
			oe_end.add(one_to_each.size()-1);
		}
		return oe_container;
	}
	
}
