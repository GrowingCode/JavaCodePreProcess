package bpe.skt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;

import tree.Tree;
import tree.TreeNode;
import util.MapUtil;
import util.YStringUtil;

public class SktPETreesUtil {

	private static Map<TreeNodeTwoMerge, Integer> GetStats(Map<Tree, Integer> vocab) {
		Map<TreeNodeTwoMerge, Integer> pairs = new TreeMap<TreeNodeTwoMerge, Integer>();
		Set<Tree> vks = vocab.keySet();
		Iterator<Tree> vk_itr = vks.iterator();
		while (vk_itr.hasNext()) {
			Tree vk = vk_itr.next();
			TreeMap<String, TreeNode> all_nodes = vk.GetAllNodes();
			TreeNode rt = vk.GetRootNode();
			int freq = vocab.get(vk);
			Set<String> all_keys = all_nodes.keySet();
			Iterator<String> ai = all_keys.iterator();
			while (ai.hasNext()) {
				String key = ai.next();
				TreeNode val = all_nodes.get(key);
				TreeNode par_val = val.GetParent();
				if (par_val != null) {
					ArrayList<TreeNode> childs = par_val.GetChildren();
					int idx = childs.indexOf(val);
					Assert.isTrue(idx > -1);
					String mgd = YStringUtil.ReplaceSpecifiedContentInSpecifiedPosition(par_val.GetContent(), "#h", val.GetContent(), idx);
					TreeNodeTwoMerge mm = new TreeNodeTwoMerge(val.GetContent(), par_val.GetContent(), mgd);
					Integer n_freq = pairs.get(mm);
					if (n_freq == null) {
						n_freq = 0;
					}
					n_freq += freq;
					pairs.put(mm, n_freq);
				} else {
					Assert.isTrue(rt == val);
				}
			}
		}
		return pairs;
	}

	private static void MergeVocab(TreeNodeTwoMerge pair, Map<Tree, Integer> old_vocab) {
		Set<Tree> ov_set = old_vocab.keySet();
		Iterator<Tree> os_itr = ov_set.iterator();
		while (os_itr.hasNext()) {
			Tree os = os_itr.next();
			TreeMap<String, TreeNode> ans = os.GetAllNodes();
			Set<String> ans_keys = ans.keySet();
			for (String an_key : ans_keys) {
				TreeNode tn = ans.get(an_key);
				MergeTwoTreeNodes(pair, tn);
			}
		}
	}
	
	private static boolean MergeTwoTreeNodes(TreeNodeTwoMerge pair, TreeNode tn) {
		boolean really_merged = false;
		if (pair.GetParent().equals(tn.GetContent())) {
			ArrayList<TreeNode> childs = tn.GetChildren();
			int index = -1;
			int rm_index = -1;
			for (TreeNode child : childs) {
				index++;
				if (pair.GetNode().equals(child.GetContent())) {
					rm_index = index;
					break;
				}
			}
			if (rm_index > -1) {
				childs.remove(rm_index);
				tn.SetContent(pair.GetMerged());
				really_merged = true;
			}
		}
		return really_merged;
	}
	
	/**
	 * This function has side effect to paremeters
	 * @param vocab
	 * @param num_merges
	 * @return
	 */
	public static List<TreeNodeTwoMerge> GenerateSktPEMerges(Map<Tree, Integer> vocab, int num_merges) {
//		PrintUtil.PrintMap(vocab, "to_merge_vocab");
		List<TreeNodeTwoMerge> merges = new LinkedList<TreeNodeTwoMerge>();
//		if (num_merges == -1) {
//			num_merges = Integer.MAX_VALUE;
//		}
//		System.out.println("num_merges:" + num_merges);
		Assert.isTrue(num_merges > 0);
		for (int i=0;i<num_merges;i++) {
			Map<TreeNodeTwoMerge, Integer> pairs = GetStats(vocab);
//			System.out.println("pairs.size():" + pairs.size());
			if (pairs.size() == 0) {
				break;
			}
//			Collection<Integer> vals = pairs.values();
//			boolean should_stop = true;
//			for (Integer val : vals) {
//				if (val > 1) {
//					should_stop = false;
//				}
//			}
//			if (should_stop) {
//				break;
//			}
//			MapUtil.FindKeyWithMaxValue(pairs);
			TreeNodeTwoMerge best = MapUtil.FindKeyWithMaxValue(pairs);
			MergeVocab(best, vocab);
			merges.add(best);
		}
//		PrintUtil.PrintMap(vocab_r, "vocab_r_in_merging");
//		PrintUtil.PrintList(merges, "bep_merges");
		return merges;
	}
	
	public static Set<String> ExtractAllSktPEUnits(Collection<Tree> sktpe_raws) {
		Set<String> bpes = new TreeSet<String>();
		Iterator<Tree> vk_itr = sktpe_raws.iterator();
		while (vk_itr.hasNext()) {
			Tree vk = vk_itr.next();
			TreeMap<String, TreeNode> nodes = vk.GetAllNodes();
//			int freq = vocab.get(vk);
			bpes.addAll(nodes.keySet());
		}
		return bpes;
	}
	
	public static SktPEHandledResult ApplySktPEMergesToTrees(List<TreeNodeTwoMerge> merges, Collection<Tree> skts, TreeMap<String, ArrayList<String>> token_composes) {
		SktPEHandledResult result = new SktPEHandledResult();
		for (TreeNodeTwoMerge merge : merges) {
			boolean merge_useful = false;
			for (Tree skt : skts) {
				TreeMap<String, TreeNode> nodes = skt.GetAllNodes();
				Collection<TreeNode> tns = nodes.values();
				for (TreeNode tn : tns) {
					boolean curr_useful = MergeTwoTreeNodes(merge, tn);
					merge_useful = merge_useful | curr_useful;
				}
			}
			if (merge_useful) {
				Assert.isTrue(!token_composes.containsKey(merge.GetMerged()));
				ArrayList<String> ll = new ArrayList<String>();
				String t0 = merge.GetMerged();
				token_composes.put(t0, ll);
				String t1 = merge.GetParent();
				String t2 = merge.GetNode();
				if (token_composes.containsKey(t1)) {
					ll.addAll(token_composes.get(t1));
				} else {
					ll.add(t1);
				}
				if (token_composes.containsKey(t2)) {
					ll.addAll(token_composes.get(t2));
				} else {
					ll.add(t2);
				}
			}
		}
		result.vobs.addAll(ExtractAllSktPEUnits(skts));
		return result;
	}

}

