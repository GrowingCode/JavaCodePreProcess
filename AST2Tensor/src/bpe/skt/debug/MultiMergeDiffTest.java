package bpe.skt.debug;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

import bpe.skt.SktPEGeneratorForProject;
import eclipse.project.AnalysisEnvironment;
import main.AppMainRoot;
import main.MetaOfApp;
import statis.trans.common.SkeletonForestRecorder;
import statis.trans.project.STProject;
import statistic.id.SktPEMergeRecorder;
import tree.Forest;
import tree.Tree;
import tree.TreeFlatten;
import util.PrintUtil;
import util.SystemUtil;

public class MultiMergeDiffTest {
	
	public static void Test(SktPEMergeRecorder sktpe_mr) throws Exception {
		File root_dir = new File(AppMainRoot.data);
		SkeletonForestRecorder stf_r = new SkeletonForestRecorder();
		{
			List<STProject> all_projs = AnalysisEnvironment.LoadAllProjects(root_dir);
			for (STProject proj : all_projs) {
				try {
					SystemUtil.Delay(1000);
					SktPEGeneratorForProject irgfop = new SktPEGeneratorForProject(proj.GetJavaProject(), null, null);
					
					// stf_r should be declared locally. 
					stf_r.EncounterNewProject(proj.GetInfo());
					irgfop.GenerateForOneProject(stf_r);
					stf_r.PreProcessAllForests();
//					id_tool.stf_r.FlattenAllOriginTrees();
					
					stf_r.ApplySktPEMerges(sktpe_mr.GetMerges(), MetaOfApp.MaximumNumberOfApplyingSkeletonMerge);
					stf_r.FlattenAllTrees();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			AnalysisEnvironment.DeleteAllProjects();
		}
		SkeletonForestRecorder stf_r_am = new SkeletonForestRecorder();
		{
			List<STProject> all_projs = AnalysisEnvironment.LoadAllProjects(root_dir);
			for (STProject proj : all_projs) {
				try {
					SystemUtil.Delay(1000);
					SktPEGeneratorForProject irgfop = new SktPEGeneratorForProject(proj.GetJavaProject(), null, null);
					
					// stf_r should be declared locally. 
					stf_r_am.EncounterNewProject(proj.GetInfo());
					irgfop.GenerateForOneProject(stf_r_am);
					stf_r_am.PreProcessAllForests();
//					id_tool.stf_r.FlattenAllOriginTrees();
					
					stf_r_am.ApplySktPEMerges(sktpe_mr.GetMerges(), MetaOfApp.MaximumNumberOfApplyingSkeletonMerge*2);
					stf_r_am.FlattenAllTrees();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			AnalysisEnvironment.DeleteAllProjects();
		}
		
		ArrayList<Forest> afs = stf_r.GetAllForests();
		ArrayList<Forest> afs2 = stf_r_am.GetAllForests();
		int a_len = afs.size();
		int a2_len = afs2.size();
		Assert.isTrue(a_len == a2_len);
		for (int i=0;i<a_len;i++) {
			Forest a = afs.get(i);
			Forest a2 = afs2.get(i);
			
			ArrayList<Tree> ats = a.GetAllTrees();
			ArrayList<Tree> a2ts = a2.GetAllTrees();
			int ats_len = ats.size();
			int a2ts_len = a2ts.size();
			
			Assert.isTrue(ats_len == a2ts_len);
			for (int j = 0;j < ats_len; j++) {
				Tree t = ats.get(j);
				Tree t2 = a2ts.get(j);
				
				TreeFlatten tf = t.GetTreeFlattenResult();
				TreeFlatten tf2 = t2.GetTreeFlattenResult();
				Assert.isTrue(tf.skt_one_e_struct.size() + tf.skt_token.size() == tf2.skt_one_e_struct.size() + tf2.skt_token.size(), "tf1:" + PrintUtil.PrintListToString(tf.skt_one_e_struct, "$$") + PrintUtil.PrintListToString(tf.skt_token, "$$") + "tf2:" + PrintUtil.PrintListToString(tf2.skt_one_e_struct, "$$") + PrintUtil.PrintListToString(tf2.skt_token, "$$"));
//				Assert.isTrue(PrintUtil.PrintListToString(tf.skt_one_e_struct, "==").equals(PrintUtil.PrintListToString(tf2.skt_one_e_struct, "==")));
				ArrayList<String> ll = new ArrayList<String>();
				for (ArrayList<String> pe_e : tf.skt_pe_e_struct) {
					ll.addAll(pe_e);
				}
				Assert.isTrue(PrintUtil.PrintListToString(tf.skt_one_e_struct, "==").equals(PrintUtil.PrintListToString(ll, "==")));
			}
		}
		System.out.println("Assert all passed!");
	}
	
	
	
}
