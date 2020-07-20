package bpe.skt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import statis.trans.common.RoleAssigner;
import statis.trans.common.SkeletonForestRecorder;
import statistic.IDTools;
import translation.TensorTools;
import translation.tensor.TensorForProject;
import tree.Forest;
import tree.ProjectForests;
import tree.Tree;
import tree.TreeNode;

public class SktLogicUtil {
	
	public static void CountPairEncodedSkeletons(IDTools id_tool, SkeletonForestRecorder sfr) {
		ArrayList<Forest> fs = sfr.GetAllForests();
		for (Forest f : fs) {
			int role = f.GetRole();
			ArrayList<Tree> ts = f.GetAllTrees();
			for (Tree t : ts) {
				TreeMap<String, TreeNode> ans = t.GetAllNodes();
				Set<String> ans_keys = ans.keySet();
				Iterator<String> ans_itr = ans_keys.iterator();
				while (ans_itr.hasNext()) {
					String an = ans_itr.next();
					if (role <= RoleAssigner.train_seen_k) {
						id_tool.str.TokenHitInTrainSet(an, 1);
					} else {
						id_tool.str.TokenNotHitInTrainSet(an, 1);
					}
				}
			}
		}
	}
	
	public static void TranslatePairEncodedSkeletonsAndTokens(TensorTools tensor_tool, SkeletonForestRecorder sfr) {
		ArrayList<ProjectForests> aps = sfr.GetAllProjectsWithForests();
		for (ProjectForests pf : aps) {
			TensorForProject tfp = new TensorForProject(kind);
			;
		}
	}
	
}
