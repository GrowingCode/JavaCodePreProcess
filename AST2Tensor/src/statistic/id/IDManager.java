package statistic.id;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONObject;
import util.FileUtil;
import util.MapUtil;

public class IDManager {
	
	public static String Default = "@Default";
	// non-leaf type
	public static String PrimordialNonLeafASTType = "PrimordialNonLeafASTType";
	// leaf type
	public static String TerminalLeafASTType = "TerminalLeafASTType";
	// leaf value
//	public static String SimpleNameLeafDefault = "SNDefault";
//	public static String NumberLiteralLeafDefault = "100";
//	public static String CharacterLiteralLeafDefault = '~' + "";
//	public static String StringLiteralLeafDefault = "\"@str!\"";
//	public static String NullLiteralLeafDefault = "null";
//	public static String TerminalLeafDefault = "TermDefault";

	private TreeMap<String, Integer> ast_type_id_map = new TreeMap<String, Integer>();
	private TreeMap<Integer, Integer> ast_type_id_count_map = new TreeMap<Integer, Integer>();
	
	private TreeMap<Integer, TreeMap<String, Integer>> ast_type_content_id_map = new TreeMap<Integer, TreeMap<String, Integer>>();
	private TreeMap<Integer, TreeMap<Integer, Integer>> ast_type_content_id_count_map = new TreeMap<Integer, TreeMap<Integer, Integer>>();
	
	public IDManager() {
		RegistTypeID(Default, 0);
	}

	private int RegistTypeID(String type, int count) {
		Integer id = ast_type_id_map.get(type);
		if (id == null) {
			id = ast_type_id_map.size();
			ast_type_id_map.put(type, id);
			TreeMap<String, Integer> content_id_map = ast_type_content_id_map.get(id);
			if (content_id_map == null) {
				content_id_map = new TreeMap<String, Integer>();
				content_id_map.put(Default, 0);
				ast_type_content_id_map.put(id, content_id_map);
			}
			TreeMap<Integer, Integer> content_id_count_map = ast_type_content_id_count_map.get(id);
			if (content_id_count_map == null) {
				content_id_count_map = new TreeMap<Integer, Integer>();
				ast_type_content_id_count_map.put(id, content_id_count_map);
			}
		}
		Integer ct = ast_type_id_count_map.get(id);
		if (ct == null) {
			ct = 0;
		}
		ct += count;
		ast_type_id_count_map.put(id, ct);
		return id;
	}

	public int RegistContentID(String type, String content, int count) {
		int type_id = RegistTypeID(type, count);
		TreeMap<String, Integer> contents_id = ast_type_content_id_map.get(type_id);
		TreeMap<Integer, Integer> contents_count_id = ast_type_content_id_count_map.get(type_id);
//		if (contents_id == null) {
//			contents_id = new TreeMap<String, Integer>();
//			ast_type_content_id_map.put(type_id, contents_id);
//		}
		Integer cnt_id = contents_id.get(content);
		if (cnt_id == null) {
			cnt_id = contents_id.size();
			contents_id.put(content, cnt_id);
		}
		Integer ct = contents_count_id.get(cnt_id);
		if (ct == null) {
			ct = 0;
		}
		ct += count;
		contents_count_id.put(cnt_id, ct);
		return cnt_id;
	}

	public void EnsureDefaultValue() {
		// non leaf
		RegistTypeID(PrimordialNonLeafASTType, 0);
		// leaf
		RegistTypeID(TerminalLeafASTType, 0);
//		GetTypeID(TerminalLeafASTType);
//		GetTypeID(SimpleName.class.getSimpleName());
//		GetTypeID(NumberLiteral.class.getSimpleName());
//		GetTypeID(CharacterLiteral.class.getSimpleName());
//		GetTypeID(StringLiteral.class.getSimpleName());
//		GetTypeID(NullLiteral.class.getSimpleName());

//		GetContentID(TerminalLeafASTType, TerminalLeafDefault);
//		GetContentID(SimpleName.class.getSimpleName(), SimpleName.class.getSimpleName() + SimpleNameLeafDefault);
//		GetContentID(NumberLiteral.class.getSimpleName(), NumberLiteralLeafDefault);
//		GetContentID(CharacterLiteral.class.getSimpleName(), CharacterLiteralLeafDefault);
//		GetContentID(StringLiteral.class.getSimpleName(), StringLiteralLeafDefault);
//		GetContentID(NullLiteral.class.getSimpleName(), NullLiteralLeafDefault);
		
		Set<String> akeys = ast_type_id_map.keySet();
		Iterator<String> aitr = akeys.iterator();
		while (aitr.hasNext()) {
			String ak = aitr.next();
			RegistTypeID(ak, 0);
//			Integer aid = ast_type_id_map.get(ak);
//			TreeMap<String, Integer> cidm = ast_type_content_id_map.get(aid);
//			if (!cidm.containsKey(ak + SimpleNameLeafDefault)) {
//				GetContentID(ak, ak + SimpleNameLeafDefault);
//			}
		}
	}

	public int GetTypeID(String type) {
		Integer id = ast_type_id_map.get(type);
		if (id != null) {
			return id;
		}
		return 0;
	}

	public int GetContentID(String type, String content) {
		int tid = GetTypeID(type);
		TreeMap<String, Integer> content_id_map = ast_type_content_id_map.get(tid);
		Integer id = content_id_map.get(content);
		if (id != null) {
			return id;
		}
		return 0;
	}
	
	public void SaveToDirectory(String dir) {
		List<Integer> summary = new LinkedList<Integer>();
		TreeMap<String, Integer> ati = ast_type_id_map;
		summary.add(ati.size());
		Map<Object, Object> ati_out = MapUtil.ReverseKeyValueInMap(ati);
		Map<String, Object> ati_out_str = MapUtil.CastKeyToString(ati_out);
		TreeMap<Integer, TreeMap<String, Integer>> atci = ast_type_content_id_map;
		JSONObject type_id_json = JSONObject.fromObject(ati_out_str);
		FileUtil.WriteToFile(new File(dir + "/" + "All_type_id.json"), type_id_json.toString());
		Set<String> akeys = ati.keySet();
		for (String ak : akeys) {
			Integer tcid = ati.get(ak);
			TreeMap<String, Integer> tci = atci.get(tcid);
			summary.add(tci.size());
			Map<Object, Object> tci_out = MapUtil.ReverseKeyValueInMap(tci);
			Map<String, Object> tci_out_str = MapUtil.CastKeyToString(tci_out);
			JSONObject type_content_id_json = JSONObject.fromObject(tci_out_str);
			FileUtil.WriteToFile(new File(dir + "/" + ak + "_content_id.json"), type_content_id_json.toString());
		}
		FileUtil.WriteToFile(new File(dir + "/" + "summary.txt"), StringUtils.join(summary, " "));
	}

}
