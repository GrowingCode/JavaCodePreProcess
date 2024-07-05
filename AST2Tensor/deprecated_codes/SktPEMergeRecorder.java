package statistic.id;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

import com.google.gson.Gson;

import bpe.skt.SktPETreesUtil;
import bpe.skt.TreeNodeTwoMerge;
import bpe.skt.TreeNodeTwoMergeWithFreqs;
import tree.Forest;
import util.FileUtil;

public class SktPEMergeRecorder {
	
	List<TreeNodeTwoMergeWithFreqs> merges = new LinkedList<TreeNodeTwoMergeWithFreqs>();
	
	private ArrayList<Forest> sktf_times = new ArrayList<Forest>();//, Integer
	
	public SktPEMergeRecorder() {
	}
	
//	, Map<String, Integer> token_times
	public void Initialize(List<TreeNodeTwoMergeWithFreqs> merges) {
		for (TreeNodeTwoMerge merge : merges) {
			Assert.isTrue(!merge.GetMerged().contains("\n"));
		}
		this.merges.addAll(merges);
//		this.token_times.putAll(token_times);
	}
	
	public List<TreeNodeTwoMergeWithFreqs> GetMerges() {
		return merges;
	}
	
	public void EncounterSkeleton(ArrayList<Forest> fs) {
//		Integer tt = skt_times.get(skt);
//		if (tt == null) {
//			tt = 0;
//		}
//		tt += encounter_time;
//		skt_times.put(skt, tt);
		sktf_times.addAll(fs);
	}
	
	public void GenerateSktPEMerges() {// int merge_num
//		PrintUtil.PrintMap(token_times, "token_times");
//		Map<String, Integer> sub_words = BPEWordsUtil.ExtractAllSubWords(token_times);
//		PrintUtil.PrintMap(sub_words, "sub_words");
//		PrintUtil.PrintMap(n_vob, "n_vob");
		List<TreeNodeTwoMergeWithFreqs> mgs = SktPETreesUtil.GenerateSktPEMerges(sktf_times);// , merge_num
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
		sktf_times.clear();
		merges.addAll(mgs);
	}
	
	public void SaveTo(File merges_json) {// , File token_times_json
		FileUtil.WriteToFile(merges_json, new Gson().toJson(merges));
//		FileUtil.WriteToFile(token_times_json, new Gson().toJson(token_times));
	}
	
}
