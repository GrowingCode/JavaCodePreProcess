package bpe.skt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

import eclipse.jdt.JDTASTHelper;
import logger.DebugLogger;
import main.MetaOfApp;
import tree.Tree;
import tree.TreeNode;
import unit.PairContainer;
import util.MapUtil;
import util.YStringUtil;

public class SktPETreesUtil {

	private static Map<TreeNodeTwoMerge, Integer> GetStats(ArrayList<Tree> vocab) {
		Map<TreeNodeTwoMerge, Integer> pairs = new TreeMap<TreeNodeTwoMerge, Integer>();
//		Set<Tree> vks = vocab.keySet();
		Iterator<Tree> vk_itr = vocab.iterator();
		while (vk_itr.hasNext()) {
			Tree vk = vk_itr.next();
			ArrayList<TreeNode> all_nodes = vk.GetAllNodes();
			TreeNode rt = vk.GetRootNode();
			int freq = 1;// vocab.get(vk);
//			Set<String> all_keys = all_nodes.keySet();
//			Iterator<String> ai = all_keys.iterator();
//			while (ai.hasNext()) {
			for (TreeNode val : all_nodes) {
//				String key = ai.next();
//				Assert.isTrue(!key.equals("#h") && !key.equals("#v"), "wrong key:" + key);
//				TreeNode val = all_nodes.get(key);
				TreeNode par_val = val.GetParent();
				boolean not_merge = MetaOfApp.NotMergeIDLeaf && JDTASTHelper.IsIDLeafNode(val.GetClazz());
				
				if (par_val != null && !not_merge) {
					ArrayList<TreeNode> childs = par_val.GetChildren();
					int idx = childs.indexOf(val);
					Assert.isTrue(idx > -1);
//					"#h", 
					String mgd = YStringUtil.ReplaceSpecifiedContentInSpecifiedPosition(par_val.GetContent(), val.GetContent(), idx);
					String r_par_val = par_val.GetContent();
//					if (JDTASTHelper.IsIDLeafNode(val.GetClazz())) {
//						r_par_val = YStringUtil.ReplaceSpecifiedContentInSpecifiedPosition(par_val.GetContent(), "#m", idx);
//					}
					TreeNodeTwoMerge mm = new TreeNodeTwoMerge(val.GetContent(), idx, r_par_val, mgd);//val.GetSiblingIndex()
					Integer n_freq = pairs.get(mm);
					if (n_freq == null) {
						n_freq = 0;
					}
					n_freq += freq;
					pairs.put(mm, n_freq);
				} else {
					Assert.isTrue(rt == val || not_merge);
				}
			}
		}
		return pairs;
	}
	
	private static void AssertAllMerged(ArrayList<Tree> vocab) {
		Iterator<Tree> vk_itr = vocab.iterator();
		while (vk_itr.hasNext()) {
			Tree vk = vk_itr.next();
			ArrayList<TreeNode> all_nodes = vk.GetAllNodes();
			Assert.isTrue(all_nodes.size() == 1);
		}
	}

	private static void MergeVocab(TreeNodeTwoMerge pair, ArrayList<Tree> old_vocab) {
//		Set<Tree> ov_set = old_vocab.keySet();
		Iterator<Tree> os_itr = old_vocab.iterator();
		while (os_itr.hasNext()) {
			Tree os = os_itr.next();
			os.ApplyMerge(pair);
//			ArrayList<TreeNode> ans = os.GetAllNodes();
////			Set<String> ans_keys = ans.keySet();
//			for (TreeNode tn : ans) {
////				TreeNode tn = ans.get(an_key);
//				MergeTwoTreeNodes(pair, tn);
//			}
		}
	}
	
//	private static boolean MergeTwoTreeNodes(TreeNodeTwoMerge pair, TreeNode tn) {
//		boolean really_merged = false;
//		if (pair.GetParent().equals(tn.GetContent())) {
//			ArrayList<TreeNode> childs = tn.GetChildren();
//			int index = -1;
//			int rm_index = -1;
//			for (TreeNode child : childs) {
//				index++;
//				if (pair.GetNode().equals(child.GetContent()) && index == pair.GetNodeIndex()) {//child.GetSiblingIndex()
//					rm_index = index;
//					break;
//				}
//			}
//			if (rm_index > -1) {
////				TreeNode rm_tn = 
//				TreeNode child = childs.remove(rm_index);
//				ArrayList<TreeNode> ccs = child.GetChildren();
//				int ccs_len = ccs.size();
//				for (int cl = ccs_len - 1; cl >= 0; cl--) {
//					TreeNode child_child = ccs.get(cl);
//					childs.add(rm_index, child_child);
//					child_child.SetParent(tn);
//				}
////				Assert.isTrue(rm_tn.GetChildren().size() == 0, "strange, children size:" + rm_tn.GetChildren().size() + "@strange child node:" + rm_tn.GetContent() + "@strange par node:" + tn.GetContent());
//				tn.SetContent(pair.GetMerged());
//				really_merged = true;
//			}
//		}
//		return really_merged;
//	}
	
	/**
	 * This function has side effect to parameters
	 * @param vocab
	 * @param num_merges
	 * @return
	 */
	public static List<TreeNodeTwoMerge> GenerateSktPEMerges(ArrayList<Tree> vocab) {// , int num_merges
		DebugLogger.Log("all trees size:" + vocab.size());
//		PrintUtil.PrintMap(vocab, "to_merge_vocab");
		Map<TreeNodeTwoMerge, Integer> merges = new TreeMap<TreeNodeTwoMerge, Integer>();
//		if (num_merges == -1) {
//			num_merges = Integer.MAX_VALUE;
//		}
//		System.out.println("num_merges:" + num_merges);
//		Assert.isTrue(num_merges > 0);
//		for (int i=0;i<num_merges;i++) {
		int turn = 0;
		while (true) {
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
			PairContainer<TreeNodeTwoMerge, Integer> best = MapUtil.FindKeyValuePairWithMaxValue(pairs);
			if (best.v < MetaOfApp.MinimumThresholdOfSkeletonMerge) {
				break;
			}
			turn++;
			DebugLogger.Log("best.v:" + best.v + " turn " + turn + " stats over");
			MergeVocab(best.k, vocab);
			String merge_info = "already exist merge:" + best.k + " best.v:" + best.v + "#existing merge turn:" + merges.get(best.k);
			DebugLogger.Log(merge_info);
			Assert.isTrue(!merges.containsKey(best.k), merge_info);
			merges.put(best.k, turn);
		}
		
		int pred = MetaOfApp.MinimumThresholdOfSkeletonMerge;
		if (pred == 1) {
			AssertAllMerged(vocab);
		}
//		PrintUtil.PrintMap(vocab_r, "vocab_r_in_merging");
//		PrintUtil.PrintList(merges, "bep_merges");
		List<Entry<TreeNodeTwoMerge, Integer>> smbv = MapUtil.SortMapByValue(merges);
		ArrayList<TreeNodeTwoMerge> res = new ArrayList<TreeNodeTwoMerge>();
		for (Entry<TreeNodeTwoMerge, Integer> e : smbv) {
			res.add(e.getKey());
		}
		return res;
	}
	
//	public static Set<String> ExtractAllSktPEUnits(Collection<Tree> sktpe_raws) {
//		Set<String> bpes = new TreeSet<String>();
//		Iterator<Tree> vk_itr = sktpe_raws.iterator();
//		while (vk_itr.hasNext()) {
//			Tree vk = vk_itr.next();
//			TreeMap<String, TreeNode> nodes = vk.GetAllNodes();
////			int freq = vocab.get(vk);
//			bpes.addAll(nodes.keySet());
//		}
//		return bpes;
//	}
	
	public static void ApplySktPEMergesToTrees(List<TreeNodeTwoMerge> merges, Collection<Tree> skts, int merge_num) {// , TreeMap<String, ArrayList<String>> token_composes
		/* Tree skt_first = skts.iterator().next();
		ArrayList<TreeNode> skt_first_all_nodes = skt_first.GetAllNodes();
		boolean encounter = false;
		for (TreeNodeTwoMerge merge : merges) {
			if (merge.GetNode().equals("#v #v")) {
				//  && merge.GetParent().equals("#h #h #v(#h) #h")
				System.out.println(merge);
				encounter = true;
			}
		}
		if (!encounter) {
			System.out.println("Not Encountered!");
		}
		for (TreeNode skt_first_node : skt_first_all_nodes) {
			System.out.println("skt_first_node:" + skt_first_node.GetContent());
		} */
//		Assert.isTrue(token_composes.isEmpty(), "size:" + token_composes.size());
//		SktPEHandledResult result = new SktPEHandledResult();
		int m_size = merges.size();
		int t_size = skts.size();
//			boolean merge_useful = false;
//			TreeNodeTwoMerge marked_merge = merge;
		int r_m_size = Math.min(merge_num, m_size);
		for (int i=0;i<r_m_size;i++) {
			TreeNodeTwoMerge merge = merges.get(i);
			for (Tree skt : skts) {
//				TreeMap<String, TreeNode> nodes = skt.GetAllNodes();
//				Collection<TreeNode> tns = nodes.values();
//			TreeMap<String, ArrayList<TreeNode>> mp = skt.GetAllContentNodeMap();
				skt.ApplyMerge(merge);
//				ArrayList<TreeNode> tns = skt.GetAllNodes();
//				for (TreeNode tn : tns) {
//					boolean curr_useful =  
//					MergeTwoTreeNodesWithNewMergeNodeCreated(skt, mp, merge);// MergeTwoTreeNodesWhileMarkMergePoint
//					merge_useful = merge_useful | curr_useful;
//					if (marked_merge == null) {
//						marked_merge = mark_merge.marked_merge;
//					} else {
//						if (mark_merge.merge_useful) {
//							Assert.isTrue(marked_merge.equals(mark_merge.marked_merge), "one:" + marked_merge + "======" + "two:" + mark_merge.marked_merge);
//						}
//					}
//				}
			}
			String merge_info = "All tree size:" + t_size + "#merge turn:" + i;//  + " #handled merge:" + merge
			DebugLogger.Log(merge_info);
		}
//			if (merge_useful) {
//				ArrayList<String> ll = new ArrayList<String>();
//				String t0 = marked_merge.GetMerged();
//				String t1 = marked_merge.GetParent();
//				String t2 = marked_merge.GetNode();
//				if (token_composes.containsKey(t1)) {
//					ll.addAll(token_composes.get(t1));
//				} else {
//					ll.add(t1);
//				}
//				if (token_composes.containsKey(t2)) {
//					ll.addAll(token_composes.get(t2));
//				} else {
//					ll.add(t2);
//				}
//				Assert.isTrue(!token_composes.containsKey(t0), "conflict key:" + t0 + "====already list:" + PrintUtil.PrintListToString(token_composes.get(t0), "") + "====income list:" + PrintUtil.PrintListToString(ll, "") + "====merge:" + merge + "====index1:" + merges.indexOf(merge) + "====index2:" + merges.lastIndexOf(merge) + "====i:" + i);
//				token_composes.put(t0, ll);
//			}
//		result.vobs.addAll(ExtractAllSktPEUnits(skts));
//		return result;
	}
	
	public static void PreProcessAllTrees(ArrayList<Tree> all_trees) {
		for (Tree t : all_trees) {
			t.PreProcessTree();
		}
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
