package bpe.skt;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

import com.google.gson.Gson;

import eclipse.project.ProjectInfo;
import main.MetaOfApp;
import statis.trans.common.RoleAssigner;
import statis.trans.common.SkeletonForestRecorder;
import statistic.IDTools;
import statistic.id.IDManager;
import translation.TensorTools;
import translation.ast.StatementScoreGenerator;
import translation.tensor.StatementSkeletonTensor;
import translation.tensor.Tensor;
import translation.tensor.TensorForProject;
import translation.tensor.TensorInfo;
import translation.tensor.util.IntsWrapper;
import tree.Forest;
import tree.ProjectForests;
import tree.Tree;
import tree.TreeFlatten;
import unit.PairContainer;
import util.FileUtil;
import util.IntegerMapUtil;
import util.ListUtil;
import util.MapUtil;
import util.PrintUtil;
import util.YStringUtil;

public class SktLogicUtil {

	public static void CountPairEncodedSkeletons(IDTools id_tool, SkeletonForestRecorder sfr) {
		ArrayList<Forest> fs = sfr.GetAllForests();
		for (Forest f : fs) {
			int role = f.GetRole();
			ArrayList<Tree> ts = f.GetAllTrees();
			for (Tree t : ts) {
//				t.FlattenTree();// sfr.GetAllTokenComposes()
				TreeFlatten tf = t.GetTreeFlattenResult();
				{
					String an = tf.skt_one_struct.get(0);
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
					for (String an : tf.skt_one_e_struct) {
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
	
	public static void FilterPairEncodedSkeletonsAndTokens(TensorTools tensor_tool, SkeletonForestRecorder sfr)
			throws Exception {
		IDManager im = tensor_tool.im;
		ArrayList<ProjectForests> aps = sfr.GetAllProjectsWithForests();
		Map<PairContainer<Forest, ProjectForests>, Double> forest_score = new HashMap<PairContainer<Forest, ProjectForests>, Double>();
		for (ProjectForests pf : aps) {
			ArrayList<Forest> func_os = pf.GetAllForests();
			for (Forest f : func_os) {
				StatementScoreGenerator ssg = new StatementScoreGenerator(im);
				ArrayList<Tree> trees = f.GetAllTrees();
				for (Tree tree : trees) {
					tree.Accept(ssg);
				}
				forest_score.put(new PairContainer<Forest, ProjectForests>(f, pf), ssg.GetScore());
			}
		}
		List<Entry<PairContainer<Forest, ProjectForests>, Double>> sorted = MapUtil.SortMapByValue(forest_score);
		Entry<PairContainer<Forest, ProjectForests>, Double> first = sorted.get(0);
		Entry<PairContainer<Forest, ProjectForests>, Double> last = sorted.get(sorted.size()-1);
		System.out.println("first score:" + first.getValue() + "#last score:" + last.getValue());
		int r_start = -1;
		int i_len = sorted.size();
		Iterator<Entry<PairContainer<Forest, ProjectForests>, Double>> s_itr = sorted.iterator();
		while (s_itr.hasNext()) {
			r_start++;
			Entry<PairContainer<Forest, ProjectForests>, Double> s = s_itr.next();
			if (s.getValue() > MetaOfApp.FilterMinimumScore) {
				break;
			}
		}
		List<Entry<PairContainer<Forest, ProjectForests>, Double>> sub_sorts = sorted.subList(r_start, i_len);
		int r_size = sub_sorts.size();
		double remove_size = r_size * MetaOfApp.FilterRate;
		
		double train_remove_size = remove_size * (MetaOfApp.train - (-1)) / (MetaOfApp.all * 1.0);
		double valid_remove_size = remove_size * (MetaOfApp.valid - MetaOfApp.train) / (MetaOfApp.all * 1.0);
		double test_remove_size = remove_size * (MetaOfApp.test - MetaOfApp.valid) / (MetaOfApp.all * 1.0);
		
		int r_train_remove_size = (int) Math.ceil(train_remove_size);
		int r_valid_remove_size = (int) Math.ceil(valid_remove_size);
		int r_test_remove_size = (int) Math.ceil(test_remove_size);
		
		Iterator<Entry<PairContainer<Forest, ProjectForests>, Double>> ss_itr = sub_sorts.iterator();
		while (ss_itr.hasNext()) {
			Entry<PairContainer<Forest, ProjectForests>, Double> ss = ss_itr.next();
			PairContainer<Forest, ProjectForests> key = ss.getKey();
			Forest f = key.k;
			ProjectForests pf = key.v;
			if (f.GetRole() == RoleAssigner.train_k) {
				if (r_train_remove_size > 0) {
					r_train_remove_size--;
					pf.GetAllForests().remove(f);
				}
			} else if (f.GetRole() == RoleAssigner.valid_k) {
				if (r_valid_remove_size > 0) {
					r_valid_remove_size--;
					pf.GetAllForests().remove(f);
				}
			} else if (f.GetRole() == RoleAssigner.test_k) {
				if (r_test_remove_size > 0) {
					r_test_remove_size--;
					pf.GetAllForests().remove(f);
				}
			}
		}
	}

	public static void TranslatePairEncodedSkeletonsAndTokens(TensorTools tensor_tool, SkeletonForestRecorder sfr)
			throws Exception {
		IDManager im = tensor_tool.im;
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
				Integer index = kind_index.get(r + "");
				if (index == null) {
					index = 0;
				} else {
					index++;
				}
				kind_index.put(r + "", index);
				String func_sig = "name:" + s + "#role:" + r + "#index:" + index;
				funcs.add(func_sig);
			}
		}

		{
			Gson gson = new Gson();
			String pi_str = gson.toJson(proj_info);
			File pi_file = new File(MetaOfApp.DataDirectory + "/All_project_example_info.json");
			FileWriter pi_fw = new FileWriter(pi_file.getAbsoluteFile(), false);
			pi_fw.write(pi_str);
			pi_fw.flush();
			pi_fw.close();
		}
		
		// meta tensor
		Map<String, ArrayList<String>> one_to_each_str = new TreeMap<String, ArrayList<String>>();
		Map<Integer, ArrayList<Integer>> one_to_each = new TreeMap<Integer, ArrayList<Integer>>();
//		Map<Integer, ArrayList<String>> one_to_each_tree_uid = new TreeMap<Integer, ArrayList<String>>();
//		Map<Integer, Integer> one_h_count = new TreeMap<Integer, Integer>();
//		Map<Integer, ArrayList<String>> one_h_tree_uid = new TreeMap<Integer, ArrayList<String>>();
//		Map<Integer, Integer> one_v_count = new TreeMap<Integer, Integer>();
//		Map<Integer, ArrayList<String>> one_v_tree_uid = new TreeMap<Integer, ArrayList<String>>();
		
		Map<String, ArrayList<String>> one_to_pe_str = new TreeMap<String, ArrayList<String>>();
		Map<Integer, ArrayList<Integer>> one_to_pe = new TreeMap<Integer, ArrayList<Integer>>();
		
		Map<String, ArrayList<String>> pe_to_each_str = new TreeMap<String, ArrayList<String>>();
		Map<Integer, ArrayList<Integer>> pe_to_each = new TreeMap<Integer, ArrayList<Integer>>();
//		Map<Integer, ArrayList<Integer>> pe_to_each_tree_uid = new TreeMap<Integer, ArrayList<Integer>>();
//		Map<Integer, Integer> pe_h_count = new TreeMap<Integer, Integer>();
//		Map<Integer, ArrayList<String>> pe_h_tree_uid = new TreeMap<Integer, ArrayList<String>>();
//		Map<Integer, Integer> pe_v_count = new TreeMap<Integer, Integer>();
//		Map<Integer, ArrayList<String>> pe_v_tree_uid = new TreeMap<Integer, ArrayList<String>>();
		
//		Map<Integer, Integer> e_h_count = new TreeMap<Integer, Integer>();
//		Map<Integer, ArrayList<String>> e_h_tree_uid = new TreeMap<Integer, ArrayList<String>>();
//		Map<Integer, Integer> e_v_count = new TreeMap<Integer, Integer>();
//		Map<Integer, ArrayList<String>> e_v_tree_uid = new TreeMap<Integer, ArrayList<String>>();
		
//		Map<Integer, ArrayList<Integer>> one_hv_num = new TreeMap<Integer, ArrayList<Integer>>();
//		Map<String, ArrayList<Integer>> one_str_hv_num = new TreeMap<String, ArrayList<Integer>>();
//		Map<Integer, ArrayList<Integer>> pe_hv_num = new TreeMap<Integer, ArrayList<Integer>>();
//		Map<String, ArrayList<Integer>> pe_str_hv_num = new TreeMap<String, ArrayList<Integer>>();
//		Map<Integer, ArrayList<Integer>> each_hv_num = new TreeMap<Integer, ArrayList<Integer>>();
//		Map<String, ArrayList<Integer>> each_str_hv_num = new TreeMap<String, ArrayList<Integer>>();
		
//		TreeMap<String, ArrayList<String>> atcs = sfr.GetAllTokenComposes();
//		TreeSet<String> pe_keys = new TreeSet<String>();
//		TreeSet<String> each_keys = new TreeSet<String>();
		
		IntsWrapper skt_batch_relative_part_max = new IntsWrapper();
		IntsWrapper skt_pe_batch_relative_part_max = new IntsWrapper();
		IntsWrapper skt_each_batch_relative_part_max = new IntsWrapper();
		
		for (ProjectForests pf : aps) {
			TensorForProject tfp = new TensorForProject("skt");
			TensorForProject tfp_one = new TensorForProject("skt_one");
			TensorForProject tfp_pe = new TensorForProject("skt_pe");
			TensorForProject tfp_e = new TensorForProject("skt_e");
//			TensorForProject tfp_nv = new TensorForProject("skt_nv");
			
			ArrayList<Tensor> skt_tensors = new ArrayList<Tensor>();
			ArrayList<Tensor> skt_one_tensors = new ArrayList<Tensor>();
			ArrayList<Tensor> skt_pe_tensors = new ArrayList<Tensor>();
			ArrayList<Tensor> skt_e_tensors = new ArrayList<Tensor>();
//			ArrayList<Tensor> skt_nv_tensors = new ArrayList<Tensor>();
			ArrayList<Forest> func_os = pf.GetAllForests();
			for (Forest f : func_os) {
				TensorInfo tinfo = new TensorInfo(f.GetFilePath(), f.GetSignature());
				StatementSkeletonTensor sst = new StatementSkeletonTensor(tinfo, f.GetSignature(), skt_batch_relative_part_max, skt_pe_batch_relative_part_max, skt_each_batch_relative_part_max);
				sst.SetRole(f.GetRole());
				ArrayList<Tree> trees = f.GetAllTrees();
				for (Tree tree : trees) {
					TreeFlatten tf = tree.GetTreeFlattenResult();

					ArrayList<String> info_str = new ArrayList<String>();
					ArrayList<Integer> info = new ArrayList<Integer>();
					ArrayList<Integer> kind = new ArrayList<Integer>();
					ArrayList<Integer> is_var = new ArrayList<Integer>();

					String o_str = tf.skt_one_struct.get(0);
					info_str.add(o_str);
					int skt_one_id = im.GetSkeletonID(o_str);
					info.add(skt_one_id);
					kind.add(0);
					is_var.add(-1);
					
					Assert.isTrue(YStringUtil.CountSubStringInString(o_str, "#h") == 0);
					int count = YStringUtil.CountSubStringInString(o_str, "#v");
					if (count != tf.skt_token.size()) {
						 tree.DebugPrintEachNode();
					}
					Assert.isTrue(count == tf.skt_token.size(), "count:" + count + "#tf.skt_token.size():" + tf.skt_token.size() + "#o_str:" + o_str + "#token list:" + PrintUtil.PrintListToString(tf.skt_token, "skt_tokens"));
					info_str.addAll(tf.skt_token);
					ArrayList<Integer> skt_token_ids = TranslateTokenToID(tf.skt_token, im, "GetSkeletonTypeContentID");
					info.addAll(skt_token_ids);
					kind.addAll(tf.skt_token_kind);
					is_var.addAll(tf.skt_token_is_var);
					
					ArrayList<Integer> skt_one_ids = TranslateTokenToID(tf.skt_one_struct, im, "GetSkeletonID");
					
					if (one_to_each_str.containsKey(tf.skt_one_struct.get(0))) {
						Assert.isTrue(PrintUtil.PrintListToString(tf.skt_one_e_struct, "").equals(PrintUtil.PrintListToString(one_to_each_str.get(tf.skt_one_struct.get(0)), "")));
					}
					one_to_each_str.put(tf.skt_one_struct.get(0), new ArrayList<String>(tf.skt_one_e_struct));
					ArrayList<Integer> skt_each_ids = TranslateTokenToID(tf.skt_one_e_struct, im, "GetEachSkeletonID");
					one_to_each.put(skt_one_ids.get(0), skt_each_ids);
					if (one_to_pe_str.containsKey(tf.skt_one_struct.get(0))) {
						Assert.isTrue(PrintUtil.PrintListToString(tf.skt_pe_struct, "").equals(PrintUtil.PrintListToString(one_to_pe_str.get(tf.skt_one_struct.get(0)), "")));
					}
					one_to_pe_str.put(tf.skt_one_struct.get(0), new ArrayList<String>(tf.skt_pe_struct));
					ArrayList<Integer> skt_pe_ids = TranslateTokenToID(tf.skt_pe_struct, im, "GetPESkeletonID");
					one_to_pe.put(skt_one_ids.get(0), skt_pe_ids);

					sst.StoreStatementSkeletonInfo(info_str, info, kind, is_var);
					sst.StoreStatementSkeletonBatchInfo(im.skeleton_hit_num, im.skt_token_hit_num, tf.skt_one_struct, skt_one_ids, tf.skt_token, skt_token_ids);
					sst.StoreStatementSkeletonPEBatchInfo(im.pe_skeleton_hit_num, im.skt_token_hit_num, tf.skt_pe_struct, skt_pe_ids, tf.skt_token, skt_token_ids);
					sst.StoreStatementSkeletonEachBatchInfo(im.each_skeleton_hit_num, im.skt_token_hit_num, tf.skt_one_e_struct, skt_each_ids, tf.skt_token, skt_token_ids);
					
//					one_to_each_tree_uid.put(skt_one_id, tf.skt_one_e_struct_tree_uid);
//					one_h_count.put(skt_one_id, tf.skt_one_struct_h_count.get(0));
//					one_h_tree_uid.put(skt_one_id, tf.skt_one_struct_h_tree_uid.get(0));
//					one_v_count.put(skt_one_id, tf.skt_one_struct_v_count.get(0));
//					one_v_tree_uid.put(skt_one_id, tf.skt_one_struct_v_tree_uid.get(0));
					
					int index = -1;
					for (String pe : tf.skt_pe_struct) {
						index++;
						if (!pe_to_each_str.containsKey(pe)) {
							pe_to_each_str.put(pe, tf.skt_pe_e_struct.get(index));
							pe_to_each.put(im.GetPESkeletonID(pe), TranslateTokenToID(tf.skt_pe_e_struct.get(index), im, "GetEachSkeletonID"));
						} else {
							Assert.isTrue(ListUtil.TwoListEachElementEqual(tf.skt_pe_e_struct.get(index), pe_to_each_str.get(pe)));
						}
					}
					
//					ArrayList<Integer> hv = new ArrayList<Integer>();
//					hv.add(0);
//					hv.add(count);
//					if (one_str_hv_num.containsKey(tf.skt_one_struct.get(0))) {
//						Assert.isTrue(PrintUtil.PrintListToString(one_str_hv_num.get(tf.skt_one_struct.get(0)), "").equals(PrintUtil.PrintListToString(hv, "")));
//					}
//					one_hv_num.put(im.GetSkeletonID(tf.skt_one_struct.get(0)), hv);
//					one_str_hv_num.put(tf.skt_one_struct.get(0), hv);
					
//					pe_keys.addAll(tf.skt_pe_struct);
//					each_keys.addAll(tf.skt_e_struct);
				}
				sst.HandleAllInfo();
				skt_tensors.add(sst);
				skt_one_tensors.add(sst.skt_batch_info);
				skt_pe_tensors.add(sst.skt_pe_batch_info);
				skt_e_tensors.add(sst.skt_each_batch_info);
			}
			tfp.AddTensors(skt_tensors);
			tfp_one.AddTensors(skt_one_tensors);
			tfp_pe.AddTensors(skt_pe_tensors);
			tfp_e.AddTensors(skt_e_tensors);
//			tfp_nv.AddTensors(skt_nv_tensors);
			tfp.SaveToFile(pf.GetProjectInfo());
			tfp_one.SaveToFile(pf.GetProjectInfo());
			tfp_pe.SaveToFile(pf.GetProjectInfo());
			tfp_e.SaveToFile(pf.GetProjectInfo());
//			tfp_nv.SaveToFile(pf.GetProjectInfo());
		}
		
//		for (String k : pe_keys) {
//			ArrayList<String> tcs = atcs.get(k);
//			if (tcs == null) {
//				tcs = new ArrayList<String>();
//				tcs.add(k);
//			}
//			pe_to_each_str.put(k, new ArrayList<String>(tcs));
//			pe_to_each.put(im.GetPESkeletonID(k), TranslateTokenToID(tcs, im, "GetEachSkeletonID"));
//		}
//		
//		for (String k : pe_keys) {
//			ArrayList<Integer> hv = new ArrayList<Integer>();
//			hv.add(YStringUtil.CountSubStringInString(k, "#h"));
//			hv.add(YStringUtil.CountSubStringInString(k, "#v"));
//			if (pe_str_hv_num.containsKey(k)) {
//				Assert.isTrue(PrintUtil.PrintListToString(pe_str_hv_num.get(k), "").equals(PrintUtil.PrintListToString(hv, "")));
//			}
//			pe_hv_num.put(im.GetPESkeletonID(k), hv);
//			pe_str_hv_num.put(k, hv);
//		}
//		
//		for (String k : each_keys) {
//			ArrayList<Integer> hv = new ArrayList<Integer>();
//			hv.add(YStringUtil.CountSubStringInString(k, "#h"));
//			hv.add(YStringUtil.CountSubStringInString(k, "#v"));
//			if (each_str_hv_num.containsKey(k)) {
//				Assert.isTrue(PrintUtil.PrintListToString(each_str_hv_num.get(k), "").equals(PrintUtil.PrintListToString(hv, "")));
//			}
//			each_hv_num.put(im.GetEachSkeletonID(k), hv);
//			each_str_hv_num.put(k, hv);
//		}
		
		/*
		 * store one_to_each;one_to_pe;pe_to_each
		 */
		FileUtil.WriteJson(one_to_each_str, MetaOfApp.DataDirectory + "/All_map_skt_one_to_each_str.json");
		FileUtil.WriteJson(one_to_pe_str, MetaOfApp.DataDirectory + "/All_map_skt_one_to_pe_str.json");
		FileUtil.WriteJson(pe_to_each_str, MetaOfApp.DataDirectory + "/All_map_skt_pe_to_each_str.json");
		
		ArrayList<ArrayList<Integer>> oe_container = IntegerMapUtil.MapToNestedList(one_to_each);
		FileUtil.WriteJson(oe_container, MetaOfApp.DataDirectory + "/All_skt_one_to_each.json");
		
		ArrayList<ArrayList<Integer>> ope_container = IntegerMapUtil.MapToNestedList(one_to_pe);
		FileUtil.WriteJson(ope_container, MetaOfApp.DataDirectory + "/All_skt_one_to_pe.json");
		
		ArrayList<ArrayList<Integer>> pee_container = IntegerMapUtil.MapToNestedList(pe_to_each);
		FileUtil.WriteJson(pee_container, MetaOfApp.DataDirectory + "/All_skt_pe_to_each.json");
		
		System.out.println("skt_relative_part_max:" + skt_batch_relative_part_max.it1 + "#token_relative_part_max:" + skt_batch_relative_part_max.it2);
		System.out.println("skt_pe_relative_part_max:" + skt_pe_batch_relative_part_max.it1 + "#token_pe_relative_part_max:" + skt_pe_batch_relative_part_max.it2);
		System.out.println("skt_each_relative_part_max:" + skt_each_batch_relative_part_max.it1 + "#token_each_relative_part_max:" + skt_pe_batch_relative_part_max.it2);
		
//		FileUtil.WriteJson(one_hv_num, MetaOfApp.DataDirectory + "/All_one_hv_num.json");
//		FileUtil.WriteJson(one_str_hv_num, MetaOfApp.DataDirectory + "/All_one_str_hv_num.json");
//		FileUtil.WriteJson(pe_hv_num, MetaOfApp.DataDirectory + "/All_pe_hv_num.json");
//		FileUtil.WriteJson(pe_str_hv_num, MetaOfApp.DataDirectory + "/All_pe_str_hv_num.json");
//		FileUtil.WriteJson(each_hv_num, MetaOfApp.DataDirectory + "/All_each_hv_num.json");
//		FileUtil.WriteJson(each_str_hv_num, MetaOfApp.DataDirectory + "/All_each_str_hv_num.json");
	}

	public static ArrayList<Integer> TranslateTokenToID(ArrayList<String> ss, IDManager im, String m_key)
			throws Exception {
		ArrayList<Integer> ll = new ArrayList<Integer>();

		Method method = IDManager.class.getMethod(m_key, String.class);
		for (String s : ss) {
			int r = (int) method.invoke(im, s);
			ll.add(r);
		}

		return ll;
	}

}
