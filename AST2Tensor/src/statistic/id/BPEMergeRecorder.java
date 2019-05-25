package statistic.id;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import bpe.BPEWordsUtil;
import util.PrintUtil;

public class BPEMergeRecorder {
	
	List<String> merges = new LinkedList<String>();
	
	Map<String, Integer> token_times = new TreeMap<String, Integer>();
	
	public BPEMergeRecorder() {
	}
	
	public void EncounterToken(String token, int encounter_time) {
		Integer tt = token_times.get(token);
		if (tt == null) {
			tt = 0;
		}
		tt += encounter_time;
		token_times.put(token, tt);
	}
	
	public void GenerateBPEMerges() {
		PrintUtil.PrintMap(token_times, "token_times");
		Map<String, Integer> sub_words = BPEWordsUtil.ExtractAllSubWords(token_times);
		TreeMap<String, Integer> n_vob = BPEWordsUtil.InsertSpaceToTokens(sub_words);
		List<String> mgs = BPEWordsUtil.GenerateBPEMerges(n_vob, -1);
//		BPEHandledResult result = ApplyBPEMergesToTokens(merges, n_vob.keySet());
//		Set<String> vbs = result.vobs;
//		Set<String> vbs = GenerateBEPVocabulary(n_vob, num_merges);
//		System.out.println("==== start printing vocabulary ====");
//		for (String vb : vbs) {
//			System.out.println(vb);
//		}
//		System.out.println("==== end printing vocabulary ====");
		merges.clear();
		merges.addAll(mgs);
	}
	
}
