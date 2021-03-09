package statistic.id;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.Gson;

import statistic.IDTools;
import statistic.id.util.RegistUtil;
import statistic.id.util.SeriesUtil;
import tree.TreeNodeParentInfo;
import util.FileUtil;

public class ParentSktHintManager {
	
	private IDManager im = null;
	
	private TreeMap<String, Integer> skt_one_parent_hint_id_map = new TreeMap<String, Integer>();
	private TreeMap<Integer, TreeSet<Integer>> skt_one_parent_hint_id_one_token_id_map = new TreeMap<Integer, TreeSet<Integer>>();
	
	private TreeMap<String, Integer> skt_pe_parent_hint_id_map = new TreeMap<String, Integer>();
	private TreeMap<Integer, TreeSet<Integer>> skt_pe_parent_hint_id_pe_token_id_map = new TreeMap<Integer, TreeSet<Integer>>();
	
	private TreeMap<String, Integer> skt_e_parent_hint_id_map = new TreeMap<String, Integer>();
	private TreeMap<Integer, TreeSet<Integer>> skt_e_parent_hint_id_e_token_id_map = new TreeMap<Integer, TreeSet<Integer>>();
	
	private int one_hint_hit_num = -1;
	private int pe_hint_hit_num = -1;
	private int e_hint_hit_num = -1;
	
	public ParentSktHintManager(IDManager im, IDTools tool) {
		this.im = im;
		ParentSktHintRecorder recorder = tool.hint_recorder;
		
		RegistUtil.Regist(skt_one_parent_hint_id_map, IDManager.reserved_words);
		RegistUtil.Regist(skt_pe_parent_hint_id_map, IDManager.reserved_words);
		RegistUtil.Regist(skt_e_parent_hint_id_map, IDManager.reserved_words);
		
		one_hint_hit_num = RegistUtil.Regist(skt_one_parent_hint_id_map, recorder.one_r.hit_train.GetOriginMap(), 0, recorder.one_r.not_hit_train.GetOriginMap(), 0, 0, "one_hit");
		pe_hint_hit_num = RegistUtil.Regist(skt_pe_parent_hint_id_map, recorder.pe_r.hit_train.GetOriginMap(), 0, recorder.pe_r.not_hit_train.GetOriginMap(), 0, 0, "pe_hit");
		e_hint_hit_num = RegistUtil.Regist(skt_e_parent_hint_id_map, recorder.e_r.hit_train.GetOriginMap(), 0, recorder.e_r.not_hit_train.GetOriginMap(), 0, 0, "e_hit");
	}
	
	public int GetSktOneParentHintID(String parent_hint) {
		return GetHintID(skt_one_parent_hint_id_map, parent_hint, one_hint_hit_num);
	}
	
	public void RegistOneParentHintIDOneTokenID(int parent_hint_id, int otid) {
		RegistParentHintIDTokenID(skt_one_parent_hint_id_one_token_id_map, parent_hint_id, otid);
	}
	
	public int GetSktPeParentHintID(String parent_hint) {
		return GetHintID(skt_pe_parent_hint_id_map, parent_hint, pe_hint_hit_num);
	}
	
	public void RegistPeParentHintIDPeTokenID(int parent_hint_id, int otid) {
		RegistParentHintIDTokenID(skt_pe_parent_hint_id_pe_token_id_map, parent_hint_id, otid);
	}
	
	public int GetSktEParentHintID(String parent_hint) {
		return GetHintID(skt_e_parent_hint_id_map, parent_hint, e_hint_hit_num);
	}
	
	public void RegistEParentHintIDETokenID(int parent_hint_id, int otid) {
		RegistParentHintIDTokenID(skt_e_parent_hint_id_e_token_id_map, parent_hint_id, otid);
	}
	

	public int GetHintID(TreeMap<String, Integer> hint_id_map, String hint, int hint_hit_num) {
		Integer id = hint_id_map.get(hint);
//		Assert.isTrue(id != null, "unseen skeleton:" + skeleton);
		if (id == null || id >= hint_hit_num) {// MetaOfApp.OutOfScopeReplacedByUnk && 
			id = hint_id_map.get(IDManager.Unk);
		}
//		if (id == null) {
//			System.out.println("==== Unk type_content: " + type_content + " ====");
//			return skeleton_id_map.get(Unk);
//		}
		return id;
	}
	
//	public int RegistAndGetSktParentHintID(TreeMap<String, Integer> skt_parent_hint_id_map, String parent_hint) {
//		Integer hint_id = skt_parent_hint_id_map.get(parent_hint);
//		if (hint_id == null) {
//			hint_id = skt_parent_hint_id_map.size();
//			skt_parent_hint_id_map.put(parent_hint, hint_id);
//		}
//		return hint_id;
//	}
	
	public void RegistParentHintIDTokenID(TreeMap<Integer, TreeSet<Integer>> skt_parent_hint_id_token_id_map, int parent_hint_id, int otid) {
		TreeSet<Integer> iset = skt_parent_hint_id_token_id_map.get(parent_hint_id);
		if (iset == null) {
			iset = new TreeSet<Integer>();
			skt_parent_hint_id_token_id_map.put(parent_hint_id, iset);
		}
		iset.add(otid);
	}
	
	public void SaveToDirectory(String dir) {
		SaveToDirectory(dir + "/" + "All_skt_one_parent_hint_mask.json", skt_one_parent_hint_id_one_token_id_map, im.skeleton_hit_num + im.skt_token_hit_num);
		SaveToDirectory(dir + "/" + "All_skt_pe_parent_hint_mask.json", skt_pe_parent_hint_id_pe_token_id_map, im.pe_skeleton_hit_num + im.skt_token_hit_num);
		SaveToDirectory(dir + "/" + "All_skt_e_parent_hint_mask.json", skt_e_parent_hint_id_e_token_id_map, im.each_skeleton_hit_num + im.skt_token_hit_num);
		
		SeriesUtil.GenerateIDJson(dir, skt_one_parent_hint_id_map, "skt_one_par_hint");
		SeriesUtil.GenerateStrIDJson(dir, skt_one_parent_hint_id_map, "skt_one_par_hint");
		SeriesUtil.GenerateIDJson(dir, skt_pe_parent_hint_id_map, "skt_pe_par_hint");
		SeriesUtil.GenerateStrIDJson(dir, skt_pe_parent_hint_id_map, "skt_pe_par_hint");
		SeriesUtil.GenerateIDJson(dir, skt_e_parent_hint_id_map, "skt_e_par_hint");
		SeriesUtil.GenerateStrIDJson(dir, skt_e_parent_hint_id_map, "skt_e_par_hint");
	}
	
	private void SaveToDirectory(String json_file, TreeMap<Integer, TreeSet<Integer>> skt_parent_hint_id_token_id_map, int n_token) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		
		Set<Integer> k_set = skt_parent_hint_id_token_id_map.keySet();
		ArrayList<Integer> k_arr = new ArrayList<Integer>(k_set);
		int size = k_arr.get(k_arr.size()-1)+1;
		for (int i=0;i<size;i++) {
			ArrayList<Integer> ll = new ArrayList<Integer>();
			result.add(ll);
			for (int j=0;j<n_token;j++) {
				ll.add(0);
			}
		}
		
		Iterator<Integer> k_itr = k_arr.iterator();
		while (k_itr.hasNext()) {
			Integer k = k_itr.next();
			ArrayList<Integer> ll = result.get(k);
			TreeSet<Integer> t_set = skt_parent_hint_id_token_id_map.get(k);
			for (Integer t : t_set) {
				ll.set(t, 1);
			}
		}
		
		Gson gson = new Gson();
		FileUtil.WriteToFile(new File(json_file),
				gson.toJson(result));
	}
	// IDManager im, String pfx, 
	public static String GenParentHint(ArrayList<TreeNodeParentInfo> par_info) {
//		String infix = StringEquivalentUtil.SktIDManagerEquivalent(pfx);
//		String im_func = "Get" + infix + "SkeletonID";
		String hint = "";
		for (TreeNodeParentInfo pi : par_info) {
			String tcnt = pi.tree_node_content;
			int index = pi.index;

//			int sd = (int) ReflectUtil.ReflectMethod(im_func, im, new Class<?>[] {String.class}, new Object[] {tcnt});
			hint += tcnt + ":" + index + "#";
		}
		return hint;
	}
	
}
