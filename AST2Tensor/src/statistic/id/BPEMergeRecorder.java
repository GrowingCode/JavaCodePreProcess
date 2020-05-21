package statistic.id;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

import bpe.BPEWordsUtil;
import util.FileUtil;

public class BPEMergeRecorder {
	
	List<String> merges = new LinkedList<String>();
	
	private Map<String, Integer> token_times = new TreeMap<String, Integer>();
	
	public BPEMergeRecorder() {
	}
	
//	, Map<String, Integer> token_times
	public void Initialize(List<String> merges) {
		this.merges.addAll(merges);
//		this.token_times.putAll(token_times);
	}
	
	public void EncounterToken(String token, int encounter_time) {
		Integer tt = token_times.get(token);
		if (tt == null) {
			tt = 0;
		}
		tt += encounter_time;
		token_times.put(token, tt);
	}
	
	public void GenerateBPEMerges(int merge_num) {
//		PrintUtil.PrintMap(token_times, "token_times");
//		Map<String, Integer> sub_words = BPEWordsUtil.ExtractAllSubWords(token_times);
//		PrintUtil.PrintMap(sub_words, "sub_words");
		TreeMap<String, Integer> n_vob = BPEWordsUtil.InsertSpaceToTokens(token_times);// sub_words
//		PrintUtil.PrintMap(n_vob, "n_vob");
		List<String> mgs = BPEWordsUtil.GenerateBPEMerges(n_vob, merge_num);
//		PrintUtil.PrintList(mgs, "mgs");
//		BPEHandledResult result = ApplyBPEMergesToTokens(merges, n_vob.keySet());
//		Set<String> vbs = result.vobs;
//		Set<String> vbs = GenerateBEPVocabulary(n_vob, num_merges);
//		System.out.println("==== start printing vocabulary ====");
//		for (String vb : vbs) {
//			System.out.println(vb);
//		}
//		System.out.println("==== end printing vocabulary ====");
		merges.clear();
		token_times.clear();
		merges.addAll(mgs);
	}
	
	public void SaveTo(File merges_json) {// , File token_times_json
		FileUtil.WriteToFile(merges_json, new Gson().toJson(merges));
//		FileUtil.WriteToFile(token_times_json, new Gson().toJson(token_times));
	}
	
}
