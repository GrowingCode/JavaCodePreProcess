package bpe;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SubWords {
	
	public static Map<String, Integer> get_stats(Map<String, Integer> vocab) {
		Map<String, Integer> pairs = new TreeMap<String, Integer>();
		Set<String> vks = vocab.keySet();
		Iterator<String> vk_itr = vks.iterator();
		while (vk_itr.hasNext()) {
			String vk = vk_itr.next();
			String[] vk_sbs = vk.split(" ");
			int freq = vocab.get(vk);
			for (int i=0;i<vk_sbs.length-1;i++) {
				String nk = vk_sbs[i] + "," + vk_sbs[i+1];
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
	    bigram = re.escape(' '.join(pair))
	    p = re.compile(r'(?     for word in v_in:
	      w_out = p.sub(''.join(pair), word)
	      print("w_out    ",w_out)
	      v_out[w_out] = v_in[word]
	    return new_vocab;
	}

	public static void main() {
	    Map<String, Integer> vocab = new TreeMap<String, Integer>();
	    vocab.put("l o w", 5);
	    vocab.put("l o w e r", 5);
	    vocab.put("n e w e s t", 6);
	    vocab.put("w i d e s t", 3);
		int num_merges = 10;

		for (int i=0;i<num_merges;i++) {
			System.out.println("=#####################################=== ");
			Map<String, Integer> pairs = get_stats(vocab);
		  
			best = max(pairs, key=pairs.get)
			vocab = merge_vocab(best, vocab)
			print("vocab   ",vocab)
		}
	}

}

