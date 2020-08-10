package statistic.id;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;

import com.google.gson.Gson;

import bpe.BPEHandledResult;
import bpe.BPEWordsUtil;
import main.MetaOfApp;
import statistic.IDTools;
import util.FileUtil;
import util.MapUtil;
import util.PrintUtil;

public class IDManager {

	public static final String Leaf = "L";
	public static final String NonLeaf = "N";

	public static final String ZDft = "$Dft_";
	public static final String Unk = "$Unk_";
	public static final String Rep = "$Rep_";

	public static final String SWordUNK = "$YYXSWordUNK_";

	public static final List<String> reserved_words = new LinkedList<String>();

	static {
		reserved_words.add(ZDft);
		reserved_words.add(Unk);
		reserved_words.add(Rep);
	}

//	public static String DefaultPart = "@Default";
//	public static String LeafType = "LeafDefault";
//	public static String InitialLeaf = "InitialLeafASTType";
//	public static String TerminalLeaf = "TerminalLeafASTType";

//	public static String LeafTypeDefault = LeafType + "#" + DefaultPart;
//	public static String LeafTypeDefault = "LeafDefault" + "#" + DefaultPart;

//	public static String RootDefault = DefaultPart + "#" + DefaultPart;
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
	
	private TreeMap<String, Integer> each_skeleton_id_map = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> pe_skeleton_id_map = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> skeleton_id_map = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> skt_token_id_map = new TreeMap<String, Integer>();
//	private TreeMap<String, Integer> skeleton_token_id_map = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> token_id_map = new TreeMap<String, Integer>();
//	private int grammar_token_num = -1;
//	private int token_hit_num = -1;
	private TreeMap<String, Integer> grammar_id_map = new TreeMap<String, Integer>();
	private TreeMap<Integer, TreeSet<Integer>> grammar_id_token_id_map = new TreeMap<Integer, TreeSet<Integer>>();
	
	private int each_skeleton_hit_num = -1;
	private int pe_skeleton_hit_num = -1;
	private int skeleton_hit_num = -1;
	private int skt_token_hit_num = -1;
	private int token_hit_num = -1;

//	private TreeMap<String, Integer> ast_type_content_id_map = new TreeMap<String, Integer>();
//	private TreeMap<String, Integer> not_hit_ast_type_content_id_map = new TreeMap<String, Integer>();
//	private TreeMap<String, Integer> ast_type_id_map = new TreeMap<String, Integer>();
//	private TreeMap<String, Integer> ast_type_content_type_id_map = new TreeMap<String, Integer>();
//	private TreeMap<Integer, Integer> ast_type_content_id_type_id_map = new TreeMap<Integer, Integer>();
//	private TreeMap<Integer, Integer> ast_type_content_id_count_map = new TreeMap<Integer, Integer>();
//	private ArrayList<Integer> ast_type_content_is_leaf = new ArrayList<Integer>();

	// only for type id
//	int type_id = 0;

//	private Set<String> not_hit_words = new TreeSet<String>();

	private TreeMap<String, Integer> api_comb_id_map = new TreeMap<String, Integer>();
//	private int api_comb_hit_num = -1;

	private int char_num = -1;
	private int subword_num = -1;

//	private TreeMap<String, Integer> not_hit_api_comb_id_map = new TreeMap<String, Integer>();

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

	IDTools id_tool = null;

	public IDManager(IDTools id_tool) {
		this.id_tool = id_tool;
//		token_id_map.putAll(id_tool.gr.node_type_id);
//		grammar_token_num = token_id_map.size();
		// skeleton regist
//		if (MetaOfApp.TakeUnseenAsUnk) {
//			Regist(skeleton_id_map, new ArrayList<String>(id_tool.sr.RefineHitTrain((int)Math.ceil(MetaOfApp.NumberOfUnk*1.0 / 10.0))));
//		} else 
//		if (MetaOfApp.GeneratePairEncodedSkeletonToken) {
//			Regist(pair_encoded_skeleton_id_map, reserved_words);
//			pair_encoded_skeleton_hit_num = RegistUtil(pair_encoded_skeleton_id_map, id_tool.str.hit_train, id_tool.str.not_hit_train, MetaOfApp.NumberOfSkeletonUnk, MetaOfApp.MinimumNumberOfSkeletonVocabulary, "PairEncodedSkeleton");
//		}
		if (MetaOfApp.GenerateSkeletonToken) {
			Regist(each_skeleton_id_map, reserved_words);
			each_skeleton_hit_num = RegistUtil(each_skeleton_id_map, id_tool.e_struct_r.hit_train, id_tool.e_struct_r.not_hit_train, MetaOfApp.NumberOfSkeletonUnk, MetaOfApp.MinimumNumberOfSkeletonVocabulary, "SkeletonEach");
			
			Regist(pe_skeleton_id_map, reserved_words);
			pe_skeleton_hit_num = RegistUtil(pe_skeleton_id_map, id_tool.pe_struct_r.hit_train, id_tool.pe_struct_r.not_hit_train, MetaOfApp.NumberOfSkeletonUnk, MetaOfApp.MinimumNumberOfSkeletonVocabulary, "SkeletonPE");
			
			Regist(skeleton_id_map, reserved_words);
			skeleton_hit_num = RegistUtil(skeleton_id_map, id_tool.one_struct_r.hit_train, id_tool.one_struct_r.not_hit_train, MetaOfApp.NumberOfSkeletonUnk, MetaOfApp.MinimumNumberOfSkeletonVocabulary, "Skeleton");
			
			Regist(skt_token_id_map, reserved_words);
			skt_token_hit_num = RegistUtil(skt_token_id_map, id_tool.s_tr.hit_train, id_tool.s_tr.not_hit_train, MetaOfApp.NumberOfSkeletonUnk, MetaOfApp.MinimumNumberOfSkeletonVocabulary, "SkeletonToken");
		}
//		Regist(skeleton_id_map, new ArrayList<String>(id_tool.sr.hit_train.entrkeySet()));
//		Regist(skeleton_id_map, new ArrayList<String>(id_tool.sr.not_hit_train.keySet()));
//		Regist(skeleton_token_id_map, new ArrayList<String>(id_tool.str.hit_train.keySet()));
//		Regist(skeleton_token_id_map, new ArrayList<String>(id_tool.str.not_hit_train.keySet()));

//		PrintUtil.PrintSet(id_tool.tr.hit_train.keySet(), "id_tool.tr.hit_train.keySet()" + ";size:" + id_tool.tr.hit_train.keySet().size());
//		PrintUtil.PrintSet(id_tool.str.hit_train.keySet(), "id_tool.str.hit_train.keySet()" + ";size:" + id_tool.str.hit_train.keySet().size());

//		Set<String> g_set = id_tool.gr.self_children_map.keySet();
//		for (String g : g_set) {
//			Assert.isTrue(id_tool.gr.fixed_tokens.contains(g));
//		}
		// token regist
//		if (MetaOfApp.TakeUnseenAsUnk) {
//			Regist(token_id_map, new ArrayList<String>(id_tool.tr.RefineHitTrain(MetaOfApp.NumberOfUnk)));
//		} else 
		{
			Regist(token_id_map, reserved_words);
			token_hit_num = RegistUtil(token_id_map, id_tool.tr.hit_train, id_tool.tr.not_hit_train, MetaOfApp.NumberOfUnk, MetaOfApp.MinimumNumberOfVocabulary, "Token");
//			ArrayList<Entry<String, Integer>> tk_ht = new ArrayList<Entry<String, Integer>>(
//					MapUtil.SortMapByValue(id_tool.tr.hit_train));
//			Collections.reverse(tk_ht);
//			Regist(token_id_map, MapUtil.EntryListToKeyList(tk_ht));
//			token_hit_num = token_id_map.size() - MetaOfApp.NumberOfUnk;
//			if (token_hit_num < MetaOfApp.MinimumNumberOfVocabulary) {
//				token_hit_num = MetaOfApp.MinimumNumberOfVocabulary;
//			}
//			if (token_hit_num > token_id_map.size()) {
//				token_hit_num = token_id_map.size();
//			}
//			PrintUtil.PrintPartOfEntryList(tk_ht, token_hit_num, token_id_map.size(), "SetUnkTokens", "token", "count");
//			ArrayList<Entry<String, Integer>> tk_nht = new ArrayList<Entry<String, Integer>>(
//					MapUtil.SortMapByValue(id_tool.tr.not_hit_train));
//			Collections.reverse(tk_nht);
//			Regist(token_id_map, MapUtil.EntryListToKeyList(tk_nht));
		}
//		Regist(token_id_map, new ArrayList<String>(id_tool.tr.hit_train.keySet()));
//		Regist(token_id_map, new ArrayList<String>(g_set));
//		Regist(token_id_map, new ArrayList<String>(id_tool.gr.fixed_tokens));
//		Regist(token_id_map, new ArrayList<String>(id_tool.gr.unfixed_tokens));

//		token_hit_num = token_id_map.size();
//		Assert.isTrue(token_hit_num > 0);
//		Regist(token_id_map, id_tool.tr.not_hit_train);
		{
			Regist(grammar_id_map, reserved_words);
			Regist(grammar_id_map, new ArrayList<String>(id_tool.gr.self_children_map.keySet()));
			{
				TreeSet<Integer> children_ids = new TreeSet<Integer>();
				for (int i = 0; i < token_hit_num; i++) {
					children_ids.add(i);
				}
				grammar_id_token_id_map.put(grammar_id_map.get(Unk), children_ids);
			}
			Map<String, TreeSet<String>> sc_map = id_tool.gr.self_children_map;
			Set<String> grammars = sc_map.keySet();
			for (String grammar : grammars) {
				TreeSet<String> children = sc_map.get(grammar);
				int g_id = grammar_id_map.get(grammar);
				TreeSet<Integer> children_ids = new TreeSet<Integer>();
				grammar_id_token_id_map.put(g_id, children_ids);
				for (String child : children) {
					int t_id = token_id_map.get(child);
					int r_id = t_id;
					if (t_id >= token_hit_num) {
						r_id = token_id_map.get(Unk);
					}
					children_ids.add(r_id);
				}
			}
		}
		// api comb regist
//		Regist(api_comb_id_map, new ArrayList<String>(id_tool.ar.api_combs));

//		id_tool.gr.ProcessNodeRelativeIndexInGrammar();

//		api_comb_hit_num = api_comb_id_map.size();
//		Assert.isTrue(api_comb_hit_num > 0);

//		Regist(api_comb_id_map, id_tool.ar.not_hit_train);

//		RegistTypeContentID(LeafTypeDefault, true, 0);
//		RegistTypeContentID(RootDefault, false, 0);
		// non leaf
//		RegistTypeContentID(Block.class.getSimpleName() + "#" + DefaultPart, false, 0);
		// leaf
//		RegistTypeContentID(InitialLeafASTType, true, 0);
		// leaf
//		RegistTypeContentID(TerminalLeafASTType, true, 0);
		// leaf default
		// initialize type id info
//		ast_type_id_map.put(LeafType, type_id++);
//		ast_type_id_map.put(DefaultPart, type_id++);
//		ast_type_content_type_id_map.put(InitialLeaf, id++);
//		ast_type_content_type_id_map.put(TerminalLeaf, id++);
//		ast_type_id_map.put(Block.class.getSimpleName(), type_id++);
	}
	
	private static int RegistUtil(TreeMap<String, Integer> id_map, Map<String, Integer> hit, Map<String, Integer> not_hit, int unk_num, int minimum_num, String desc) {
		ArrayList<Entry<String, Integer>> sk_ht = new ArrayList<Entry<String, Integer>>(
				MapUtil.SortMapByValue(hit));
		Collections.reverse(sk_ht);
		Regist(id_map, MapUtil.EntryListToKeyList(sk_ht));
		int hit_num = id_map.size() - unk_num;
		if (hit_num < minimum_num) {
			hit_num = minimum_num;
		}
		if (hit_num > id_map.size()) {
			hit_num = id_map.size();
		}
		PrintUtil.PrintPartOfEntryList(sk_ht, hit_num, id_map.size(), "SetUnk" + desc,
				desc, "count");
		ArrayList<Entry<String, Integer>> sk_nht = new ArrayList<Entry<String, Integer>>(
				MapUtil.SortMapByValue(not_hit));
		Collections.reverse(sk_nht);
		Regist(id_map, MapUtil.EntryListToKeyList(sk_nht));
		return hit_num;
	}

	private static void Regist(Map<String, Integer> reg_map, List<String> ele_set) {
		Iterator<String> ele_itr = ele_set.iterator();
		while (ele_itr.hasNext()) {
			String ele = ele_itr.next();
			if (!reg_map.containsKey(ele)) {
				reg_map.put(ele, reg_map.size());
			}
		}
	}

//	private int RegistTypeContentID(String type_content) {// , boolean is_leaf, int count
//		Integer id = ast_type_content_id_map.get(type_content);
//		if (id == null) {
//			id = ast_type_content_id_map.size();
//			ast_type_content_id_map.put(type_content, id);
////			ast_type_content_is_leaf.add(id, (is_leaf ? 1 : 0));
//		}
////		Integer ct = ast_type_content_id_count_map.get(id);
////		if (ct == null) {
////			ct = 0;
////		}
////		ct += count;
////		ast_type_content_id_count_map.put(id, ct);
//		return id;
//	}

//	public void GenerateTypeSummary() {
//		Set<String> to_keys = ast_type_content_id_map.keySet();
//		Iterator<String> tk_itr = to_keys.iterator();
//		while (tk_itr.hasNext()) {
//			String type_content = tk_itr.next();
//			int pos = type_content.indexOf('#');
//			String type = type_content.substring(0, pos);
//			Integer t_id = ast_type_id_map.get(type);
//			if (t_id == null) {
//				t_id = type_id++;
//				ast_type_id_map.put(type, t_id);
//			}
//			ast_type_content_type_id_map.put(type_content, t_id);
//			ast_type_content_id_type_id_map.put(ast_type_content_id_map.get(type_content), t_id);
//		}
//	}

//	public int GetTypeNum() {
//		return type_id;
//	}

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
	
	public int GetEachSkeletonID(String skeleton) {
		Integer id = each_skeleton_id_map.get(skeleton);
		Assert.isTrue(id != null, "unseen skeleton:" + skeleton);
		if (MetaOfApp.OutOfScopeReplacedByUnk && id >= each_skeleton_hit_num) {
			id = each_skeleton_id_map.get(Unk);
		}
//		if (id == null) {
//			System.out.println("==== Unk type_content: " + type_content + " ====");
//			return skeleton_id_map.get(Unk);
//		}
		return id + MetaOfApp.SkeletonIDBase;
	}
	
	public int GetPESkeletonID(String skeleton) {
		Integer id = pe_skeleton_id_map.get(skeleton);
		Assert.isTrue(id != null, "unseen skeleton:" + skeleton);
		if (MetaOfApp.OutOfScopeReplacedByUnk && id >= pe_skeleton_hit_num) {
			id = pe_skeleton_id_map.get(Unk);
		}
//		if (id == null) {
//			System.out.println("==== Unk type_content: " + type_content + " ====");
//			return skeleton_id_map.get(Unk);
//		}
		return id + MetaOfApp.SkeletonIDBase;
	}

	public int GetSkeletonID(String skeleton) {
		Integer id = skeleton_id_map.get(skeleton);
		Assert.isTrue(id != null, "unseen skeleton:" + skeleton);
		if (MetaOfApp.OutOfScopeReplacedByUnk && id >= skeleton_hit_num) {
			id = skeleton_id_map.get(Unk);
		}
//		if (id == null) {
//			System.out.println("==== Unk type_content: " + type_content + " ====");
//			return skeleton_id_map.get(Unk);
//		}
		return id + MetaOfApp.SkeletonIDBase;
	}
	
	public int GetSkeletonTypeContentID(String type_content) {
		Integer id = skt_token_id_map.get(type_content);
		Assert.isTrue(id != null, "unseen type_content:" + type_content);
		if (MetaOfApp.OutOfScopeReplacedByUnk && id >= skt_token_hit_num) {
			id = skt_token_id_map.get(Unk);
		}
		return id;
	}

//	public int GetSkeletonTypeContentID(String type_content) {
//		Integer id = skeleton_token_id_map.get(type_content);
//		Assert.isTrue(id != null, "unseen skeleton type_content:" + type_content);
//		return id;
//	}

	public int GetTypeContentID(String type_content) {
		Integer id = token_id_map.get(type_content);
		Assert.isTrue(id != null, "unseen type_content:" + type_content);
		if (MetaOfApp.OutOfScopeReplacedByUnk && id >= token_hit_num) {
//			System.err.println("id:"+id+"#token_hit_num:"+token_hit_num);
			id = token_id_map.get(Unk);
		}
//		if (id == null) {
//			System.out.println("==== Unk type_content: " + type_content + " ====");
//			return token_id_map.get(Unk);
//		}
		return id;
//		not_hit_words.add(type_content);
//		int pos = type_content.indexOf('#');
//		String type = type_content.substring(0, pos);
//		String type_with_default = type + "#" + DefaultPart;
//		id = ast_type_content_id_map.get(type_with_default);
//		if (id != null) {
//			return id;
//		}
//		System.out.println("Using LeafTypeDefault:" + type_content);
//		return RegistNotHitTypeContentID(type_content);
	}

	public int GetGrammarID(String grammar) {
		Integer id = grammar_id_map.get(grammar);
		if (id == null) {
			id = grammar_id_map.get(Unk);
		}
		return id;
	}

	// type_content = PreProcessContentHelper.PreProcessTypeContent(type_content);
//	public int GetAPICombID(String api_comb) {
//		Integer id = api_comb_id_map.get(api_comb);
//		Assert.isTrue(id != null);
//		return id;
//	}

//	private int RegistNotHitTypeContentID(String type_content) {
//		Integer id = not_hit_ast_type_content_id_map.get(type_content);
//		if (id == null) {
//			id = ast_type_content_id_map.size() + not_hit_ast_type_content_id_map.size();
//			not_hit_ast_type_content_id_map.put(type_content, id);
//		}
//		return id;
//	}

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

	private void GenerateIDHitJson(String dir) {
		ArrayList<Integer> id_is_hit = new ArrayList<Integer>();
//		Map<Object, Object> ati_objs = new HashMap<Object, Object>();
		Map<Integer, String> ati_out = MapUtil.ReverseKeyValueInMap(token_id_map);
		Set<Integer> ati_keys = ati_out.keySet();
		Iterator<Integer> ati_itr = ati_keys.iterator();
		int index = 0;
		while (ati_itr.hasNext()) {
			Integer ii = ati_itr.next();
			String tk = ati_out.get(ii);
			boolean is_hit = id_tool.tr.hit_train.containsKey(tk);
			if (!is_hit) {
				Assert.isTrue(id_tool.tr.not_hit_train.containsKey(tk));
			}
			Assert.isTrue(ii == id_is_hit.size() && index == ii);
			id_is_hit.add(is_hit ? 1 : 0);
			index++;
		}
//		Set<Object> ati_keys = ati_out.keySet();
//		Iterator<Object> ati_key_itr = ati_keys.iterator();
//		while (ati_key_itr.hasNext()) {
//			Object key = ati_key_itr.next();
//			ati_objs.put(key, ati_out.get(key));
//		}
		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_is_hit.json"), gson.toJson(id_is_hit));// type_id_json.toString()
	}

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

//	private void GenerateTypeAndSummaryJson(String dir, TreeMap<String, Integer> to_gen, String desc) {
//		Gson gson = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_type_content_type_id.json"), gson.toJson(ast_type_content_type_id_map));
//		Gson gson_r = new Gson();
//		ArrayList<Integer> tcid_tid = new ArrayList<Integer>();
//		Set<Integer> rg_keys = ast_type_content_id_type_id_map.keySet();
//		Iterator<Integer> rg_itr = rg_keys.iterator();
//		while (rg_itr.hasNext()) {
//			Integer key = rg_itr.next();
//			Assert.isTrue(key.equals(tcid_tid.size()));
//			tcid_tid.add(ast_type_content_id_type_id_map.get(key));
//		}
//		FileUtil.WriteToFile(new File(dir + "/" + "All_type_content_id_" + desc + "_id.json"), gson_r.toJson(tcid_tid));
//		Gson gson_tid = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_id.json"), gson_tid.toJson(ast_type_id_map));
//		Gson gson_summary = new Gson();
//		TreeMap<String, Integer> summary = new TreeMap<String, Integer>();
//		summary.put("TypeNum", type_id);
//		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_summary.json"), gson_summary.toJson(summary));
//	}

//	private void GenerateAndSaveHuffTree(String dir, TreeMap<Integer, Integer> count_map, String desc, int huff_tree_standard_children_num) {
//		GenerateHuffmanTreeTensor gen = new GenerateHuffmanTreeTensor(huff_tree_standard_children_num, count_map);
//		WordInfo wi = gen.GetWordInfo();
////		HuffmanNode root = GenerateHuffmanTreeTensor.BuildTree(count_map);
////		WordInfo wi = GenerateHuffmanTreeTensor.BuildEncodeTensor(root);
//		int[][] huffman_leaf_node_encode_direction_tensor = wi.getEncodeDirection();
//		int[][] huffman_leaf_node_encode_state_tensor = wi.getEncodeState();
//		int[][][] huffman_tree_tensor = gen.GetHuffTreeTensor();// root.ToTensor();
//		int[] huffman_non_leaf_node_valid_children_num_tensor = gen.GetHuffTreeValidChildrenNumTensor();
//		String huff_tree_summary = "StandardChildrenNum:" + gen.GetStandardChildrenNum() + "\n" + "MaximumChildrenNum:" + gen.GetMaximumChildrenNum() + "\n" + "MinimumChildrenNum:" + gen.GetMinimumChildrenNum() + "\n" + "MaximumDepth:" + gen.GetMaximumDepth();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_tree_summary.txt"), huff_tree_summary);
//		Gson gson = new Gson();
////		JSONArray type_huff_leaf_encode_direction_json = JSONArray.fromObject(huffman_leaf_node_encode_direction_tensor);
//		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_leaf_encode_direction.json"),
//				gson.toJson(huffman_leaf_node_encode_direction_tensor));// type_huff_leaf_encode_direction_json.toString()
//		Gson gson2 = new Gson();
////		JSONArray type_huff_leaf_encode_state_json = JSONArray.fromObject(huffman_leaf_node_encode_state_tensor);
//		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_leaf_encode_state.json"),
//				gson2.toJson(huffman_leaf_node_encode_state_tensor));// type_huff_leaf_encode_state_json.toString()
//		Gson gson3 = new Gson();
////		JSONArray type_huff_non_leaf_valid_children_num_json = JSONArray.fromObject(huffman_non_leaf_node_valid_children_num_tensor);
//		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_non_leaf_valid_children_num.json"), gson3.toJson(huffman_non_leaf_node_valid_children_num_tensor));// type_huff_non_leaf_valid_children_num_json.toString()
////		JSONArray type_huff_leaf_huff_tree_index_json = JSONArray
////				.fromObject(type_huffman_leaf_node_huff_tree_index_tensor);
////		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_leaf_huff_tree_index.json"),
////				type_huff_leaf_huff_tree_index_json.toString());
//		Gson gson4 = new Gson();
////		JSONArray type_huff_tree_json = JSONArray.fromObject(huffman_tree_tensor);
//		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_huff_tree.json"), gson4.toJson(huffman_tree_tensor));// type_huff_tree_json.toString()
//	}

//	private void GenerateAndSaveCharSequence(String dir, TreeMap<String, Integer> hit, TreeMap<String, Integer> not_hit, String desc) {
//		TreeMap<String, Integer> to_gen = new TreeMap<String, Integer>();
//		to_gen.putAll(hit);
////		PrintUtil.PrintMap(hit);
//		to_gen.putAll(not_hit);
////		PrintUtil.PrintMap(not_hit);
//		Set<Character> c_set = new TreeSet<Character>();
//		int max_length = 0;
//		Map<Integer, String> ati_out = MapUtil.ReverseKeyValueInMap(to_gen);
//		Collection<String> ao = ati_out.values();
//		Iterator<String> aitr = ao.iterator();
//		while (aitr.hasNext()) {
//			String tc = aitr.next();
//			if (max_length < tc.length()) {
//				max_length = tc.length();
//			}
//			int tc_len = tc.length();
//			for (int i=0;i<tc_len;i++) {
//				char c = tc.charAt(i);
//				c_set.add(c);
//			}
//		}
//		int index = 0;
//		Map<Character, Integer> char_idx = new HashMap<Character, Integer>();
//		Iterator<Character> c_itr = c_set.iterator();
//		while (c_itr.hasNext()) {
//			Character c = c_itr.next();
//			char_idx.put(c, index);
//			index++;
//		}
//		int[][] char_seq = new int[ati_out.size()][max_length];
//		Set<Integer> keys = ati_out.keySet();
//		Set<Integer> sort_keys = new TreeSet<Integer>((o1, o2) -> o1.compareTo(o2));
//		sort_keys.addAll(keys);
//		Iterator<Integer> kitr = sort_keys.iterator();
//		int k_idx = -1;
//		while (kitr.hasNext()) {
//			k_idx++;
//			Integer k = kitr.next();
////			System.out.println("k:" + k);
//			Assert.isTrue(k_idx == k, "k_idx:" + k_idx + "#k:" + k);
//			String tc = ati_out.get(k);
//			String cnt = tc.substring(tc.indexOf('#')+1, tc.length());
//			if (cnt.equals(DefaultPart)) {
//				cnt = tc.substring(0, tc.indexOf('#'));
//			}
//			int tc_len = cnt.length();
//			for (int i=0;i<tc_len;i++) {
//				char c = cnt.charAt(i);
//				int idx = char_idx.get(c);
//				char_seq[k][i] = idx;
//			}
//			for (int i=tc_len;i<max_length;i++) {
//				char_seq[k][i] = -1;
//			}
//		}
//		Gson gson = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_char_sequence.json"),
//				gson.toJson(char_seq));
//		String char_seq_meta = "HitNumber:" + hit.size() + "\n" + "MaxCharSequenceLength:" + max_length + "\n" + "TotalNumberOfChar:" + char_idx.size();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_" + desc + "_char_sequence_summary.txt"), char_seq_meta);
//	}

//	private Map<String, Integer> HandleSubWord(String dir, Map<Integer, String> ati_out) {
//	
//		
//		return subword_index;
//	}

//	private void GenerateAndSaveCharSequenceInSubWordForm(String dir, Map<Integer, String> sw_out, Map<Character, Integer> char_idx) {
//		ArrayList<Integer> char_sequences = new ArrayList<Integer>();
//		ArrayList<Integer> each_char_sequence_start = new ArrayList<Integer>();
//		ArrayList<Integer> each_char_sequence_end = new ArrayList<Integer>();
//		
////		Map<Integer, String> sw_out = MapUtil.ReverseKeyValueInMap(subword_index);
//		Set<Integer> sw_keys = sw_out.keySet();
//		Iterator<Integer> sw_itr = sw_keys.iterator();
//		while (sw_itr.hasNext()) {
//			Integer sw = sw_itr.next();
////			Assert.isTrue(subword_is_end_of_token.size() == sw);
//			String subword = sw_out.get(sw);
////			if (subword.endsWith(" ")) {
////				subword_is_end_of_token.add(1);
////			} else {
////				subword_is_end_of_token.add(0);
////			}
//			each_char_sequence_start.add(char_sequences.size());
//			for (int i11 = 0; i11 < subword.length(); i11++) {
//				char c = subword.charAt(i11);
//				int idx = char_idx.get(c);
//				char_sequences.add(idx);
//			}
//			each_char_sequence_end.add(char_sequences.size() - 1);
//		}
//
//		Gson gson7 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_char_sequences.json"), gson7.toJson(char_sequences));
//		Gson gson8 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_char_sequence_start.json"),
//				gson8.toJson(each_char_sequence_start));
//		Gson gson9 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_char_sequence_end.json"),
//				gson9.toJson(each_char_sequence_end));
//	}

	private void GenerateAndSaveCharSequence(String dir, Map<Integer, String> tk_out,
			Map<Character, Integer> char_idx) {
		ArrayList<Integer> char_sequences = new ArrayList<Integer>();
		ArrayList<Integer> each_char_sequence_start = new ArrayList<Integer>();
		ArrayList<Integer> each_char_sequence_end = new ArrayList<Integer>();

//		// handle char index
//		Map<Integer, String> ati_out = MapUtil.ReverseKeyValueInMap(token_id_map);
//		Set<Character> c_set = new TreeSet<Character>();
//		Collection<String> ao = ati_out.values();
//		Iterator<String> aitr = ao.iterator();
//		while (aitr.hasNext()) {
//			String tc = aitr.next();
//			int tc_len = tc.length();
//			for (int i = 0; i < tc_len; i++) {
//				char c = tc.charAt(i);
//				c_set.add(c);
//			}
//		}
//		Map<Character, Integer> char_idx = new HashMap<Character, Integer>();
//		Iterator<Character> c_itr = c_set.iterator();
//		while (c_itr.hasNext()) {
//			Character c = c_itr.next();
//			char_idx.put(c, char_idx.size());
//		}
//		char_num = char_idx.size();

		// handle sub words
//		Map<String, Integer> sub_words = HandleSubWord(dir, ati_out);
//		ArrayList<Integer> subword_sequences = new ArrayList<Integer>();
//		ArrayList<Integer> each_subword_sequence_start = new ArrayList<Integer>();
//		ArrayList<Integer> each_subword_sequence_end = new ArrayList<Integer>();

//		Map<String, Integer> subword_index = new TreeMap<String, Integer>();
//		for (int i = 0; i < ati_out.size(); i++) {
//			String token = ati_out.get(i);
//			Assert.isTrue(token != null && token.length() > 0);
//			ArrayList<String> subwords = ContentUtil.SplitByUnderScoreWithCamelCase(token);
//			Assert.isTrue(subwords.size() > 0);
//			each_subword_sequence_start.add(subword_sequences.size());
//			for (int i1 = 0; i1 < subwords.size(); i1++) {
//				String subword = subwords.get(i1);
//				if (!subword_index.containsKey(subword)) {
//					subword_index.put(subword, subword_index.size());
//				}
//				Integer idx = subword_index.get(subword);
//				subword_sequences.add(idx);
//			}
//			each_subword_sequence_end.add(subword_sequences.size() - 1);
//		}

//		Gson gson4 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_subword_sequences.json"), gson4.toJson(subword_sequences));
//		Gson gson5 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_subword_sequence_start.json"),
//				gson5.toJson(each_subword_sequence_start));
//		Gson gson6 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_subword_sequence_end.json"),
//				gson6.toJson(each_subword_sequence_end));

//		for (int i=0;i<ati_out.size();i++) {
//			String token = ati_out.get(i);
//			Assert.isTrue(token != null && token.length() > 0);
//			ArrayList<String> subwords = ContentUtil.SplitByUnderScoreWithCamelCase(token);
//			for (int i1=0;i1<subwords.size();i1++) {

//		ArrayList<Integer> subword_is_end_of_token = new ArrayList<Integer>();

//		Map<Integer, String> sw_out = ati_out;// MapUtil.ReverseKeyValueInMap(subword_index);
		Set<Integer> sw_keys = tk_out.keySet();
		Iterator<Integer> sw_itr = sw_keys.iterator();
		while (sw_itr.hasNext()) {
			Integer sw = sw_itr.next();
//			Assert.isTrue(subword_is_end_of_token.size() == sw);
			String subword = tk_out.get(sw);
//			if (subword.endsWith(" ")) {
//				subword_is_end_of_token.add(1);
//			} else {
//				subword_is_end_of_token.add(0);
//			}
			each_char_sequence_start.add(char_sequences.size());
			for (int i11 = 0; i11 < subword.length(); i11++) {
				char c = subword.charAt(i11);
				int idx = char_idx.get(c);
				char_sequences.add(idx);
			}
			each_char_sequence_end.add(char_sequences.size() - 1);
		}

//		Gson gson0 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_subword_is_end_of_token.json"),
//				gson0.toJson(subword_is_end_of_token));

//				String subword = subwords.get(i1);
//				Assert.isTrue(subword != null && subword.length() > 0);
//				if (!subword_index.containsKey(subword)) {
//					subword_index.put(subword, subword_index.size());
//					each_char_sequence_start.add(char_sequences.size());
//					for (int i11=0;i11<subword.length();i11++) {
//						char c = subword.charAt(i11);
//						int idx = char_idx.get(c);
//						char_sequences.add(idx);
//					}
//					each_char_sequence_end.add(char_sequences.size()-1);
//				}
//			}
//			int tc_len = token.length();
//			each_char_sequence_start.add(char_sequences.size());
//			for (int i1=0;i1<tc_len;i1++) {
//				char c = token.charAt(i1);
//				int idx = char_idx.get(c);
//				char_sequences.add(idx);
//			}
//			if (i < grammar_token_num) {
//				each_char_sequence_end.add(-1);
//			} else {
//				each_char_sequence_end.add(char_sequences.size()-1);
//			}
//		}

		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_char_sequences.json"), gson.toJson(char_sequences));
		Gson gson2 = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_char_sequence_start.json"),
				gson2.toJson(each_char_sequence_start));
		Gson gson3 = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_char_sequence_end.json"),
				gson3.toJson(each_char_sequence_end));
	}

	private void GenerateAndSaveBPESubWordSequenceInCascadeForm(String dir) {
//		PrintUtil.PrintList(id_tool.bpe_mr.merges, "id_tool.bpe_mr.merges");

		TreeMap<String, Integer> ht = id_tool.tr.hit_train;
		Set<String> ht_keys = ht.keySet();
		Set<String> inserted_ht_keys = BPEWordsUtil.InsertSpaceToTokens(ht_keys);
		BPEHandledResult hit_res = BPEWordsUtil.ApplyBPEMergesToTokens(id_tool.bpe_mr.merges, inserted_ht_keys);

		TreeMap<String, Integer> nht = id_tool.tr.not_hit_train;
		Set<String> nht_keys = nht.keySet();
		Set<String> inserted_nht_keys = BPEWordsUtil.InsertSpaceToTokens(nht_keys);
		BPEHandledResult not_hit_res = BPEWordsUtil.ApplyBPEMergesToTokens(id_tool.bpe_mr.merges, inserted_nht_keys);

		Map<String, String> origin_after = new TreeMap<String, String>();
		PrintUtil.PrintMap(hit_res.origin_after, "hit_res.origin_after", 100);
		origin_after.putAll(hit_res.origin_after);
		PrintUtil.PrintMap(not_hit_res.origin_after, "not_hit_res.origin_after", 100);
		origin_after.putAll(not_hit_res.origin_after);
//		PrintUtil.PrintMap(origin_after, "origin_after");
//		TreeSet<String> ts = new TreeSet<String>(token_id_map.keySet());
//		ts.removeAll(id_tool.tr.hit_train.keySet());
//		ts.removeAll(id_tool.tr.not_hit_train.keySet());
//		PrintUtil.PrintSet(ts, "left things");

		Assert.isTrue(origin_after.size() == token_id_map.size(),
				"token_id_map.size():" + token_id_map.size() + "#origin_after.size():" + origin_after.size());

//		// in train
//		Set<String> in_train_vobs = new TreeSet<String>(hit_res.vobs);
//		// not in train
//		Set<String> not_in_train_vobs = new TreeSet<String>(not_hit_res.vobs);
//		not_in_train_vobs.removeAll(in_train_vobs);
//		System.out.println("In_Train_Sub_Vobs_Size:" + in_train_vobs.size() + "#Not_In_Train_Sub_Vobs_Size:"
//				+ not_in_train_vobs.size() + "#Unseen_Rate:"
//				+ (not_in_train_vobs.size() * 1.0) / (in_train_vobs.size() * 1.0));
////		PrintUtil.PrintSet(in_train_vobs, "in_train_vobs");
////		PrintUtil.PrintSet(not_in_train_vobs, "not_in_train_vobs");
////		ArrayList<Integer> subword_sequences = new ArrayList<Integer>();
////		ArrayList<Integer> each_subword_sequence_start = new ArrayList<Integer>();
////		ArrayList<Integer> each_subword_sequence_end = new ArrayList<Integer>();

		/**
		 * statistics for output
		 */
//		int in_hit_total_subword_num = 0;
		Set<String> in_hit_sub_words = new TreeSet<String>();
//		int in_hit_max_subword_num_in_one_token = 0;
//		int in_hit_token_num = 0;
//		int not_in_hit_total_subword_num = 0;
		Set<String> not_in_hit_sub_words = new TreeSet<String>();
//		int not_in_hit_max_subword_num_in_one_token = 0;
//		int not_in_hit_token_num = 0;

		ArrayList<Integer> subword_sequences = new ArrayList<Integer>();
		ArrayList<Integer> each_subword_sequence_start = new ArrayList<Integer>();
		ArrayList<Integer> each_subword_sequence_end = new ArrayList<Integer>();

		TreeMap<String, Integer> subword_index = new TreeMap<String, Integer>();
		subword_index.put(SWordUNK, subword_index.size());
//		for (String rv : reserved_words) {
//			subword_index.put(rv, subword_index.size());
//		}

		Map<Integer, String> ati_out = MapUtil.ReverseKeyValueInMap(token_id_map);
		for (int i = 0; i < ati_out.size(); i++) {
			String ori_token = ati_out.get(i);
			Assert.isTrue(ori_token != null && ori_token.length() > 0);
			String token = BPEWordsUtil.InsertSpaceToToken(ori_token);
			Assert.isTrue(origin_after.get(token) != null, "token:" + token);
			ArrayList<String> subwords = new ArrayList<String>(Arrays.asList(origin_after.get(token).split(" ")));
			int subwords_size = subwords.size();
			if (id_tool.tr.hit_train.containsKey(ori_token)) {
////				in_hit_total_subword_num += subwords_size;
				in_hit_sub_words.addAll(subwords);
////				if (in_hit_max_subword_num_in_one_token < subwords_size) {
////					in_hit_max_subword_num_in_one_token = subwords_size;
////				}
////				in_hit_token_num++;
//				for (String subword : subwords) {
//					not_in_hit_sub_words.remove(subword);
//				}
			} else {
				not_in_hit_sub_words.addAll(subwords);
//				Assert.isTrue(id_tool.tr.not_hit_train.containsKey(ori_token));
////				int n_h_num = 0;
//				for (String subword : subwords) {
//					if (!in_hit_sub_words.contains(subword)) {
////						not_in_hit_total_subword_num += subwords_size;
//						not_in_hit_sub_words.add(subword);
////						n_h_num++;
//					}
//				}
////				if (not_in_hit_max_subword_num_in_one_token < n_h_num) {
////					not_in_hit_max_subword_num_in_one_token = n_h_num;
////				}
////				not_in_hit_token_num++;
			}

			Assert.isTrue(subwords_size > 0);
			Assert.isTrue(i == each_subword_sequence_start.size());

			each_subword_sequence_start.add(subword_sequences.size());
			for (int i1 = 0; i1 < subwords_size; i1++) {
				String subword = subwords.get(i1);
//				if (i1 == subwords.size()-1) {
//					subword = subword + " ";
//				}
				if (!subword_index.containsKey(subword)) {
					subword_index.put(subword, subword_index.size());
				}
				Integer idx = subword_index.get(subword);
				subword_sequences.add(idx);
			}
			each_subword_sequence_end.add(subword_sequences.size() - 1);
		}

		Map<Integer, String> sw_out = MapUtil.ReverseKeyValueInMap(subword_index);
		subword_num = subword_index.size();

		/**
		 * print statistics
		 */
//		System.out.println("in_hit_token_num:" + in_hit_token_num);
//		System.out.println("not_in_hit_token_num:" + not_in_hit_token_num);
//		System.out.println("total_token_unseen_rate:" + (not_in_hit_token_num * 1.0) / ((in_hit_token_num + not_in_hit_token_num)*1.0));
		not_in_hit_sub_words.removeAll(in_hit_sub_words);
		int in_hit_total_subword_num = in_hit_sub_words.size();
		int not_in_hit_total_subword_num = not_in_hit_sub_words.size();
		System.out.println("in_hit_total_subword_num:" + in_hit_total_subword_num);
//		System.out.println("in_hit_average_subword_num_in_one_token:"
//				+ ((in_hit_total_subword_num * 1.0) / (in_hit_token_num * 1.0)));
//		System.out.println("in_hit_max_subword_num_in_one_token:" + in_hit_max_subword_num_in_one_token);
		System.out.println("not_in_hit_total_subword_num:" + not_in_hit_total_subword_num);
		System.out.println("total_subword_unseen_rate:"
				+ (not_in_hit_total_subword_num * 1.0) / ((in_hit_total_subword_num) * 1.0));
//		System.out.println("not_in_hit_average_subword_num_in_one_token:"
//				+ ((not_in_hit_total_subword_num * 1.0) / (not_in_hit_token_num * 1.0)));
//		System.out.println("not_in_hit_max_subword_num_in_one_token:" + not_in_hit_max_subword_num_in_one_token);

		Gson gson4 = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_subword_sequences.json"), gson4.toJson(subword_sequences));
		Gson gson5 = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_subword_sequence_start.json"),
				gson5.toJson(each_subword_sequence_start));
		Gson gson6 = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_subword_sequence_end.json"),
				gson6.toJson(each_subword_sequence_end));
		GenerateIDJson(dir, subword_index, "subword");
		ArrayList<Integer> subword_is_start_end = new ArrayList<Integer>();
		TreeMap<Integer, String> idx_subword = MapUtil.ReverseKeyValueInMap(subword_index);
		Set<Integer> idxs = idx_subword.keySet();
		for (int idx : idxs) {
			String subword = idx_subword.get(idx);
			int state = 0b00;
			if (subword.endsWith("_")) {
				state |= 0b01;
			}
			if (subword.startsWith("$")) {
				state |= 0b10;
			}
			Assert.isTrue(idx == subword_is_start_end.size());
			subword_is_start_end.add(state);
		}
		Gson gson7 = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_subword_is_start_end.json"), gson7.toJson(subword_is_start_end));

//		ArrayList<Integer> char_sequences = new ArrayList<Integer>();
//		ArrayList<Integer> each_char_sequence_start = new ArrayList<Integer>();
//		ArrayList<Integer> each_char_sequence_end = new ArrayList<Integer>();
		// char index
		// handle char index
		Set<Character> in_hit_chars = new TreeSet<Character>();
		Set<Character> not_in_hit_chars = new TreeSet<Character>();
		Set<Character> c_set = new TreeSet<Character>();
		Collection<String> so = sw_out.values();
		Iterator<String> sitr = so.iterator();
		while (sitr.hasNext()) {
			String sw = sitr.next();
			int tc_len = sw.length();
			for (int i = 0; i < tc_len; i++) {
				char c = sw.charAt(i);
				c_set.add(c);
				if (in_hit_sub_words.contains(sw)) {
					in_hit_chars.add(c);
					not_in_hit_chars.remove(c);
				} else {
					not_in_hit_chars.add(c);
				}
			}
		}
		System.out.println("in_hit_total_char_num:" + in_hit_chars.size());
		System.out.println("not_in_hit_total_char_num:" + not_in_hit_chars.size());
		System.out.println("total_char_unseen_rate:"
				+ (not_in_hit_chars.size() * 1.0) / ((in_hit_chars.size() + not_in_hit_chars.size()) * 1.0));

		Map<Character, Integer> char_idx = new HashMap<Character, Integer>();
		char_idx.put(' ', char_idx.size());

		Iterator<Character> c_itr = c_set.iterator();
		while (c_itr.hasNext()) {
			Character c = c_itr.next();
			char_idx.put(c, char_idx.size());
		}
		char_num = char_idx.size();

//		if (MetaOfApp.CharForm == MetaOfApp.TokenChar) {
//			GenerateAndSaveCharSequence(dir, ati_out, char_idx);
//		} else 
//		if (MetaOfApp.CharForm == MetaOfApp.SubWordChar) {
		GenerateAndSaveCharSequence(dir, sw_out, char_idx);
//		} else {
//			Assert.isTrue(false);
//		}

//		char_num = subword_index.size();
//		Assert.isTrue(char_num > 0, "char_num must be greater than 0");

//		ArrayList<Integer> subword_is_end_of_token = new ArrayList<Integer>();
//
//		TreeMap<Integer, String> sw_out = MapUtil.ReverseKeyValueInMap(subword_index);
//		Set<Integer> sw_keys = sw_out.keySet();
//		Iterator<Integer> sw_itr = sw_keys.iterator();
//		while (sw_itr.hasNext()) {
//			Integer sw = sw_itr.next();
//			Assert.isTrue(subword_is_end_of_token.size() == sw);
//			String subword = sw_out.get(sw);
//			if (subword.endsWith("_")) {
//				subword_is_end_of_token.add(1);
//			} else {
//				subword_is_end_of_token.add(0);
//			}
//		}

		// validate token subwords
		Map<String, String> token_subwords = new TreeMap<String, String>();
		int i_len = each_subword_sequence_start.size();
		for (int i = 0; i < i_len; i++) {
			Integer st = each_subword_sequence_start.get(i);
			Integer ed = each_subword_sequence_end.get(i);
			Assert.isTrue(ed >= st);
			String token = "";
			String sbwds = "";
			for (int j = st; j <= ed; j++) {
				Integer subword_idx = subword_sequences.get(j);
				String subsord = sw_out.get(subword_idx);
				token += subsord;
				sbwds += (subsord + " ");
			}
//			token = token.substring(0, token.length()-1);
			String exp_tk = ati_out.get(i);// .replace(" ", "")
			Assert.isTrue((token).equals(exp_tk), "token:" + token + "#expected:" + exp_tk);
			token_subwords.put(exp_tk, sbwds);
		}
		System.out.println("=== unsubword token num:" + token_subwords.size() + "#number_of_merges:"
				+ MetaOfApp.NumberOfMerges + " ===");
//		PrintUtil.PrintMap(token_subwords, "token_subwords");

//		Gson gson0 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_subword_is_end_of_token.json"),
//				gson0.toJson(subword_is_end_of_token));

//		return subword_index;
//		// handle sub words
//		Map<String, Integer> sub_words = HandleSubWord(dir, ati_out);
//		TreeMap<Integer, String> sw_out = MapUtil.ReverseKeyValueInMap(sub_words);
//		Set<Integer> sw_keys = sw_out.keySet();
//		Iterator<Integer> sw_itr = sw_keys.iterator();
//		while (sw_itr.hasNext()) {
//			Integer sw = sw_itr.next();
//			String subword = sw_out.get(sw);
//			each_char_sequence_start.add(char_sequences.size());
//			for (int i11=0;i11<subword.length();i11++) {
//				char c = subword.charAt(i11);
//				int idx = char_idx.get(c);
//				char_sequences.add(idx);
//			}
//			each_char_sequence_end.add(char_sequences.size()-1);
//		}
//		
//		Gson gson = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_char_sequences.json"),
//				gson.toJson(char_sequences));
//		Gson gson2 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_char_sequence_start.json"),
//				gson2.toJson(each_char_sequence_start));
//		Gson gson3 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_char_sequence_end.json"),
//				gson3.toJson(each_char_sequence_end));
	}

//	private void GenerateAndSaveCharSequence(String dir) {
//		ArrayList<Integer> char_sequences = new ArrayList<Integer>();
//		ArrayList<Integer> each_char_sequence_start = new ArrayList<Integer>();
//		ArrayList<Integer> each_char_sequence_end = new ArrayList<Integer>();
//		
//		Map<Integer, String> ati_out = MapUtil.ReverseKeyValueInMap(token_id_map);
//		Set<Character> c_set = new TreeSet<Character>();
//		Collection<String> ao = ati_out.values();
//		Iterator<String> aitr = ao.iterator();
//		while (aitr.hasNext()) {
//			String tc = aitr.next();
//			int tc_len = tc.length();
//			for (int i=0;i<tc_len;i++) {
//				char c = tc.charAt(i);
//				c_set.add(c);
//			}
//		}
//		Map<Character, Integer> char_idx = new HashMap<Character, Integer>();
//		Iterator<Character> c_itr = c_set.iterator();
//		while (c_itr.hasNext()) {
//			Character c = c_itr.next();
//			char_idx.put(c, char_idx.size());
//		}
//		char_num = char_idx.size();
//		
//		for (int i=0;i<ati_out.size();i++) {
//			String token = ati_out.get(i);
//			Assert.isTrue(token != null && token.length() > 0);
//			int tc_len = token.length();
//			each_char_sequence_start.add(char_sequences.size());
//			for (int i1=0;i1<tc_len;i1++) {
//				char c = token.charAt(i1);
//				int idx = char_idx.get(c);
//				char_sequences.add(idx);
//			}
//			if (i < id_tool.gr.fixed_tokens.size()) {
//				each_char_sequence_end.add(-1);
//			} else {
//				each_char_sequence_end.add(char_sequences.size()-1);
//			}
//		}
//		Gson gson = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_char_sequences.json"),
//				gson.toJson(char_sequences));
//		Gson gson2 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_char_sequence_start.json"),
//				gson2.toJson(each_char_sequence_start));
//		Gson gson3 = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_char_sequence_end.json"),
//				gson3.toJson(each_char_sequence_end));
//	}

	private void GenerateIDSummary(String dir) {
		Gson gson = new Gson();
		TreeMap<String, Integer> meta_of_ast2tensor = new TreeMap<String, Integer>();
		meta_of_ast2tensor.put("MaximumStringLength", MetaOfApp.MaximumStringLength);
		meta_of_ast2tensor.put("SkeletonNum", skeleton_id_map.size());
		meta_of_ast2tensor.put("SkeletonHitNum", skeleton_hit_num);
		meta_of_ast2tensor.put("SkeletonPENum", pe_skeleton_id_map.size());
		meta_of_ast2tensor.put("SkeletonPEHitNum", pe_skeleton_hit_num);
		meta_of_ast2tensor.put("SkeletonEachNum", each_skeleton_id_map.size());
		meta_of_ast2tensor.put("SkeletonEachHitNum", each_skeleton_hit_num);
		meta_of_ast2tensor.put("TokenNum", token_id_map.size());
		meta_of_ast2tensor.put("TokenHitNum", token_hit_num);
		meta_of_ast2tensor.put("SwordNum", subword_num);
		meta_of_ast2tensor.put("SwordHitNum", subword_num);
		meta_of_ast2tensor.put("CharNum", char_num);
		meta_of_ast2tensor.put("CharHitNum", char_num);
//		meta_of_ast2tensor.put("GrammarTokenNum", grammar_token_num);
//		meta_of_ast2tensor.put("TokenHitNumber", token_hit_num);
//		meta_of_ast2tensor.put("InBPEForm", MetaOfApp.InBPEForm ? 1 : 0);
//		meta_of_ast2tensor.put("NoChar", MetaOfApp.NoChar);
//		meta_of_ast2tensor.put("TokenChar", MetaOfApp.TokenChar);
//		meta_of_ast2tensor.put("SubWordChar", MetaOfApp.SubWordChar);
//		meta_of_ast2tensor.put("CharForm", MetaOfApp.CharForm);
//		meta_of_ast2tensor.put("TokenFixedNumber", id_tool.gr.fixed_tokens.size());
//		meta_of_ast2tensor.put("TotalNumberOfSubWord", subword_num);
//		meta_of_ast2tensor.put("TotalNumberOfChar", char_num);
		meta_of_ast2tensor.put("ReservedNumberOfWords", reserved_words.size());
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_summary.json"), gson.toJson(meta_of_ast2tensor));
//		String char_seq_meta = "GrammarTokenNum:" + grammar_token_num + "\n" + "TokenHitNumber:" + token_hit_num + "\n" + "TotalNumberOfChar:" + char_num;
	}

	private void GenerateAPIJson(String dir) {
		int all_size = api_comb_id_map.size();
		String[] strs = new String[all_size];
		Set<String> api_keys = api_comb_id_map.keySet();
		Iterator<String> api_k_itr = api_keys.iterator();
		while (api_k_itr.hasNext()) {
			String api_k = api_k_itr.next();
			Integer idx = api_comb_id_map.get(api_k);
			strs[idx] = api_k;
		}

		ArrayList<Integer> api_comb_sequences = new ArrayList<Integer>();
		ArrayList<Integer> each_api_comb_start = new ArrayList<Integer>();
		ArrayList<Integer> each_api_comb_end = new ArrayList<Integer>();

		for (int i = 0; i < all_size; i++) {
			String str = strs[i];
			String[] to_compare_apis = str.split("#");
			each_api_comb_start.add(api_comb_sequences.size());
			for (String tc_api : to_compare_apis) {
				api_comb_sequences.add(GetTypeContentID(tc_api));
			}
			each_api_comb_end.add(api_comb_sequences.size() - 1);
		}
		// add default api_comb
		each_api_comb_start.add(api_comb_sequences.size());
		api_comb_sequences.add(-1);
		each_api_comb_end.add(api_comb_sequences.size() - 1);

		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_api_comb_sequences.json"), gson.toJson(api_comb_sequences));
		Gson gson2 = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_api_comb_sequence_start.json"),
				gson2.toJson(each_api_comb_start));
		Gson gson3 = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_each_api_comb_sequence_end.json"),
				gson3.toJson(each_api_comb_end));
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
//		Gson gson = new Gson();
//		FileUtil.WriteToFile(new File(dir + "/" + "All_type_content_is_leaf.json"), gson.toJson(MapUtil.ListToIndexMap(ast_type_content_is_leaf)));
//		GenerateIDJson(dir, not_hit_ast_type_content_id_map, "not_hit_type_content");
//		GenerateAndSaveCharSequence(dir, ast_type_content_id_map, not_hit_ast_type_content_id_map, "type_content");
//		GenerateIDJson(dir, ast_type_content_id_map, "type_content");
//		if (MetaOfApp.InBPEForm) {
//			GenerateAndSaveCharSequence(dir);
		GenerateAndSaveBPESubWordSequenceInCascadeForm(dir);
//		} else {
//		}
		// only for debug
		GenerateIDJson(dir, skeleton_id_map, "skeleton");
		GenerateIDJson(dir, token_id_map, "token");
		GenerateIDJson(dir, grammar_id_map, "grammar");
		SaveGrammarToDirectory(dir);
		GenerateIDJson(dir, api_comb_id_map, "api_comb");
		// for real tensor usage
		GenerateIDHitJson(dir);
		GenerateIDSummary(dir);
		GenerateAPIJson(dir);
//		GenerateTypeAndSummaryJson(dir, ast_type_content_id_map, "type");
//		GenerateAndSaveHuffTree(dir, ast_type_content_id_count_map, "type_content", MetaOfApp.TypeContentHuffTreeStandardChildrenNum);
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

	private void SaveGrammarToDirectory(String dir) {
		ArrayList<Integer> self_children_grammar = new ArrayList<Integer>();
		ArrayList<Integer> self_children_grammar_start = new ArrayList<Integer>();
		ArrayList<Integer> self_children_grammar_end = new ArrayList<Integer>();

		int g_size = grammar_id_map.size();
		for (int i = 0; i < g_size; i++) {
			TreeSet<Integer> ll = grammar_id_token_id_map.get(i);
			self_children_grammar_start.add(self_children_grammar.size());
			if (ll == null || ll.size() == 0) {
				self_children_grammar.add(0);
			} else {
				for (Integer l : ll) {
					self_children_grammar.add(l);
				}
			}
			self_children_grammar_end.add(self_children_grammar.size() - 1);
		}

		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_grammar_ids.json"),
				gson.toJson(self_children_grammar));
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_grammar_start.json"),
				gson.toJson(self_children_grammar_start));
		FileUtil.WriteToFile(new File(dir + "/" + "All_token_grammar_end.json"),
				gson.toJson(self_children_grammar_end));
	}

//	public GrammarRecorder GetGrammarRecorder() {
//		return id_tool.gr;
//	}

	public String WordVocabularyInfo() {
		return "Summary -- " + "#Vocabulary_Word_Size:" + token_hit_num + "#OutOfVocabulary_Word_Size:"
				+ (token_id_map.size() - token_hit_num) + "#Unseen_Rate:"
				+ ((token_id_map.size() - token_hit_num) * 1.0) / (token_hit_num * 1.0) 
				+ "#Word_Unk_Num:" + MetaOfApp.NumberOfUnk
				+ "#Word_Hit_Num:" + id_tool.tr.hit_train.size() 
				+ "#Word_Not_Hit_Num:" + id_tool.tr.not_hit_train.size() 
				+ "#Vocabulary_Skeleton_Size:" + skeleton_hit_num
				+ "#OutOfVocabulary_Skeleton_Size:" + (skeleton_id_map.size() - skeleton_hit_num) 
				+ "#Unseen_Rate:" + ((skeleton_id_map.size() - skeleton_hit_num) * 1.0) / (skeleton_hit_num * 1.0) 
//				+ "#pair_encoded_skeleton_hit_num:" + pair_encoded_skeleton_hit_num
				+ "#Skeleton_Unk_Num:" + MetaOfApp.NumberOfSkeletonUnk
				+ "#Skeleton_Hit_Num:" + id_tool.one_struct_r.hit_train.size()
				+ "#Skeleton_Not_Hit_Num:" + id_tool.one_struct_r.not_hit_train.size()
				+ "#PESkeleton_Hit_Num:" + id_tool.pe_struct_r.hit_train.size()
				+ "#PESkeleton_Not_Hit_Num:" + id_tool.pe_struct_r.not_hit_train.size()
				+ "#EachSkeleton_Hit_Num:" + id_tool.e_struct_r.hit_train.size()
				+ "#EachSkeleton_Not_Hit_Num:" + id_tool.e_struct_r.not_hit_train.size()
				;
		// + "#OutOfVocabulary_API_Comb_Size:"
		// +
		// (api_comb_id_map.size()
		// -
		// api_comb_hit_num);
		// + "#Vocabulary_API_Comb_Size:" + api_comb_id_map.size()
	}

}
