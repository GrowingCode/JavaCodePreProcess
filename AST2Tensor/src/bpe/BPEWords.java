package bpe;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import util.MapUtil;

public class BPEWords {

	private static Map<String, Integer> get_stats(Map<String, Integer> vocab) {
		Map<String, Integer> pairs = new TreeMap<String, Integer>();
		Set<String> vks = vocab.keySet();
		Iterator<String> vk_itr = vks.iterator();
		while (vk_itr.hasNext()) {
			String vk = vk_itr.next();
			String[] vk_sbs = vk.split(" ");
			int freq = vocab.get(vk);
			for (int i = 0; i < vk_sbs.length - 1; i++) {
				String nk = vk_sbs[i] + " " + vk_sbs[i + 1];
				Integer n_freq = pairs.get(nk);
				if (n_freq == null) {
					n_freq = 0;
				}
				n_freq += freq;
				pairs.put(nk, n_freq);
			}
		}
		return pairs;
	}

	private static Map<String, Integer> merge_vocab(String pair, Map<String, Integer> old_vocab) {
		Map<String, Integer> new_vocab = new TreeMap<String, Integer>();
//	    bigram = re.escape(' '.join(pair))
		Set<String> ov_set = old_vocab.keySet();
		Iterator<String> os_itr = ov_set.iterator();
		while (os_itr.hasNext()) {
			String os = os_itr.next();
			String new_os = os.replace(pair, pair.replace(" ", ""));
			new_vocab.put(new_os, old_vocab.get(os));
		}
		return new_vocab;
	}
	
	public static List<String> GenerateBPEMerges(Map<String, Integer> vocab, int num_merges) {
		List<String> merges = new LinkedList<String>();
		Map<String, Integer> vocab_r = new TreeMap<String, Integer>(vocab);
		if (num_merges == -1) {
			num_merges = Integer.MAX_VALUE;
		}
		for (int i=0;i<num_merges;i++) {
			Map<String, Integer> pairs = get_stats(vocab);
			Collection<Integer> vals = pairs.values();
			boolean should_stop = true;
			for (Integer val : vals) {
				if (val > 1) {
					should_stop = false;
				}
			}
			if (should_stop) {
				break;
			}
			MapUtil.FindKeyWithMaxValue(pairs);
			String best = MapUtil.FindKeyWithMaxValue(pairs);
			vocab_r = merge_vocab(best, vocab_r);
			merges.add(best);
		}
//		PrintUtil.PrintMap(vocab_r);
		return merges;
	}
	
	public static BPEHandledResult ApplyBPEMergesToTokens(List<String> merges, Set<String> tokens) {
		BPEHandledResult result = new BPEHandledResult();
		Map<String, String> origin_after = new TreeMap<String, String>();
		Map<String, String> after_origin = new TreeMap<String, String>();
		for (String token : tokens) {
			origin_after.put(token, token);
			after_origin.put(token, token);
		}
		Set<String> inserted = InsertSpaceToTokens(tokens);
		Set<String> new_inserted = new TreeSet<String>();
		for (String merge : merges) {
			for (String ins : inserted) {
				String new_ins = ins.replace(merge, merge.replace(" ", ""));
				new_inserted.add(new_ins);
				String ori = after_origin.remove(ins);
				after_origin.put(new_ins, ori);
				origin_after.put(ori, new_ins);
			}
			inserted.clear();
			inserted.addAll(new_inserted);
			new_inserted.clear();
		}
		result.vobs.addAll(new_inserted);
		result.origin_after.putAll(origin_after);
		return result;
	}
	
	public static TreeMap<String,Integer> InsertSpaceToTokens(Map<String, Integer> vocab) {
		TreeMap<String, Integer> new_vocab = new TreeMap<String, Integer>();
		Set<String> vks = vocab.keySet();
		for (String vk : vks) {
			Integer r = vocab.get(vk);
			new_vocab.put(vk.replace("", " ").trim(), r);
		}
		return new_vocab;
	}
	
	public static Set<String> InsertSpaceToTokens(Set<String> tokens) {
		Set<String> new_tokens = new TreeSet<String>();
		for (String token : tokens) {
			new_tokens.add(token.replace("", " ").trim());
		}
		return new_tokens;
	}

	public static Set<String> ExtractAllBPEUnits(Set<String> bpe_raws) {
		// Map<String, Integer> vocab
		Set<String> bpes = new TreeSet<String>();
//		Set<String> vks = vocab.keySet();
		Set<String> vks = bpe_raws;
		Iterator<String> vk_itr = vks.iterator();
		while (vk_itr.hasNext()) {
			String vk = vk_itr.next();
			String[] vk_sbs = vk.split(" ");
//			int freq = vocab.get(vk);
			for (int i = 0; i < vk_sbs.length; i++) {
				bpes.add(vk_sbs[i]);
			}
		}
		return bpes;
	}
	
	public static void main(String[] args) {
	    Map<String, Integer> vocab = new TreeMap<String, Integer>();
	    vocab.put("low", 5);
	    vocab.put("lower", 2);
	    vocab.put("newest", 6);
	    vocab.put("widest", 3);
		int num_merges = 10;
		
		TreeMap<String, Integer> n_vob = InsertSpaceToTokens(vocab);
		List<String> merges = GenerateBPEMerges(n_vob, num_merges);
		BPEHandledResult result = ApplyBPEMergesToTokens(merges, n_vob.keySet());
		Set<String> vbs = result.vobs;
//		Set<String> vbs = GenerateBEPVocabulary(n_vob, num_merges);
		System.out.println("==== start printing vocabulary ====");
		for (String vb : vbs) {
			System.out.println(vb);
		}
		System.out.println("==== end printing vocabulary ====");
	}

}
