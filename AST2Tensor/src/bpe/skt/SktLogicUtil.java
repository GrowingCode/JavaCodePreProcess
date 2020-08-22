package bpe.skt;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;

import com.google.gson.Gson;

import eclipse.project.ProjectInfo;
import main.MetaOfApp;
import statis.trans.common.RoleAssigner;
import statis.trans.common.SkeletonForestRecorder;
import statistic.IDTools;
import statistic.id.IDManager;
import translation.TensorTools;
import translation.tensor.StatementSkeletonTensor;
import translation.tensor.Tensor;
import translation.tensor.TensorForProject;
import translation.tensor.TensorInfo;
import tree.Forest;
import tree.ProjectForests;
import tree.Tree;
import tree.TreeFlatten;
import util.FileUtil;
import util.PrintUtil;
import util.YStringUtil;

public class SktLogicUtil {

	public static void CountPairEncodedSkeletons(IDTools id_tool, SkeletonForestRecorder sfr) {
		ArrayList<Forest> fs = sfr.GetAllForests();
		for (Forest f : fs) {
			int role = f.GetRole();
			ArrayList<Tree> ts = f.GetAllTrees();
			for (Tree t : ts) {
				TreeFlatten tf = t.FlattenTree(sfr.GetAllTokenComposes());
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
		
		Map<String, ArrayList<String>> one_to_each_str = new TreeMap<String, ArrayList<String>>();
		Map<Integer, ArrayList<Integer>> one_to_each = new TreeMap<Integer, ArrayList<Integer>>();
		Map<String, ArrayList<String>> one_to_pe_str = new TreeMap<String, ArrayList<String>>();
		Map<Integer, ArrayList<Integer>> one_to_pe = new TreeMap<Integer, ArrayList<Integer>>();
		Map<String, ArrayList<String>> pe_to_each_str = new TreeMap<String, ArrayList<String>>();
		Map<Integer, ArrayList<Integer>> pe_to_each = new TreeMap<Integer, ArrayList<Integer>>();
		
		Map<Integer, ArrayList<Integer>> one_hv_num = new TreeMap<Integer, ArrayList<Integer>>();
		Map<String, ArrayList<Integer>> one_str_hv_num = new TreeMap<String, ArrayList<Integer>>();
		Map<Integer, ArrayList<Integer>> pe_hv_num = new TreeMap<Integer, ArrayList<Integer>>();
		Map<String, ArrayList<Integer>> pe_str_hv_num = new TreeMap<String, ArrayList<Integer>>();
		Map<Integer, ArrayList<Integer>> each_hv_num = new TreeMap<Integer, ArrayList<Integer>>();
		Map<String, ArrayList<Integer>> each_str_hv_num = new TreeMap<String, ArrayList<Integer>>();
		
		TreeMap<String, ArrayList<String>> atcs = sfr.GetAllTokenComposes();
		TreeSet<String> pe_keys = new TreeSet<String>();
		TreeSet<String> each_keys = new TreeSet<String>();

		for (ProjectForests pf : aps) {
			TensorForProject tfp = new TensorForProject("skt");

			ArrayList<Tensor> e = new ArrayList<Tensor>();
			ArrayList<Forest> func_os = pf.GetAllForests();
			for (Forest f : func_os) {
				TensorInfo tinfo = new TensorInfo(f.GetFilePath(), f.GetSignature());
				StatementSkeletonTensor sst = new StatementSkeletonTensor(tinfo, f.GetSignature());
				sst.SetRole(f.GetRole());
				ArrayList<Tree> trees = f.GetAllTrees();
				for (Tree tree : trees) {
					TreeFlatten tf = tree.FlattenTree(atcs);

					ArrayList<String> info_str = new ArrayList<String>();
					ArrayList<Integer> info = new ArrayList<Integer>();
					ArrayList<Integer> kind = new ArrayList<Integer>();
					ArrayList<Integer> is_var = new ArrayList<Integer>();

					String o_str = tf.skt_one_struct.get(0);
					info_str.add(o_str);
					info.add(im.GetSkeletonID(o_str));
					kind.add(0);
					is_var.add(-1);
					
					Assert.isTrue(YStringUtil.CountSubStringInString(o_str, "#h") == 0);
					int count = YStringUtil.CountSubStringInString(o_str, "#v");
					if (count != tf.skt_token.size()) {
						 tree.DebugPrintEachNode();
					}
					Assert.isTrue(count == tf.skt_token.size(), "count:" + count + "#tf.skt_token.size():" + tf.skt_token.size() + "#o_str:" + o_str + "#token list:" + PrintUtil.PrintListToString(tf.skt_token, "skt_tokens"));
					info_str.addAll(tf.skt_token);
					info.addAll(TranslateTokenToID(tf.skt_token, im, "GetSkeletonTypeContentID"));
					kind.addAll(tf.skt_token_kind);
					is_var.addAll(tf.skt_token_is_var);

					sst.StoreStatementSkeletonInfo(info_str, info, kind, is_var);
					if (one_to_each_str.containsKey(tf.skt_one_struct.get(0))) {
						Assert.isTrue(PrintUtil.PrintListToString(tf.skt_e_struct, "").equals(PrintUtil.PrintListToString(one_to_each_str.get(tf.skt_one_struct.get(0)), "")));
					}
					one_to_each_str.put(tf.skt_one_struct.get(0), new ArrayList<String>(tf.skt_e_struct));
					one_to_each.put(im.GetSkeletonID(tf.skt_one_struct.get(0)),
							TranslateTokenToID(tf.skt_e_struct, im, "GetEachSkeletonID"));
					if (one_to_pe_str.containsKey(tf.skt_one_struct.get(0))) {
						Assert.isTrue(PrintUtil.PrintListToString(tf.skt_pe_struct, "").equals(PrintUtil.PrintListToString(one_to_pe_str.get(tf.skt_one_struct.get(0)), "")));
					}
					one_to_pe_str.put(tf.skt_one_struct.get(0), new ArrayList<String>(tf.skt_pe_struct));
					one_to_pe.put(im.GetSkeletonID(tf.skt_one_struct.get(0)),
							TranslateTokenToID(tf.skt_pe_struct, im, "GetPESkeletonID"));
					
					ArrayList<Integer> hv = new ArrayList<Integer>();
					hv.add(0);
					hv.add(count);
					if (one_str_hv_num.containsKey(tf.skt_one_struct.get(0))) {
						Assert.isTrue(PrintUtil.PrintListToString(one_str_hv_num.get(tf.skt_one_struct.get(0)), "").equals(PrintUtil.PrintListToString(hv, "")));
					}
					one_hv_num.put(im.GetSkeletonID(tf.skt_one_struct.get(0)), hv);
					one_str_hv_num.put(tf.skt_one_struct.get(0), hv);
					
					pe_keys.addAll(tf.skt_pe_struct);
					each_keys.addAll(tf.skt_e_struct);
				}
				sst.HandleAllInfo();
				e.add(sst);
			}
			tfp.AddTensors(e);
			tfp.SaveToFile(pf.GetProjectInfo());
		}
		
		for (String k : pe_keys) {
			ArrayList<String> tcs = atcs.get(k);
			if (tcs == null) {
				tcs = new ArrayList<String>();
				tcs.add(k);
			}
			pe_to_each_str.put(k, new ArrayList<String>(tcs));
			pe_to_each.put(im.GetPESkeletonID(k), TranslateTokenToID(tcs, im, "GetEachSkeletonID"));
		}
		
		for (String k : pe_keys) {
			ArrayList<Integer> hv = new ArrayList<Integer>();
			hv.add(YStringUtil.CountSubStringInString(k, "#h"));
			hv.add(YStringUtil.CountSubStringInString(k, "#v"));
			if (pe_str_hv_num.containsKey(k)) {
				Assert.isTrue(PrintUtil.PrintListToString(pe_str_hv_num.get(k), "").equals(PrintUtil.PrintListToString(hv, "")));
			}
			pe_hv_num.put(im.GetPESkeletonID(k), hv);
			pe_str_hv_num.put(k, hv);
		}
		
		for (String k : each_keys) {
			ArrayList<Integer> hv = new ArrayList<Integer>();
			hv.add(YStringUtil.CountSubStringInString(k, "#h"));
			hv.add(YStringUtil.CountSubStringInString(k, "#v"));
			if (each_str_hv_num.containsKey(k)) {
				Assert.isTrue(PrintUtil.PrintListToString(each_str_hv_num.get(k), "").equals(PrintUtil.PrintListToString(hv, "")));
			}
			each_hv_num.put(im.GetEachSkeletonID(k), hv);
			each_str_hv_num.put(k, hv);
		}
		
		/*
		 * store one_to_each;one_to_pe;pe_to_each
		 */
		FileUtil.WriteJson(one_to_each_str, MetaOfApp.DataDirectory + "/All_map_skt_one_to_each_str.json");
		FileUtil.WriteJson(one_to_each, MetaOfApp.DataDirectory + "/All_map_skt_one_to_each.json");
		FileUtil.WriteJson(one_to_pe_str, MetaOfApp.DataDirectory + "/All_map_skt_one_to_pe_str.json");
		FileUtil.WriteJson(one_to_pe, MetaOfApp.DataDirectory + "/All_map_skt_one_to_pe.json");
		FileUtil.WriteJson(pe_to_each_str, MetaOfApp.DataDirectory + "/All_map_skt_pe_to_each_str.json");
		FileUtil.WriteJson(pe_to_each, MetaOfApp.DataDirectory + "/All_map_skt_pe_to_each.json");
		
		FileUtil.WriteJson(one_hv_num, MetaOfApp.DataDirectory + "/All_one_hv_num.json");
		FileUtil.WriteJson(one_str_hv_num, MetaOfApp.DataDirectory + "/All_one_str_hv_num.json");
		FileUtil.WriteJson(pe_hv_num, MetaOfApp.DataDirectory + "/All_pe_hv_num.json");
		FileUtil.WriteJson(pe_str_hv_num, MetaOfApp.DataDirectory + "/All_pe_str_hv_num.json");
		FileUtil.WriteJson(each_hv_num, MetaOfApp.DataDirectory + "/All_each_hv_num.json");
		FileUtil.WriteJson(each_str_hv_num, MetaOfApp.DataDirectory + "/All_each_str_hv_num.json");
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
