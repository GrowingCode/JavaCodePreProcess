package statistic.id;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jdt.core.dom.Block;

import huffman.GenerateHuffmanTree;
import huffman.HuffmanNode;
import huffman.WordInfo;
import net.sf.json.JSONArray;
import util.FileUtil;
import util.MapUtil;

public class IDManager {

	public static String Default = "@Default";
	// non-leaf type
	public static String VirtualChildrenConnectionNonLeafASTType = "VirtualChildrenConnectionNonLeafASTType";
	// non-leaf type
	// public static String PrimordialNonLeafASTType = "PrimordialNonLeafASTType";
	// leaf type
	public static String TerminalLeafASTType = "TerminalLeafASTType";
	// leaf type
	public static String InitialLeafASTType = "InitialLeafASTType";
	// leaf value
	// public static String SimpleNameLeafDefault = "SNDefault";
	// public static String NumberLiteralLeafDefault = "100";
	// public static String CharacterLiteralLeafDefault = '~' + "";
	// public static String StringLiteralLeafDefault = "\"@str!\"";
	// public static String NullLiteralLeafDefault = "null";
	// public static String TerminalLeafDefault = "TermDefault";

	private TreeMap<String, Integer> ast_type_id_map = new TreeMap<String, Integer>();
	private TreeMap<Integer, Integer> ast_type_id_count_map = new TreeMap<Integer, Integer>();
	private ArrayList<Integer> ast_type_is_leaf = new ArrayList<Integer>();

	private TreeMap<String, Integer> ast_content_id_map = new TreeMap<String, Integer>();
	private TreeMap<Integer, Integer> ast_content_id_count_map = new TreeMap<Integer, Integer>();

	// private TreeMap<Integer, TreeMap<String, Integer>> ast_type_content_id_map =
	// new TreeMap<Integer, TreeMap<String, Integer>>();
	// private TreeMap<Integer, TreeMap<Integer, Integer>>
	// ast_type_content_id_count_map = new TreeMap<Integer, TreeMap<Integer,
	// Integer>>();

	public IDManager() {
		RegistTypeID(Default, true, 0);
	}

	public int RegistTypeID(String type, boolean is_leaf, int count) {
		Integer id = ast_type_id_map.get(type);
		if (id == null) {
			id = ast_type_id_map.size();
			ast_type_id_map.put(type, id);
			ast_type_is_leaf.add(id, (is_leaf ? 1 : 0));
		}
		Integer ct = ast_type_id_count_map.get(id);
		if (ct == null) {
			ct = 0;
		}
		ct += count;
		ast_type_id_count_map.put(id, ct);
		return id;
	}

	public int RegistContentID(String content, int count) {
		Integer id = ast_content_id_map.get(content);
		if (id == null) {
			id = ast_content_id_map.size();
			ast_content_id_map.put(content, id);
		}
		Integer ct = ast_content_id_count_map.get(id);
		if (ct == null) {
			ct = 0;
		}
		ct += count;
		ast_content_id_count_map.put(id, ct);
		return id;
	}

	public void EnsureDefaultValue() {
		// non leaf
		RegistTypeID(VirtualChildrenConnectionNonLeafASTType, false, 0);
		// non leaf
		RegistTypeID(Block.class.getSimpleName(), false, 0);
		// leaf
		RegistTypeID(TerminalLeafASTType, true, 0);
		// leaf
		RegistTypeID(InitialLeafASTType, true, 0);
		// GetTypeID(TerminalLeafASTType);
		// GetTypeID(SimpleName.class.getSimpleName());
		// GetTypeID(NumberLiteral.class.getSimpleName());
		// GetTypeID(CharacterLiteral.class.getSimpleName());
		// GetTypeID(StringLiteral.class.getSimpleName());
		// GetTypeID(NullLiteral.class.getSimpleName());

		// GetContentID(TerminalLeafASTType, TerminalLeafDefault);
		// GetContentID(SimpleName.class.getSimpleName(),
		// SimpleName.class.getSimpleName() + SimpleNameLeafDefault);
		// GetContentID(NumberLiteral.class.getSimpleName(), NumberLiteralLeafDefault);
		// GetContentID(CharacterLiteral.class.getSimpleName(),
		// CharacterLiteralLeafDefault);
		// GetContentID(StringLiteral.class.getSimpleName(), StringLiteralLeafDefault);
		// GetContentID(NullLiteral.class.getSimpleName(), NullLiteralLeafDefault);

		// Set<String> akeys = ast_type_id_map.keySet();
		// Iterator<String> aitr = akeys.iterator();
		// while (aitr.hasNext()) {
		// String ak = aitr.next();
		// RegistTypeID(ak, 0);
		// Integer aid = ast_type_id_map.get(ak);
		// TreeMap<String, Integer> cidm = ast_type_content_id_map.get(aid);
		// if (!cidm.containsKey(ak + SimpleNameLeafDefault)) {
		// GetContentID(ak, ak + SimpleNameLeafDefault);
		// }
		// }
	}

	public int GetTypeID(String type) {
		Integer id = ast_type_id_map.get(type);
		if (id != null) {
			return id;
		}
		return 0;
	}

	public int GetContentID(String content) {
		Integer id = ast_content_id_map.get(content);
		if (id != null) {
			return id;
		}
		return 0;
	}

	private void GenerateIDJson(String dir, TreeMap<String, Integer> to_gen, String desc) {
		// List<Integer> summary = new LinkedList<Integer>();
		// TreeMap<String, Integer> ati = ast_type_id_map;
		// summary.add(ati.size());
		Map<Object, Object> ati_out = MapUtil.ReverseKeyValueInMap(to_gen);
		Set<Object> ati_keys = ati_out.keySet();
		List<Object> ati_objs = new LinkedList<Object>();
		Iterator<Object> ati_key_itr = ati_keys.iterator();
		while (ati_key_itr.hasNext()) {
			Object key = ati_key_itr.next();
			ati_objs.add(ati_out.get(key));
		}
		JSONArray type_id_json = JSONArray.fromObject(ati_objs);
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_id.json"), type_id_json.toString());
	}
	
	private void GenerateHuffTree(String dir, TreeMap<Integer, Integer> count_map, String desc) {
		HuffmanNode root = GenerateHuffmanTree.BuildTree(count_map);
		WordInfo wi = GenerateHuffmanTree.BuildEncodeTensor(root);
		int[][] type_huffman_leaf_node_encode_tensor = wi.getEncode();
		int[] type_huffman_leaf_node_huff_tree_index_tensor = wi.getHuffTreeIndex();
		int[][] type_huffman_tree_tensor = root.ToTensor();
		JSONArray type_huff_leaf_encode_json = JSONArray.fromObject(type_huffman_leaf_node_encode_tensor);
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_leaf_encode.json"),
				type_huff_leaf_encode_json.toString());
		JSONArray type_huff_leaf_huff_tree_index_json = JSONArray
				.fromObject(type_huffman_leaf_node_huff_tree_index_tensor);
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_leaf_huff_tree_index.json"),
				type_huff_leaf_huff_tree_index_json.toString());
		JSONArray type_huff_tree_json = JSONArray.fromObject(type_huffman_tree_tensor);
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_tree.json"), type_huff_tree_json.toString());
	}

	public void SaveToDirectory(String dir) {
		GenerateIDJson(dir, ast_type_id_map, "type");
		JSONArray ast_type_is_leaf_json = JSONArray.fromObject(ast_type_is_leaf);
		FileUtil.WriteToFile(new File(dir + "/" + "All_type_is_leaf.json"), ast_type_is_leaf_json.toString());
		GenerateIDJson(dir, ast_content_id_map, "content");
		
		GenerateHuffTree(dir, ast_type_id_count_map, "type");
		GenerateHuffTree(dir, ast_content_id_count_map, "content");
		
//		Set<String> akeys = ati.keySet();
//		for (String ak : akeys) {
//			Integer tcid = ati.get(ak);
//			TreeMap<String, Integer> tci = ast_type_content_id_map.get(tcid);
//			summary.add(tci.size());
//			Map<Object, Object> tci_out = MapUtil.ReverseKeyValueInMap(tci);
//			Set<Object> tci_keys = tci_out.keySet();
//			List<Object> tci_objs = new LinkedList<Object>();
//			Iterator<Object> tci_key_itr = tci_keys.iterator();
//			while (tci_key_itr.hasNext()) {
//				Object key = tci_key_itr.next();
//				tci_objs.add(tci_out.get(key));
//			}
//			JSONArray type_content_id_json = JSONArray.fromObject(tci_objs);
//			FileUtil.WriteToFile(new File(dir + "/" + ak + "_content_id.json"), type_content_id_json.toString());
//		}
//		FileUtil.WriteToFile(new File(dir + "/" + "summary.txt"), StringUtils.join(summary, " "));
//		System.out.println("=== ast_type_id_count_map:" + ast_type_id_count_map);
//
//		List<int[][]> type_content_huff_leaf_encode_list = new ArrayList<int[][]>();
//		List<int[]> type_content_huff_leaf_huff_tree_index_list = new ArrayList<int[]>();
//		List<int[][]> type_content_huff_tree_list = new ArrayList<int[][]>();
//		Set<Integer> atckeys = ast_type_content_id_count_map.keySet();
//		Iterator<Integer> atcitr = atckeys.iterator();
//		while (atcitr.hasNext()) {
//			Integer atckey = atcitr.next();
//			TreeMap<Integer, Integer> atc = ast_type_content_id_count_map.get(atckey);
//			HuffmanNode act_root = GenerateHuffmanTree.BuildTree(atc);
//			WordInfo at_wi = GenerateHuffmanTree.BuildEncodeTensor(act_root);
//			int[][] act_type_huffman_leaf_node_encode_tensor = at_wi.getEncode();
//			type_content_huff_leaf_encode_list.add(atckey, act_type_huffman_leaf_node_encode_tensor);
//			int[] act_type_huffman_leaf_node_huff_tree_index_tensor = at_wi.getHuffTreeIndex();
//			type_content_huff_leaf_huff_tree_index_list.add(atckey, act_type_huffman_leaf_node_huff_tree_index_tensor);
//			int[][] act_type_huffman_tree_tensor = act_root.ToTensor();
//			type_content_huff_tree_list.add(atckey, act_type_huffman_tree_tensor);
//		}
//		JSONArray type_content_huff_leaf_encode_list_json = JSONArray.fromObject(type_content_huff_leaf_encode_list);
//		FileUtil.WriteToFile(new File(dir + "/" + "All_type_content_huff_leaf_encode.json"),
//				type_content_huff_leaf_encode_list_json.toString());
//		JSONArray type_content_huff_leaf_huff_tree_index_list_json = JSONArray
//				.fromObject(type_content_huff_leaf_huff_tree_index_list);
//		FileUtil.WriteToFile(new File(dir + "/" + "All_type_content_huff_leaf_huff_tree_index.json"),
//				type_content_huff_leaf_huff_tree_index_list_json.toString());
//		JSONArray type_content_huff_tree_list_json = JSONArray.fromObject(type_content_huff_tree_list);
//		FileUtil.WriteToFile(new File(dir + "/" + "All_type_content_huff_tree.json"),
//				type_content_huff_tree_list_json.toString());
	}

}
