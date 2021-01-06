package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bpe.skt.SktPEGeneratorForProject;
import eclipse.project.AnalysisEnvironment;
import logger.DebugLogger;
import main.util.AppRunUtil;
import main.util.HandleOneProject;
import statis.trans.common.RoleAssigner;
import statis.trans.common.SkeletonForestRecorder;
import statis.trans.project.STProject;
import statistic.IDTools;
import statistic.id.SktPEMergeRecorder;
import translation.TensorTools;
import tree.Forest;
import tree.Tree;
import util.SystemUtil;

public class AppSktMergeSetUp {
	
	public static void main(String[] args) {
		IDTools id_tool = null;
		{
			SktPEMergeRecorder sktpe_mr = new SktPEMergeRecorder();
			id_tool = new IDTools(null, null, null, null, null, null, null, null, null, sktpe_mr);
		}
		
		System.out.println("==== SktPECount Begin ====");
		File bpe_dir = new File(AppMainRoot.witness);
		List<STProject> all_projs = null;
		try {
			all_projs = AnalysisEnvironment.LoadAllProjects(bpe_dir);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Wrong loading projects and the system will exit!");
			System.exit(1);
		}
		SktPEGenerateMergeForOneProjectHandle handle = new SktPEGenerateMergeForOneProjectHandle();
		AppRunUtil.HandleEachProjectFramework(all_projs, handle, id_tool, null);
//		ArrayList<Forest> fs = id_tool.stf_r.GetAllForests();
//		for (Forest f : fs) {
//			ArrayList<Tree> f_trees = f.GetAllTrees();
//			for (Tree t : f_trees) {
//				id_tool.sktpe_mr.EncounterSkeleton(t, 1);
//			}
//		}
//		id_tool.sktpe_mr.GenerateSktPEMerges(MetaOfApp.NumberOfSkeletonMerges);
//		id_tool.sktpe_mr.SaveTo(sktpe_mj);// , sktpe_ttj
//		id_tool.stf_r.Clear();
//		AnalysisEnvironment.DeleteAllProjects();
		RoleAssigner.GetInstance().ClearRoles();
		System.out.println("==== SktPECount End ====");
	}
	
}


class SktPEGenerateMergeForOneProjectHandle implements HandleOneProject {

//	String proj_path, int all_size
	public int Handle(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
//		IJavaProject java_project = null;
//		try {
//			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			int all_size = SktPEGenerateMergeForOneProject(proj, id_tool, tensor_tool);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				ProjectLoader.CloseAllProjects();
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		}
		return all_size;
	}
	

	public static int SktPEGenerateMergeForOneProject(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
		File sktpe_mj = new File(AppMainRoot.sktpe_merges_json);
		int project_size = 0;
		try {
			SystemUtil.Delay(1000);
			SktPEGeneratorForProject irgfop = new SktPEGeneratorForProject(proj.GetJavaProject(), id_tool, tensor_tool);
			SkeletonForestRecorder stf_r = new SkeletonForestRecorder();
			// stf_r should be declared locally. 
			stf_r.EncounterNewProject(proj.GetInfo());
			project_size = irgfop.GenerateForOneProject(stf_r);
			stf_r.PreProcessAllForests();
//			id_tool.stf_r.FlattenAllOriginTrees();
//			stf_r.ApplySktPEMerges(id_tool.sktpe_mr.GetMerges());
//			stf_r.FlattenAllTrees();
//			SktLogicUtil.CountPairEncodedSkeletons(id_tool, stf_r);
			DebugLogger.Log("Forest preprocessed!");
			
			ArrayList<Forest> fs = stf_r.GetAllForests();
			for (Forest f : fs) {
				ArrayList<Tree> f_trees = f.GetAllTrees();
				for (Tree t : f_trees) {
					id_tool.sktpe_mr.EncounterSkeleton(t, 1);
				}
			}
			id_tool.sktpe_mr.GenerateSktPEMerges();// MetaOfApp.NumberOfSkeletonMerges
			id_tool.sktpe_mr.SaveTo(sktpe_mj);// , sktpe_ttj
//			id_tool.stf_r.Clear();
//			AnalysisEnvironment.DeleteAllProjects();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return project_size;
	}


}


