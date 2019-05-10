package statistic.id;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.Block;

import com.google.gson.Gson;

import huffman.GenerateHuffmanTreeTensor;
import huffman.WordInfo;
import main.MetaOfApp;
import util.FileUtil;
import util.MapUtil;

public class IDManager {
	
	public static String DefaultPart = "@Default";
	public static String LeafType = "LeafDefault";
//	public static String InitialLeaf = "InitialLeafASTType";
//	public static String TerminalLeaf = "TerminalLeafASTType";
	
	
	public static String LeafTypeDefault = LeafType + "#" + DefaultPart;
//	public static String LeafTypeDefault = "LeafDefault" + "#" + DefaultPart;

	public static String RootDefault = DefaultPart + "#" + DefaultPart;
	// leaf type
//	public static String InitialLeafASTType = InitialLeaf + "#" + DefaultPart;
	// leaf type
//	public static String TerminalLeafASTType = TerminalLeaf + "#" + DefaultPart;
	// leaf value
	// public static String SimpleNameLeafDefault = "SNDefault";
	// public static String NumberLiteralLeafDefault = "100";
	// public static String CharacterLiteralLeafDefault = '~' + "";
	// public static String StringLiteralLeafDefault = "\"@str!\"";
	// public static String NullLiteralLeafDefault = "null";
	// public static String TerminalLeafDefault = "TermDefault";
	
	private TreeMap<String, Integer> ast_type_content_id_map = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> ast_type_id_map = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> ast_type_content_type_id_map = new TreeMap<String, Integer>();
	private TreeMap<Integer, Integer> ast_type_content_id_type_id_map = new TreeMap<Integer, Integer>();
	private TreeMap<Integer, Integer> ast_type_content_id_count_map = new TreeMap<Integer, Integer>();
	private ArrayList<Integer> ast_type_content_is_leaf = new ArrayList<Integer>();
	
	// only for type id
	int type_id = 0;
	
//	private Set<String> not_hit_words = new TreeSet<String>();
	private TreeMap<String, Integer> not_hit_ast_type_content_id_map = new TreeMap<String, Integer>();
	
	// this does not need trim and should be separated.
//	private TreeMap<String, Boolean> ast_type_is_leaf = new TreeMap<String, Boolean>();
	
//	{
//		ast_type_is_leaf.put(DefaultPart, false);
//		ast_type_is_leaf.put(Block.class.getSimpleName(), false);
//		ast_type_is_leaf.put("InitialLeafASTType", true);
//		ast_type_is_leaf.put("TerminalLeafASTType", true);
//		ast_type_is_leaf.put("NonLeafDefault", false);
//		ast_type_is_leaf.put("LeafDefault", true);
//	}
	
//	private TreeMap<String, Integer> ast_type_id_map = new TreeMap<String, Integer>();
//	private TreeMap<Integer, Integer> ast_type_id_count_map = new TreeMap<Integer, Integer>();
//	private ArrayList<Integer> ast_type_is_leaf = new ArrayList<Integer>()
	
//	private TreeMap<String, Integer> ast_content_id_map = new TreeMap<String, Integer>();
//	private TreeMap<Integer, Integer> ast_content_id_count_map = new TreeMap<Integer, Integer>();

	// private TreeMap<Integer, TreeMap<String, Integer>> ast_type_content_id_map =
	// new TreeMap<Integer, TreeMap<String, Integer>>();
	// private TreeMap<Integer, TreeMap<Integer, Integer>>
	// ast_type_content_id_count_map = new TreeMap<Integer, TreeMap<Integer,
	// Integer>>();

	public IDManager() {
		RegistTypeContentID(LeafTypeDefault, true, 0);
		RegistTypeContentID(RootDefault, false, 0);
		// non leaf
		RegistTypeContentID(Block.class.getSimpleName() + "#" + DefaultPart, false, 0);
		// leaf
//		RegistTypeContentID(InitialLeafASTType, true, 0);
		// leaf
//		RegistTypeContentID(TerminalLeafASTType, true, 0);
		// leaf default
		// initialize type id info
		ast_type_id_map.put(LeafType, type_id++);
		ast_type_id_map.put(DefaultPart, type_id++);
//		ast_type_content_type_id_map.put(InitialLeaf, id++);
//		ast_type_content_type_id_map.put(TerminalLeaf, id++);
		ast_type_id_map.put(Block.class.getSimpleName(), type_id++);
	}
	
	public int RegistTypeContentID(String type_content, boolean is_leaf, int count) {
		Integer id = ast_type_content_id_map.get(type_content);
		if (id == null) {
			id = ast_type_content_id_map.size();
			ast_type_content_id_map.put(type_content, id);
			ast_type_content_is_leaf.add(id, (is_leaf ? 1 : 0));
		}
		Integer ct = ast_type_content_id_count_map.get(id);
		if (ct == null) {
			ct = 0;
		}
		ct += count;
		ast_type_content_id_count_map.put(id, ct);
		return id;
	}
	
	public void GenerateTypeSummary() {
		Set<String> to_keys = ast_type_content_id_map.keySet();
		Iterator<String> tk_itr = to_keys.iterator();
		while (tk_itr.hasNext()) {
			String type_content = tk_itr.next();
			int pos = type_content.indexOf('#');
			String type = type_content.substring(0, pos);
			Integer t_id = ast_type_id_map.get(type);
			if (t_id == null) {
				t_id = type_id++;
				ast_type_id_map.put(type, t_id);
			}
			ast_type_content_type_id_map.put(type_content, t_id);
			ast_type_content_id_type_id_map.put(ast_type_content_id_map.get(type_content), t_id);
		}
	}
	
	public int GetTypeNum() {
		return type_id;
	}
	
//	public void RegistTypeIsLeaf(TreeMap<String, Boolean> ast_type_is_leaf) {
//		this.ast_type_is_leaf.putAll(ast_type_is_leaf);
//	}

//	public int RegistTypeID(String type, boolean is_leaf, int count) {
//		Integer id = ast_type_id_map.get(type);
//		if (id == null) {
//			id = ast_type_id_map.size();
//			ast_type_id_map.put(type, id);
//			ast_type_is_leaf.add(id, (is_leaf ? 1 : 0));
//		}
//		Integer ct = ast_type_id_count_map.get(id);
//		if (ct == null) {
//			ct = 0;
//		}
//		ct += count;
//		ast_type_id_count_map.put(id, ct);
//		return id;
//	}
//
//	public int RegistContentID(String content, int count) {
//		Integer id = ast_content_id_map.get(content);
//		if (id == null) {
//			id = ast_content_id_map.size();
//			ast_content_id_map.put(content, id);
//		}
//		Integer ct = ast_content_id_count_map.get(id);
//		if (ct == null) {
//			ct = 0;
//		}
//		ct += count;
//		ast_content_id_count_map.put(id, ct);
//		return id;
//	}

//	public void EnsureDefaultValue() {
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
//	}
	
	public int GetTypeContentID(String type_content) {
		type_content = PreProcessContentHelper.PreProcessTypeContent(type_content);
		Integer id = ast_type_content_id_map.get(type_content);
		if (id != null) {
			return id;
		}
//		not_hit_words.add(type_content);
//		int pos = type_content.indexOf('#');
//		String type = type_content.substring(0, pos);
//		String type_with_default = type + "#" + DefaultPart;
//		id = ast_type_content_id_map.get(type_with_default);
//		if (id != null) {
//			return id;
//		}
//		System.out.println("Using LeafTypeDefault:" + type_content);
		return RegistNotHitTypeContentID(type_content);
	}
	
	private int RegistNotHitTypeContentID(String type_content) {
		Integer id = not_hit_ast_type_content_id_map.get(type_content);
		if (id == null) {
			id = ast_type_content_id_map.size() + not_hit_ast_type_content_id_map.size();
			not_hit_ast_type_content_id_map.put(type_content, id);
		}
		return id;
	}
	
//	public int GetTypeID(String type_content) {
//		Integer id = ast_type_content_type_id_map.get(type_content);
//		if (id != null) {
//			return id;
//		}
//		return ast_type_content_type_id_map.get(LeafTypeDefault);
//	}

//	public int GetTypeID(String type) {
//		Integer id = ast_type_id_map.get(type);
//		if (id != null) {
//			return id;
//		}
//		return 0;
//	}
//
//	public int GetContentID(String content) {
//		content = PreProcessContentHelper.PreProcessContent(content);
//		Integer id = ast_content_id_map.get(content);
//		if (id != null) {
//			return id;
//		}
//		return 0;
//	}

	private void GenerateIDJson(String dir, TreeMap<String, Integer> to_gen, String desc) {
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
	
	private void GenerateTypeAndSummaryJson(String dir, TreeMap<String, Integer> to_gen, String desc) {
		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_type_content_type_id.json"), gson.toJson(ast_type_content_type_id_map));
		Gson gson_r = new Gson();
		ArrayList<Integer> tcid_tid = new ArrayList<Integer>();
		Set<Integer> rg_keys = ast_type_content_id_type_id_map.keySet();
		Iterator<Integer> rg_itr = rg_keys.iterator();
		while (rg_itr.hasNext()) {
			Integer key = rg_itr.next();
			Assert.isTrue(key.equals(tcid_tid.size()));
			tcid_tid.add(ast_type_content_id_type_id_map.get(key));
		}
		FileUtil.WriteToFile(new File(dir + "/" + "All_type_content_id_" + desc + "_id.json"), gson_r.toJson(tcid_tid));
		Gson gson_tid = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_id.json"), gson_tid.toJson(ast_type_id_map));
		Gson gson_summary = new Gson();
		TreeMap<String, Integer> summary = new TreeMap<String, Integer>();
		summary.put("TypeNum", type_id);
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_summary.json"), gson_summary.toJson(summary));
	}
	
	private void GenerateAndSaveHuffTree(String dir, TreeMap<Integer, Integer> count_map, String desc, int huff_tree_standard_children_num) {
		GenerateHuffmanTreeTensor gen = new GenerateHuffmanTreeTensor(huff_tree_standard_children_num, count_map);
		WordInfo wi = gen.GetWordInfo();
//		HuffmanNode root = GenerateHuffmanTreeTensor.BuildTree(count_map);
//		WordInfo wi = GenerateHuffmanTreeTensor.BuildEncodeTensor(root);
		int[][] huffman_leaf_node_encode_direction_tensor = wi.getEncodeDirection();
		int[][] huffman_leaf_node_encode_state_tensor = wi.getEncodeState();
		int[][][] huffman_tree_tensor = gen.GetHuffTreeTensor();// root.ToTensor();
		int[] huffman_non_leaf_node_valid_children_num_tensor = gen.GetHuffTreeValidChildrenNumTensor();
		String huff_tree_summary = "StandardChildrenNum:" + gen.GetStandardChildrenNum() + "\n" + "MaximumChildrenNum:" + gen.GetMaximumChildrenNum() + "\n" + "MinimumChildrenNum:" + gen.GetMinimumChildrenNum() + "\n" + "MaximumDepth:" + gen.GetMaximumDepth();
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_tree_summary.txt"), huff_tree_summary);
		Gson gson = new Gson();
//		JSONArray type_huff_leaf_encode_direction_json = JSONArray.fromObject(huffman_leaf_node_encode_direction_tensor);
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_leaf_encode_direction.json"),
				gson.toJson(huffman_leaf_node_encode_direction_tensor));// type_huff_leaf_encode_direction_json.toString()
		Gson gson2 = new Gson();
//		JSONArray type_huff_leaf_encode_state_json = JSONArray.fromObject(huffman_leaf_node_encode_state_tensor);
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_leaf_encode_state.json"),
				gson2.toJson(huffman_leaf_node_encode_state_tensor));// type_huff_leaf_encode_state_json.toString()
		Gson gson3 = new Gson();
//		JSONArray type_huff_non_leaf_valid_children_num_json = JSONArray.fromObject(huffman_non_leaf_node_valid_children_num_tensor);
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_non_leaf_valid_children_num.json"), gson3.toJson(huffman_non_leaf_node_valid_children_num_tensor));// type_huff_non_leaf_valid_children_num_json.toString()
//		JSONArray type_huff_leaf_huff_tree_index_json = JSONArray
//				.fromObject(type_huffman_leaf_node_huff_tree_index_tensor);
//		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_leaf_huff_tree_index.json"),
//				type_huff_leaf_huff_tree_index_json.toString());
		Gson gson4 = new Gson();
//		JSONArray type_huff_tree_json = JSONArray.fromObject(huffman_tree_tensor);
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_tree.json"), gson4.toJson(huffman_tree_tensor));// type_huff_tree_json.toString()
	}
	
	private void GenerateAndSaveCharSequence(String dir, TreeMap<String, Integer> hit, TreeMap<String, Integer> not_hit, String desc) {
		TreeMap<String, Integer> to_gen = new TreeMap<String, Integer>();
		to_gen.putAll(hit);
//		PrintUtil.PrintMap(hit);
		to_gen.putAll(not_hit);
//		PrintUtil.PrintMap(not_hit);
		Set<Character> c_set = new TreeSet<Character>();
		int max_length = 0;
		Map<Integer, String> ati_out = MapUtil.ReverseKeyValueInMap(to_gen);
		Collection<String> ao = ati_out.values();
		Iterator<String> aitr = ao.iterator();
		while (aitr.hasNext()) {
			String tc = aitr.next();
			if (max_length < tc.length()) {
				max_length = tc.length();
			}
			int tc_len = tc.length();
			for (int i=0;i<tc_len;i++) {
				char c = tc.charAt(i);
				c_set.add(c);
			}
		}
		int index = 0;
		Map<Character, Integer> char_idx = new HashMap<Character, Integer>();
		Iterator<Character> c_itr = c_set.iterator();
		while (c_itr.hasNext()) {
			Character c = c_itr.next();
			char_idx.put(c, index);
			index++;
		}
		int[][] char_seq = new int[ati_out.size()][max_length];
		Set<Integer> keys = ati_out.keySet();
		Set<Integer> sort_keys = new TreeSet<Integer>((o1, o2) -> o1.compareTo(o2));
		sort_keys.addAll(keys);
		Iterator<Integer> kitr = sort_keys.iterator();
		int k_idx = -1;
		while (kitr.hasNext()) {
			k_idx++;
			Integer k = kitr.next();
//			System.out.println("k:" + k);
			Assert.isTrue(k_idx == k, "k_idx:" + k_idx + "#k:" + k);
			String tc = ati_out.get(k);
			String cnt = tc.substring(tc.indexOf('#')+1, tc.length());
			if (cnt.equals(DefaultPart)) {
				cnt = tc.substring(0, tc.indexOf('#'));
			}
			int tc_len = cnt.length();
			for (int i=0;i<tc_len;i++) {
				char c = cnt.charAt(i);
				int idx = char_idx.get(c);
				char_seq[k][i] = idx;
			}
			for (int i=tc_len;i<max_length;i++) {
				char_seq[k][i] = -1;
			}
		}
		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_char_sequence.json"),
				gson.toJson(char_seq));
		String char_seq_meta = "HitNumber:" + hit.size() + "\n" + "MaxCharSequenceLength:" + max_length + "\n" + "TotalNumberOfChar:" + char_idx.size();
		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_char_sequence_summary.txt"), char_seq_meta);
	}

	public void SaveToDirectory(String dir) {
//		GenerateIDJson(dir, ast_type_content_id_map, "type");
//		Gson gson = new Gson();
////		JSONArray ast_type_is_leaf_json = JSONArray.fromObject(ast_type_is_leaf);
//		FileUtil.WriteToFile(new File(dir + "/" + "All_type_is_leaf.json"), gson.toJson(MapUtil.ListToIndexMap(ast_type_is_leaf)));// ast_type_is_leaf_json.toString()
//		GenerateIDJson(dir, ast_content_id_map, "content");
//		
//		GenerateAndSaveHuffTree(dir, ast_type_id_count_map, "type", MetaOfApp.TypeHuffTreeStandardChildrenNum);
//		GenerateAndSaveHuffTree(dir, ast_content_id_count_map, "content", MetaOfApp.ContentHuffTreeStandardChildrenNum);
		
		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_type_content_is_leaf.json"), gson.toJson(MapUtil.ListToIndexMap(ast_type_content_is_leaf)));
		GenerateIDJson(dir, not_hit_ast_type_content_id_map, "not_hit_type_content");
		GenerateAndSaveCharSequence(dir, ast_type_content_id_map, not_hit_ast_type_content_id_map, "type_content");
		GenerateIDJson(dir, ast_type_content_id_map, "type_content");
		GenerateTypeAndSummaryJson(dir, ast_type_content_id_map, "type");
		GenerateAndSaveHuffTree(dir, ast_type_content_id_count_map, "type_content", MetaOfApp.TypeContentHuffTreeStandardChildrenNum);
		
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
	
	public String WordVocabularyInfo() {
		return "Summary -- Vocabulary_Word_Size:" + ast_type_content_id_map.size() + "#OutOfVocabulary_Word_Size:" + not_hit_ast_type_content_id_map.size();
	}

}
