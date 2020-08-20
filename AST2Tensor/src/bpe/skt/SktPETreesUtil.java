package bpe.skt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;

import tree.Tree;
import tree.TreeNode;
import util.MapUtil;
import util.PrintUtil;
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
				Assert.isTrue(!key.equals("#h") && !key.equals("#v"), "wrong key:" + key);
				TreeNode val = all_nodes.get(key);
				TreeNode par_val = val.GetParent();
				if (par_val != null) {
					ArrayList<TreeNode> childs = par_val.GetChildren();
					int idx = childs.indexOf(val);
					Assert.isTrue(idx > -1);
//					"#h", 
					String mgd = YStringUtil.ReplaceSpecifiedContentInSpecifiedPosition(par_val.GetContent(), val.GetContent(), idx);
					String r_par_val = par_val.GetContent();
//					if (JDTASTHelper.IsIDLeafNode(val.GetClazz())) {
//						r_par_val = YStringUtil.ReplaceSpecifiedContentInSpecifiedPosition(par_val.GetContent(), "#m", idx);
//					}
					TreeNodeTwoMerge mm = new TreeNodeTwoMerge(val.GetContent(), val.GetSiblingIndex(), r_par_val, mgd);
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
				if (pair.GetNode().equals(child.GetContent()) && child.GetSiblingIndex() == pair.GetNodeIndex()) {
					rm_index = index;
					break;
				}
			}
			if (rm_index > -1) {
//				TreeNode rm_tn = 
				childs.remove(rm_index);
//				Assert.isTrue(rm_tn.GetChildren().size() == 0, "strange, children size:" + rm_tn.GetChildren().size() + "@strange child node:" + rm_tn.GetContent() + "@strange par node:" + tn.GetContent());
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
		Set<TreeNodeTwoMerge> merges = new TreeSet<TreeNodeTwoMerge>();
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
			Assert.isTrue(!merges.contains(best));
			merges.add(best);
		}
//		PrintUtil.PrintMap(vocab_r, "vocab_r_in_merging");
//		PrintUtil.PrintList(merges, "bep_merges");
		return new ArrayList<TreeNodeTwoMerge>(merges);
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
		Assert.isTrue(token_composes.isEmpty(), "size:" + token_composes.size());
		SktPEHandledResult result = new SktPEHandledResult();
		int m_size = merges.size();
		for (int i=0;i<m_size;i++) {
			TreeNodeTwoMerge merge = merges.get(i);
			boolean merge_useful = false;
			TreeNodeTwoMerge marked_merge = merge;
			for (Tree skt : skts) {
				TreeMap<String, TreeNode> nodes = skt.GetAllNodes();
				Collection<TreeNode> tns = nodes.values();
				for (TreeNode tn : tns) {
					boolean curr_useful = MergeTwoTreeNodes(merge, tn);// MergeTwoTreeNodesWhileMarkMergePoint
					merge_useful = merge_useful | curr_useful;
//					if (marked_merge == null) {
//						marked_merge = mark_merge.marked_merge;
//					} else {
//						if (mark_merge.merge_useful) {
//							Assert.isTrue(marked_merge.equals(mark_merge.marked_merge), "one:" + marked_merge + "======" + "two:" + mark_merge.marked_merge);
//						}
//					}
				}
			}
			if (merge_useful) {
				ArrayList<String> ll = new ArrayList<String>();
				String t0 = marked_merge.GetMerged();
				String t1 = marked_merge.GetParent();
				String t2 = marked_merge.GetNode();
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
				Assert.isTrue(!token_composes.containsKey(t0), "conflict key:" + t0 + "====already list:" + PrintUtil.PrintListToString(token_composes.get(t0), "") + "====income list:" + PrintUtil.PrintListToString(ll, "") + "====merge:" + merge + "====index1:" + merges.indexOf(merge) + "====index2:" + merges.lastIndexOf(merge) + "====i:" + i);
				token_composes.put(t0, ll);
			}
		}
		result.vobs.addAll(ExtractAllSktPEUnits(skts));
		return result;
	}
	
//	private static MarkMerge MergeTwoTreeNodesWhileMarkMergePoint(TreeNodeTwoMerge pair, TreeNode tn) {
//		boolean really_merged = false;
//		TreeNodeTwoMerge mark_merge = null;
//		if (pair.GetParent().equals(tn.GetContent())) {
//			ArrayList<TreeNode> childs = tn.GetChildren();
//			int index = -1;
//			int rm_index = -1;
//			for (TreeNode child : childs) {
//				index++;
//				if (pair.GetNode().equals(child.GetContent())) {
//					rm_index = index;
//					break;
//				}
//			}
//			if (rm_index > -1) {
//				TreeNode rm_tn = childs.remove(rm_index);
////				Assert.isTrue(rm_tn.GetChildren().size() == 0, "strange, children size:" + rm_tn.GetChildren().size() + "@strange child node:" + rm_tn.GetContent() + "@strange par node:" + tn.GetContent());
//				really_merged = true;
//				String r_par_val = tn.GetContent();
////				if (JDTASTHelper.IsIDLeafNode(rm_tn.GetClazz())) {
////					try {
////					r_par_val = YStringUtil.ReplaceSpecifiedContentInSpecifiedPosition(r_par_val, "#m", rm_index);
////					} catch (Exception e) {
////						System.out.println("child_content:" + rm_tn.GetContent());
////						throw e;
////					}
////				}
//				mark_merge = new TreeNodeTwoMerge(rm_tn.GetContent(), r_par_val, pair.GetMerged());
//				tn.SetContent(pair.GetMerged());
//			}
//		}
//		return new MarkMerge(really_merged, mark_merge);
//	}

}

class MarkMerge {
	
	boolean merge_useful = false;
	TreeNodeTwoMerge marked_merge = null;
	
	public MarkMerge(boolean merge_useful, TreeNodeTwoMerge marked_merge) {
		this.merge_useful = merge_useful;
		this.marked_merge = marked_merge;
	}
	
}
