package statistic.id;

import java.io.File;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

import net.sf.json.JSONObject;
import util.FileUtil;

public class IDManager {
	
	public static String PrimordialNonLeafASTType = "PrimordialNonLeafASTType";
	public static String SimpleNameLeafDefault = "S";
	public static String NumberLiteralLeafDefault = "100";
	public static String CharacterLiteralLeafDefault = '~' + "";
	public static String StringLiteralLeafDefault = "@str!";
	public static String NullLiteralLeafDefault = "null";

	private TreeMap<String, Integer> ast_type_id_map = new TreeMap<String, Integer>();
	
	private TreeMap<Integer, TreeMap<String, Integer>> ast_type_content_id_map = new TreeMap<Integer, TreeMap<String, Integer>>();

	public IDManager() {
		// non leaf
		GetTypeID(PrimordialNonLeafASTType);
		// leaf
		GetTypeID(SimpleName.class.getSimpleName());
		GetTypeID(NumberLiteral.class.getSimpleName());
		GetTypeID(CharacterLiteral.class.getSimpleName());
		GetTypeID(StringLiteral.class.getSimpleName());
		GetTypeID(NullLiteral.class.getSimpleName());
		
		GetContentID(SimpleName.class.getSimpleName(), SimpleNameLeafDefault);
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
	
	public void SaveToDirectory(String dir) {
		TreeMap<String, Integer> ati = ast_type_id_map;
		TreeMap<Integer, TreeMap<String, Integer>> atci = ast_type_content_id_map;
		JSONObject type_id_json = JSONObject.fromObject(ati);
		FileUtil.WriteToFile(new File(dir + "/" + "type_id.json"), type_id_json.toString());
		Set<String> akeys = ati.keySet();
		for (String ak : akeys) {
			Integer tcid = ati.get(ak);
			TreeMap<String, Integer> tci = atci.get(tcid);
			JSONObject type_content_id_json = JSONObject.fromObject(tci);
			FileUtil.WriteToFile(new File(dir + "/" + ak + "_content_id.json"), type_content_id_json.toString());
		}
	}
	
}
