package bpe.skt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

import bpe.skt.util.SktMergeApplyCondition;
import eclipse.jdt.JDTASTHelper;
import logger.DebugLogger;
import main.MetaOfApp;
import statis.trans.common.RoleAssigner;
import tree.Forest;
import tree.Tree;
import tree.TreeNode;
import unit.PairContainer;
import util.MapUtil;
import util.YStringUtil;

public class SktPETreesUtil {

	private static Map<TreeNodeTwoMerge, PairContainer<Integer, Integer>> GetStats(ArrayList<Forest> pfs) {// ArrayList<Tree> vocab
		Map<TreeNodeTwoMerge, PairContainer<Integer, Integer>> pairs = new TreeMap<TreeNodeTwoMerge, PairContainer<Integer, Integer>>();
//		Set<Tree> vks = vocab.keySet();
		for (Forest pf : pfs) {
			ArrayList<Tree> trees = pf.GetAllTrees();
			Iterator<Tree> vk_itr = trees.iterator();
	//		Iterator<Tree> vk_itr = vocab.iterator();
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
						PairContainer<Integer, Integer> n_freq = pairs.get(mm);
						if (n_freq == null) {
							n_freq = new PairContainer<Integer, Integer>(0, 0);
							pairs.put(mm, n_freq);
						}
						if (pf.GetRole() <= RoleAssigner.train_k) {
							n_freq.k += freq;
						} else {
							n_freq.v += freq;
						}
					} else {
						Assert.isTrue(rt == val || not_merge);
					}
				}
			}
		}
		return pairs;
	}
	
	private static void AssertAllMerged(ArrayList<Forest> pfs) {// ArrayList<Tree> vocab
		for (Forest pf : pfs) {
			ArrayList<Tree> trees = pf.GetAllTrees();
			Iterator<Tree> vk_itr = trees.iterator();
			while (vk_itr.hasNext()) {
				Tree vk = vk_itr.next();
				ArrayList<TreeNode> all_nodes = vk.GetAllNodes();
				Assert.isTrue(all_nodes.size() == 1);
			}
		}
	}

	private static void MergeVocab(TreeNodeTwoMerge pair, ArrayList<Forest> pfs) {// ArrayList<Tree> old_vocab
//		Set<Tree> ov_set = old_vocab.keySet();
		for (Forest pf : pfs) {
			ArrayList<Tree> trees = pf.GetAllTrees();
			Iterator<Tree> os_itr = trees.iterator();
			while (os_itr.hasNext()) {
				Tree os = os_itr.next();
				os.ApplyMerge(pair);
//				ArrayList<TreeNode> ans = os.GetAllNodes();
////				Set<String> ans_keys = ans.keySet();
//				for (TreeNode tn : ans) {
////					TreeNode tn = ans.get(an_key);
//					MergeTwoTreeNodes(pair, tn);
//				}
			}
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
	public static List<TreeNodeTwoMergeWithFreqs> GenerateSktPEMerges(ArrayList<Forest> pfs) {// ArrayList<Tree> vocab, int num_merges
//		DebugLogger.Log("all trees size:" + vocab.size());
//		PrintUtil.PrintMap(vocab, "to_merge_vocab");
		Map<TreeNodeTwoMergeWithFreqs, Integer> merges = new TreeMap<TreeNodeTwoMergeWithFreqs, Integer>();
//		if (num_merges == -1) {
//			num_merges = Integer.MAX_VALUE;
//		}
//		System.out.println("num_merges:" + num_merges);
//		Assert.isTrue(num_merges > 0);
//		for (int i=0;i<num_merges;i++) {
		int turn = 0;
		while (true) {
			Map<TreeNodeTwoMerge, PairContainer<Integer, Integer>> pairs = GetStats(pfs);
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
			PairContainer<TreeNodeTwoMerge, PairContainer<Integer, Integer>> best = FindKeyValuePairWithMaxValueWithFiltering(pairs);
			if (best == null) {
				break;
			}
			int merge_total_exist = best.v.k + best.v.v;
			if (merge_total_exist < MetaOfApp.MinimumThresholdOfSkeletonMerge) {
				break;
			}
			turn++;
			MergeVocab(best.k, pfs);
			String merge_info = "turn " + turn + "#best.v exist in train:" + best.v.k + "#best.v exist in test:" + best.v.v + "#current merge:" + best.k;
			DebugLogger.Log(merge_info);
			Assert.isTrue(!merges.containsKey(best.k), merge_info + "#existed turn:" + merges.get(best.k));
			merges.put(new TreeNodeTwoMergeWithFreqs(best.k, merge_total_exist), turn);
		}
		
		int pred = MetaOfApp.MinimumThresholdOfSkeletonMerge;
		if (pred == 1) {
			AssertAllMerged(pfs);
		}
//		PrintUtil.PrintMap(vocab_r, "vocab_r_in_merging");
//		PrintUtil.PrintList(merges, "bep_merges");
		List<Entry<TreeNodeTwoMergeWithFreqs, Integer>> smbv = MapUtil.SortMapByValue(merges);
		ArrayList<TreeNodeTwoMergeWithFreqs> res = new ArrayList<TreeNodeTwoMergeWithFreqs>();
		for (Entry<TreeNodeTwoMergeWithFreqs, Integer> e : smbv) {
			res.add(e.getKey());
		}
		return res;
	}
	
	private static PairContainer<TreeNodeTwoMerge, PairContainer<Integer, Integer>> FindKeyValuePairWithMaxValueWithFiltering(Map<TreeNodeTwoMerge, PairContainer<Integer, Integer>> pairs) {
		TreeNodeTwoMerge max_tn = null;
		Integer max_k = null;
		Integer max_t = null;
		Integer max = null;
		Set<TreeNodeTwoMerge> tns = pairs.keySet();
		Iterator<TreeNodeTwoMerge> tn_itr = tns.iterator();
		while (tn_itr.hasNext()) {
			TreeNodeTwoMerge tn = tn_itr.next();
			PairContainer<Integer, Integer> p_t = pairs.get(tn);
			boolean apply_meet = SktMergeApplyCondition.MeetTrainTestJoinCondition(p_t.k, p_t.v);
			if (!MetaOfApp.GenFilterTrainTestJoinTreeMerge || apply_meet) {
				Integer t = p_t.k + p_t.v;
//				Integer cmp = t;
				if (max == null) {
					max_tn = tn;
					max_k = p_t.k;
					max_t = p_t.v;
					max = t;
				} else {
					if (max.compareTo(t) < 0) {
						max_tn = tn;
						max_k = p_t.k;
						max_t = p_t.v;
						max = t;
					}
				}
			}
		}
		if (max_tn == null || max_k == null || max_t == null || max == null) {
			Assert.isTrue(max_tn == null && max_k == null && max_t == null && max == null);
			return null;
		}
		return new PairContainer<TreeNodeTwoMerge, PairContainer<Integer, Integer>>(max_tn, new PairContainer<Integer, Integer>(max_k, max_t));
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
	
	public static void ApplySktPEMergesToTrees(ArrayList<Forest> pfs, List<TreeNodeTwoMergeWithFreqs> merges, int merge_num) {// , TreeMap<String, ArrayList<String>> token_composes
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
		int pf_size = pfs.size();
//		int t_size = skts.size();
//			boolean merge_useful = false;
//			TreeNodeTwoMerge marked_merge = merge;
		int valid_merge_num = 0;
		int r_m_size = Math.min(merge_num, m_size);
		for (int i=0;i<r_m_size;i++) {
			TreeNodeTwoMergeWithFreqs merge = merges.get(i);
			int exist_in_train = 0;
			int exist_in_test = 0;
//			if (MetaOfApp.ApplyTrainTestJoinTreeMerge) {
			for (int j=0;j<pf_size;j++) {
				Forest pf = pfs.get(j);
				ArrayList<Tree> skts = pf.GetAllTrees();
				for (Tree skt : skts) {
					int pmc = skt.PossibleMergeCount(merge);
					if (pmc > 0) {
						if (pf.GetRole() <= RoleAssigner.train_k) {
							exist_in_train += pmc;
						} else {
							exist_in_test += pmc;
						}
					}
				}
			}
//			}
			int mm_count = 0;
			boolean meet_join = SktMergeApplyCondition.MeetTrainTestJoinCondition(exist_in_train, exist_in_test);
			int r_exist_in_train = 0;
			int r_exist_in_test = 0;
			if (meet_join || !MetaOfApp.ApplyTrainTestJoinTreeMerge) {
				for (int j=0;j<pf_size;j++) {
					Forest pf = pfs.get(j);
					ArrayList<Tree> skts = pf.GetAllTrees();
					for (Tree skt : skts) {
						int merge_count = skt.ApplyMerge(merge);
						mm_count += merge_count;
						if (pf.GetRole() <= RoleAssigner.train_k) {
							r_exist_in_train += merge_count;
						} else {
							r_exist_in_test += merge_count;
						}
					}
				}
				if (mm_count > 0) {
					valid_merge_num++;
				}
			}
//			"All tree size:" + t_size + 
			String merge_info = "#merge turn:" + i + "#apply join:" + MetaOfApp.ApplyTrainTestJoinTreeMerge + "#meet_join:" + meet_join + "#mm_count:" + mm_count + "#merge freq in witness:" + merge.GetFreqs() + "#exist_in_train:" + exist_in_train + "#exist_in_test:" + exist_in_test + "#r_exist_in_train:" + r_exist_in_train + "#r_exist_in_test:" + r_exist_in_test + " #handled merge:" + merge;
			DebugLogger.Log(merge_info);
		}
		System.out.println("valid_merge_num:" + valid_merge_num);
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
