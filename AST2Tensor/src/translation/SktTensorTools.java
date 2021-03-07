package translation;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import main.MetaOfApp;
import statistic.id.IDManager;
import statistic.id.ParentSktHintManager;
import util.FileUtil;
import util.IntegerMapUtil;

public class SktTensorTools extends TensorTools {

	public ParentSktHintManager pshm = null;
	
	public Map<String, ArrayList<String>> one_to_each_str = new TreeMap<String, ArrayList<String>>();
	public Map<Integer, ArrayList<Integer>> one_to_each = new TreeMap<Integer, ArrayList<Integer>>();

	public Map<String, ArrayList<String>> one_to_pe_str = new TreeMap<String, ArrayList<String>>();
	public Map<Integer, ArrayList<Integer>> one_to_pe = new TreeMap<Integer, ArrayList<Integer>>();
	
	public Map<String, ArrayList<String>> pe_to_each_str = new TreeMap<String, ArrayList<String>>();
	public Map<Integer, ArrayList<Integer>> pe_to_each = new TreeMap<Integer, ArrayList<Integer>>();
	
	public SktTensorTools(IDManager im, ParentSktHintManager pshm) {
		super(im);
		this.pshm = pshm;
	}

	public void SaveSkeletonComposeData() {
		/*
		 * store one_to_each;one_to_pe;pe_to_each
		 */
		FileUtil.WriteJson(one_to_each_str, MetaOfApp.DataDirectory + "/All_map_skt_one_to_each_str.json");
		FileUtil.WriteJson(one_to_pe_str, MetaOfApp.DataDirectory + "/All_map_skt_one_to_pe_str.json");
		FileUtil.WriteJson(pe_to_each_str, MetaOfApp.DataDirectory + "/All_map_skt_pe_to_each_str.json");
		
		ArrayList<ArrayList<Integer>> oe_container = IntegerMapUtil.MapToNestedList(one_to_each);
		FileUtil.WriteJson(oe_container, MetaOfApp.DataDirectory + "/All_skt_one_to_each.json");
		
		ArrayList<ArrayList<Integer>> ope_container = IntegerMapUtil.MapToNestedList(one_to_pe);
		FileUtil.WriteJson(ope_container, MetaOfApp.DataDirectory + "/All_skt_one_to_pe.json");
		
		ArrayList<ArrayList<Integer>> pee_container = IntegerMapUtil.MapToNestedList(pe_to_each);
		FileUtil.WriteJson(pee_container, MetaOfApp.DataDirectory + "/All_skt_pe_to_each.json");
	}
	
}
