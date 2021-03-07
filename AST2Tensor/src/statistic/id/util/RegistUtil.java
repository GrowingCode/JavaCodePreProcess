package statistic.id.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Assert;

import util.MapUtil;

public class RegistUtil {
	
	public static int Regist(TreeMap<String, Integer> id_map, Map<String, Integer> hit, int minimum_value_threshold, Map<String, Integer> not_hit, int unk_num, int minimum_num, String desc) {
		int pre_size = id_map.size();
		ArrayList<Entry<String, Integer>> raw_sk_ht = new ArrayList<Entry<String, Integer>>(
				MapUtil.SortMapByValue(hit));
		Collections.reverse(raw_sk_ht);
		
		int i=0;
		int prev_val = Integer.MAX_VALUE;
		for (;i<raw_sk_ht.size();i++) {
			Entry<String, Integer> ent = raw_sk_ht.get(i);
			int e_val = ent.getValue();
			Assert.isTrue(prev_val >= e_val);
			prev_val = e_val;
			if (e_val < minimum_value_threshold) {
				break;
			}
		}
//		ArrayList<Entry<String, Integer>> sk_ht = new ArrayList<Entry<String, Integer>>(raw_sk_ht.subList(i, raw_sk_ht.size()));
//		Collections.reverse(sk_ht);
		
		int hit_num = i - unk_num;
		if (hit_num < minimum_num) {
			hit_num = minimum_num;
		}
		if (hit_num > raw_sk_ht.size()) {
			hit_num = raw_sk_ht.size();
		}
//		System.out.println(desc + " hit_num:" + hit_num + "#id_map.size():" + id_map.size());
		Regist(id_map, MapUtil.EntryListToKeyList(raw_sk_ht.subList(0, hit_num)));

		int r_hit_num = id_map.size();
		Assert.isTrue(hit_num <= r_hit_num && r_hit_num <= pre_size + hit_num, "r_hit_num:" + r_hit_num +"#pre_size + hit_num:" + (pre_size + hit_num));
		
		if (hit_num < raw_sk_ht.size()) {
			Regist(id_map, MapUtil.EntryListToKeyList(raw_sk_ht.subList(hit_num, raw_sk_ht.size())));
		}
//		PrintUtil.PrintPartOfEntryList(raw_sk_ht, hit_num, raw_sk_ht.size(), "SetUnk" + desc,
//				desc, "count");
		
		ArrayList<Entry<String, Integer>> sk_nht = new ArrayList<Entry<String, Integer>>(
				MapUtil.SortMapByValue(not_hit));
		Collections.reverse(sk_nht);
		Regist(id_map, MapUtil.EntryListToKeyList(sk_nht));
		
		return r_hit_num;
	}

	public static void Regist(Map<String, Integer> reg_map, List<String> ele_set) {
		Iterator<String> ele_itr = ele_set.iterator();
		while (ele_itr.hasNext()) {
			String ele = ele_itr.next();
			if (!reg_map.containsKey(ele)) {
				reg_map.put(ele, reg_map.size());
			}
		}
	}

	
}
