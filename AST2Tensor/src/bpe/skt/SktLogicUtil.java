package bpe.skt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import eclipse.project.ProjectInfo;
import statis.trans.common.RoleAssigner;
import statis.trans.common.SkeletonForestRecorder;
import statistic.IDTools;
import translation.TensorTools;
import translation.tensor.TensorForProject;
import tree.Forest;
import tree.ProjectForests;
import tree.Tree;
import tree.TreeFlatten;
import tree.TreeNode;

public class SktLogicUtil {
	
	public static void CountPairEncodedSkeletons(IDTools id_tool, SkeletonForestRecorder sfr) {
		ArrayList<Forest> fs = sfr.GetAllForests();
		for (Forest f : fs) {
			int role = f.GetRole();
			ArrayList<Tree> ts = f.GetAllTrees();
			for (Tree t : ts) {
				TreeFlatten tf = t.FlattenTree(sfr.GetAllTokenComposes());
				{
					String an= tf.skt_one_struct.get(0);
					if (role <= RoleAssigner.train_seen_k) {
						id_tool.one_struct_r.TokenHitInTrainSet(an, 1);
					} else {
						id_tool.one_struct_r.TokenNotHitInTrainSet(an, 1);
					}
				}
				{
					for (String an : tf.skt_pe_struct) {
						if (role <= RoleAssigner.train_seen_k) {
							id_tool.pe_struct_r.TokenHitInTrainSet(an, 1);
						} else {
							id_tool.pe_struct_r.TokenNotHitInTrainSet(an, 1);
						}
					}
				}
				{
					for (String an : tf.skt_e_struct) {
						if (role <= RoleAssigner.train_seen_k) {
							id_tool.e_struct_r.TokenHitInTrainSet(an, 1);
						} else {
							id_tool.e_struct_r.TokenNotHitInTrainSet(an, 1);
						}
					}
				}
				{
					for (String an : tf.skt_token) {
						if (role <= RoleAssigner.train_seen_k) {
							id_tool.s_tr.TokenHitInTrainSet(an, 1);
						} else {
							id_tool.s_tr.TokenNotHitInTrainSet(an, 1);
						}
					}
				}
//				TreeMap<String, TreeNode> ans = t.GetAllNodes();
//				Set<String> ans_keys = ans.keySet();
//				Iterator<String> ans_itr = ans_keys.iterator();
//				while (ans_itr.hasNext()) {
//					String an = ans_itr.next();
//					if (role <= RoleAssigner.train_seen_k) {
//						id_tool.str.TokenHitInTrainSet(an, 1);
//					} else {
//						id_tool.str.TokenNotHitInTrainSet(an, 1);
//					}
//				}
			}
		}
	}
	
	public static void TranslatePairEncodedSkeletonsAndTokens(TensorTools tensor_tool, SkeletonForestRecorder sfr) {
		ArrayList<ProjectForests> aps = sfr.GetAllProjectsWithForests();
		
		TreeMap<String, Integer> kind_index = new TreeMap<String, Integer>();
		ArrayList<TreeMap<String, ArrayList<String>>> proj_info = new ArrayList<TreeMap<String, ArrayList<String>>>();
		for (ProjectForests pf : aps) {
			ProjectInfo pi = pf.GetProjectInfo();
			TreeMap<String, ArrayList<String>> pi_name_funcs = new TreeMap<String, ArrayList<String>>();
			proj_info.add(pi_name_funcs);
			ArrayList<String> funcs = new ArrayList<String>();
			pi_name_funcs.put(pi.getName(), funcs);
			ArrayList<Forest> func_os = pf.GetAllForests();
			for (Forest f : func_os) {
				String s = f.GetSignature();
				int r = f.GetRole();
				Integer index = kind_index.get(r+"");
				if (index == null) {
					index = 0;
				} else {
					index++;
				}
				kind_index.put(r+"", index);
				String func_sig = s + r + index;
				funcs.add(func_sig);
			}
		}
		
		for (ProjectForests pf : aps) {
			TensorForProject tfp = new TensorForProject("Skt");
			TensorForProject tfp_pe = new TensorForProject("SktPE");
			TensorForProject tfp_each = new TensorForProject("SktEach");
			
			ArrayList<Forest> func_os = pf.GetAllForests();
			for (Forest f : func_os) {
				ArrayList<Tree> trees = f.GetAllTrees();
				for (Tree tree : trees) {
					TreeFlatten tf = tree.FlattenTree(sfr.GetAllTokenComposes());
					
				}
			}
		}
		
	}
	
}
