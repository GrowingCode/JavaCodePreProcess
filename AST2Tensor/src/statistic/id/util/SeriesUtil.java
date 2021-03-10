package statistic.id.util;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.Gson;

import util.FileUtil;
import util.MapUtil;
import util.YStringUtil;

public class SeriesUtil {
	
	public static void GenerateIDJson(String dir, TreeMap<String, Integer> to_gen, String desc) {
//		Map<Object, Object> ati_objs = new HashMap<Object, Object>();
		Map<Integer, String> ati_out = MapUtil.ReverseKeyValueInMap(to_gen);
//		Set<Object> ati_keys = ati_out.keySet();
//		Iterator<Object> ati_key_itr = ati_keys.iterator();
//		while (ati_key_itr.hasNext()) {
//			Object key = ati_key_itr.next();
//			ati_objs.put(key, ati_out.get(key));
//		}
		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_id.json"), gson.toJson(ati_out));// type_id_json.toString()
	}
	
	public static void GenerateSkeletonIDJson(String dir, TreeMap<String, Integer> to_gen, String desc) {
//		Map<Object, Object> ati_objs = new HashMap<Object, Object>();
		Map<Integer, String> ati_out = MapUtil.ReverseKeyValueInMap(to_gen);
		Map<Integer, Integer> ati_h_num = new TreeMap<Integer, Integer>();
		Set<Integer> a_set = ati_out.keySet();
		Iterator<Integer> a_itr = a_set.iterator();
		while (a_itr.hasNext()) {
			Integer a = a_itr.next();
			String str = ati_out.get(a);
			int h_count = YStringUtil.CountSubStringInString(str, "#h");
			ati_h_num.put(a, h_count);
		}
//		Set<Object> ati_keys = ati_out.keySet();
//		Iterator<Object> ati_key_itr = ati_keys.iterator();
//		while (ati_key_itr.hasNext()) {
//			Object key = ati_key_itr.next();
//			ati_objs.put(key, ati_out.get(key));
//		}
		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_id.json"), gson.toJson(ati_out));// type_id_json.toString()
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_h_num.json"), gson.toJson(ati_h_num));// type_id_json.toString()
	}
	
	public static void GenerateStrIDJson(String dir, TreeMap<String, Integer> to_gen, String desc) {
//		Map<Object, Object> ati_objs = new HashMap<Object, Object>();
//		Map<Integer, String> ati_out = MapUtil.ReverseKeyValueInMap(to_gen);
//		Set<Object> ati_keys = ati_out.keySet();
//		Iterator<Object> ati_key_itr = ati_keys.iterator();
//		while (ati_key_itr.hasNext()) {
//			Object key = ati_key_itr.next();
//			ati_objs.put(key, ati_out.get(key));
//		}
		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_str_id.json"), gson.toJson(to_gen));// type_id_json.toString()
	}
	
}
