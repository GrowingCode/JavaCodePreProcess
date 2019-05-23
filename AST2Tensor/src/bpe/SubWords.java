package bpe;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import util.MapUtil;
import util.PrintUtil;

public class SubWords {

	public static Map<String, Integer> get_stats(Map<String, Integer> vocab) {
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

	public static Map<String, Integer> merge_vocab(String pair, Map<String, Integer> old_vocab) {
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

	public static void main(String[] args) {
	    Map<String, Integer> vocab = new TreeMap<String, Integer>();
	    vocab.put("l o w", 5);
	    vocab.put("l o w e r", 2);
	    vocab.put("n e w e s t", 6);
	    vocab.put("w i d e s t", 3);
		int num_merges = 10;

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
			vocab = merge_vocab(best, vocab);
			PrintUtil.PrintMap(vocab);
		}
	}

}
