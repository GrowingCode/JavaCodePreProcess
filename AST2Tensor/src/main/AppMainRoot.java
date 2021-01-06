package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bpe.BPEGeneratorForProject;
import bpe.skt.SktLogicUtil;
import bpe.skt.SktPEGeneratorForProject;
import bpe.skt.TreeNodeTwoMerge;
import eclipse.project.AnalysisEnvironment;
import logger.DebugLogger;
import main.util.AppRunUtil;
import main.util.HandleOneProject;
import statis.trans.common.SkeletonForestRecorder;
import statis.trans.project.STProject;
import statistic.IDGeneratorForProject;
import statistic.IDTools;
import statistic.ast.ChildrenNumCounter;
import statistic.id.APIRecorder;
import statistic.id.BPEMergeRecorder;
import statistic.id.GrammarRecorder;
import statistic.id.IDManager;
import statistic.id.SktPEMergeRecorder;
import statistic.id.TokenRecorder;
import translation.SktTensorTools;
import translation.TensorGeneratorForProject;
import translation.TensorTools;
import translation.tensor.StatementTensor;
import translation.tensor.TensorForProject;
import util.FileUtil;
import util.JsonPrintUtil;
import util.SystemUtil;

public class AppMainRoot implements IApplication {

	public static final String user_home = System.getProperty("user.home");
	
	public static final String bpe_merges_json = user_home + "/YYXWitnessBPEMerges.json";
//	public static final String bpe_token_times_json = user_home + "/YYXWitnessBPETokenTimes.json";

	public static final String sktpe_merges_json = user_home + "/YYXWitnessSktPEMerges.json";
//	public static final String sktpe_token_times_json = user_home + "/YYXWitnessSktPETokenTimes.json";
	
	public static final String witness = user_home + "/YYXWitness";
	public static final String data = user_home + "/YYXData";

//	public final static int RefinePeriod = 10000000;
//	public final static int MinSupport = 3;
//	public final static int MaxCapacity = 10000000;

	public final static String TemporaryUnzipWorkingSpace = "temp_unzip";

	@Override
	public Object start(IApplicationContext context) throws Exception {
//		DebugLogger.Log("Start is invoked!");
		SystemUtil.Delay(2000);
//		while (!PlatformUI.isWorkbenchRunning()) {
//			DebugLogger.Log("Waiting the creation of the workbench.");
//			SystemUtil.Delay(1000);
//		}
		{
			// clear data.
			DebugLogger.Log(
					"===== Data Directory:" + System.getProperty("user.home") + "/AST_Tensors" + "; created!!! =====");
			File dd = new File(MetaOfApp.DataDirectory);
			if (dd.exists()) {
				FileUtil.DeleteFile(dd);
			}
			Thread.sleep(5000);
			dd.mkdirs();
			Thread.sleep(5000);
			Assert.isTrue(dd.listFiles().length == 0);
			// clear projects. 
			AnalysisEnvironment.DeleteAllProjects();
		}
		// load and execute the project.
//		String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
//		if (args.length < 2) {
//			System.err.println("Wrong: argument size should be 2 and must be the directory which contains the tested files with BPE merge handling projects!");
//			return IApplication.EXIT_OK;
//		}
//		String all_proj_paths = args[0];
		File root_dir = new File(data);

//		String bpe_proj_paths = args[1];

//		int max_handle_projs = -1;
//		if (args.length >= 2) {
//			max_handle_projs = Integer.parseInt(args[1]);
//		}
		IDTools id_tool = null;
		{
			BPEMergeRecorder bpe_mr = null;
//			new BPEMergeRecorder();
			TokenRecorder tr = new TokenRecorder(MetaOfApp.MaximumTokenCapacity);
//			TokenRecorder sr = new TokenRecorder();
			TokenRecorder one_struct_r = new TokenRecorder(MetaOfApp.MaximumSkeletonTokenCapacity);
			TokenRecorder pe_struct_r = new TokenRecorder(MetaOfApp.MaximumSkeletonTokenCapacity);
			TokenRecorder e_struct_r = new TokenRecorder(MetaOfApp.MaximumSkeletonTokenCapacity);
			TokenRecorder s_t_r = new TokenRecorder(MetaOfApp.MaximumTokenCapacity);
			GrammarRecorder gr = new GrammarRecorder();
			APIRecorder ar = new APIRecorder();
			ChildrenNumCounter cnc = new ChildrenNumCounter();
			SktPEMergeRecorder sktpe_mr = new SktPEMergeRecorder();
//			stf_r, 
			id_tool = new IDTools(bpe_mr, tr, one_struct_r, pe_struct_r, e_struct_r, s_t_r, gr, ar, cnc, sktpe_mr);
		}
//		{
//			File bpe_mj = new File(bpe_merges_json);
////			File bpe_ttj = new File(bpe_token_times_json);
//			if (bpe_mj.exists()) {
////				Assert.isTrue(bpe_ttj.exists());
//				List<String> merges = new Gson().fromJson(FileUtil.ReadFromFile(bpe_mj), new TypeToken<List<String>>(){}.getType());
////				String bpe_mj_content = FileUtil.ReadFromFile(bpe_mj);
////				System.out.println("bpe_mj_content:" + bpe_mj_content);
////				Map<String, Integer> token_times = new Gson().fromJson(FileUtil.ReadFromFile(bpe_ttj), new TypeToken<Map<String, Integer>>(){}.getType());
////				, token_times
//				id_tool.bpe_mr.Initialize(merges);
//				System.out.println("==== BPECount Loaded ====");
//			} else {
//				System.out.println("==== BPECount Begin ====");
//				List<STProject> all_projs = AnalysisEnvironment.LoadAllProjects(bpe_dir);
//				BPEOneProjectHandle handle = new BPEOneProjectHandle();
//				HandleEachProjectFramework(all_projs, handle, id_tool, null);
//	//			System.out.println("==== BPEMerge Begin ====");
//				id_tool.bpe_mr.GenerateBPEMerges();// MetaOfApp.NumberOfMerges
//	//			System.out.println("==== BPEMerge End ====");
//				id_tool.bpe_mr.SaveTo(bpe_mj);// , bpe_ttj
//				AnalysisEnvironment.DeleteAllProjects();
//				RoleAssigner.GetInstance().ClearRoles();
//				System.out.println("==== BPECount End ====");
//			}
//		}
		if (MetaOfApp.GenerateSkeletonToken)
		{
			File sktpe_mj = new File(sktpe_merges_json);
//			File sktpe_ttj = new File(sktpe_token_times_json);
			if (sktpe_mj.exists()) {
//				Assert.isTrue(sktpe_ttj.exists());
				List<TreeNodeTwoMerge> merges = new Gson().fromJson(FileUtil.ReadFromFile(sktpe_mj), new TypeToken<ArrayList<TreeNodeTwoMerge>>(){}.getType());
//				Map<String, Integer> token_times = new Gson().fromJson(FileUtil.ReadFromFile(sktpe_ttj), new TypeToken<Map<String, Integer>>(){}.getType());
				id_tool.sktpe_mr.Initialize(merges);// , token_times
				System.out.println("==== SktPEMerge Loaded ====");
			} else {
				System.out.println("==== SktPEMerge Not Exist! Set Up and Exit Program! ====");
				AppSktMergeSetUp.SetUp();
				return IApplication.EXIT_OK;
			}
		}
		List<STProject> all_projs = AnalysisEnvironment.LoadAllProjects(root_dir);
		{
			System.out.println("==== IDCount Begin ====");
			/**
			 * handle pair encoded skeleton
			 */
			if (MetaOfApp.GenerateSkeletonToken) {
				// Handle SktPE logic
				SktPECountOneProjectHandle handle = new SktPECountOneProjectHandle();
				AppRunUtil.HandleEachProjectFramework(all_projs, handle, id_tool, null);
			}
			/**
			 * normal handle
			 */
			{
				CountOneProjectHandle handle = new CountOneProjectHandle();
				AppRunUtil.HandleEachProjectFramework(all_projs, handle, id_tool, null);
			}
			// max_handle_projs,
//			List<String> proj_paths = FileUtil.ReadLineFromFile(new File(all_proj_paths));
//			Iterator<String> pitr = proj_paths.iterator();
//			int all_size = 0;
//			while (pitr.hasNext()) {
//				String proj_path = pitr.next();
//				all_size += CountOneProject(proj_path, ic);
//				if (all_size >= RefinePeriod) {
//					ic.TempRefineAllStatistics(MinSupport, MaxCapacity);
//					all_size %= RefinePeriod;
//				}
//			}
			System.out.println("==== IDCount End ====");
		}
		IDManager im = new IDManager(id_tool);
		{
			MetaOfApp.SaveToDirectory();
			id_tool.cnc.SaveToDirectory(MetaOfApp.DataDirectory);
			im.SaveToDirectory(MetaOfApp.DataDirectory);
//			gr.SaveToDirectory(MetaOfApp.DataDirectory, im);
		}
//		{
//			tr.RefineAllStatistics(MinSupport, MaxCapacity);
//			tr.FullFillIDManager(im);
//		}
		{
			System.out.println("==== GenerateTensor Begin ====");
			
			Assert.isTrue((root_dir.isDirectory()), "The root path given in parameter should be a directory which contains zip files or with-project directories");
			/**
			 * store the map of skeleton one token to scatter tokens. 
			 */
			if (MetaOfApp.GenerateSkeletonToken) {
				SktTensorTools skt_tensor_tool = new SktTensorTools(im);
				SktPETranslateOneProjectHandle handle = new SktPETranslateOneProjectHandle();
				AppRunUtil.HandleEachProjectFramework(all_projs, handle, id_tool, skt_tensor_tool);
				skt_tensor_tool.SaveSkeletonComposeData();
			}
			/**
			 * normal handle
			 */
			TensorTools tensor_tool = new TensorTools(im);
			{
				TranslateOneProjectHandle handle = new TranslateOneProjectHandle();
				AppRunUtil.HandleEachProjectFramework(all_projs, handle, null, tensor_tool);
			}
				// max_handle_projs,
//				File[] files = root_dir.listFiles();
//				int count_projs = 0;
//				for (File f : files) {
//					if (f.isDirectory()) {
//						count_projs++;
//						TranslateOneProject(role_assigner, f.getAbsolutePath(), im);
//					} else {
//						File unzip_out_dir = new File(TemporaryUnzipWorkingSpace);
//						if (unzip_out_dir.exists()) {
//							FileUtil.DeleteFile(unzip_out_dir);
//						}
//						unzip_out_dir.mkdirs();
//						if (f.getName().endsWith(".zip")) {
//							ZIPUtil.Unzip(f, unzip_out_dir);
//							TranslateOneProject(role_assigner, unzip_out_dir.getAbsolutePath(), im);
//						}
////						if (unzip_out_dir.exists()) {
////							FileUtil.DeleteFile(unzip_out_dir);
////						}
//					}
//					if (count_projs > max_handle_projs) {
//						break;
//					}
//				}
//			} else {
//				DebugLogger.Error(
//						"The root path given in parameter should be a directory which contains zip files or with-project directories");
//			}
			System.out.println("==== GenerateTensor End ====");
		}
		System.out.println("==== TranslateProject Over ====");
		System.out.println(im.WordVocabularyInfo());
		System.out.println(StatementTensor.StatementSummaryInfo());
		AnalysisEnvironment.DeleteAllProjects();
		
		int proj_num = all_projs.size();
		TreeMap<String, Integer> meta_of_app_handle = new TreeMap<String, Integer>();
		meta_of_app_handle.put("ProjectSize", proj_num);
		JsonPrintUtil.PrintMapToJsonFile(meta_of_app_handle, MetaOfApp.MetaDirectory, "meta_of_app_handle.json");
		
		SystemUtil.Flush();
		SystemUtil.Delay(2500);
		
		return IApplication.EXIT_OK;
	}
	
	static int SktPECountOneProject(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
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
			stf_r.ApplySktPEMerges(id_tool.sktpe_mr.GetMerges());
			stf_r.FlattenAllTrees();
			SktLogicUtil.CountPairEncodedSkeletons(id_tool, stf_r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return project_size;
	}

	static int SktPETranslateOneProject(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
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
			stf_r.ApplySktPEMerges(id_tool.sktpe_mr.GetMerges());
			stf_r.FlattenAllTrees();
			SktLogicUtil.FilterPairEncodedSkeletonsAndTokens(tensor_tool, stf_r);
			SktLogicUtil.TranslatePairEncodedSkeletonsAndTokens((SktTensorTools) tensor_tool, stf_r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return project_size;
	}
	
	static int BPEOneProject(STProject proj, IDTools id_tool) {
		int project_size = 0;
		try {
			SystemUtil.Delay(1000);
			BPEGeneratorForProject irgfop = new BPEGeneratorForProject(proj.GetJavaProject(), id_tool);
			project_size = irgfop.GenerateForOneProject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return project_size;
	}

	static int CountOneProject(STProject proj, IDTools id_tool) {
		int project_size = 0;
		try {
			SystemUtil.Delay(1000);
			IDGeneratorForProject irgfop = new IDGeneratorForProject(proj.GetJavaProject(), id_tool);
			project_size = irgfop.GenerateForOneProject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return project_size;
	}

	static void TranslateOneProject(STProject proj, TensorTools tensor_tool) {
		try {
			SystemUtil.Delay(1000);
			TensorGeneratorForProject tgfp = new TensorGeneratorForProject(proj.GetJavaProject(), tensor_tool);
			List<TensorForProject> project_tensors = tgfp.GenerateForOneProject();
			for (TensorForProject one_project_tensor : project_tensors) {
				one_project_tensor.SaveToFile(proj.GetInfo());// , Best, debug_dest, oracle_dest
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		// DebugLogger.Log("Force Stop is invoked!");
	}

}

class TranslateOneProjectHandle implements HandleOneProject {

//	String proj_path, int all_size, 
	@Override
	public int Handle(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
//		IJavaProject java_project = null;
//		try {
//			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
//			TensorGeneratorForProject ttgfop = new TreeTensorGeneratorForProject(role_assigner, java_project, im);
			AppMainRoot.TranslateOneProject(proj, tensor_tool);// proj_path, , ttgfop
//			TensorGeneratorForProject stgfop = new SequenceTensorGeneratorForProject(role_assigner, java_project, im);
//			Application.TranslateOneProject(role_assigner, im, stgfop, "sequence");// proj_path, 
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				ProjectLoader.CloseAllProjects();
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		}
		return -1;
	}

}

class CountOneProjectHandle implements HandleOneProject {

//	String proj_path, int all_size
	@Override
	public int Handle(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
//		IJavaProject java_project = null;
//		try {
//			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			int all_size = AppMainRoot.CountOneProject(proj, id_tool);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				ProjectLoader.CloseAllProjects();
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		}
//		if (all_size >= Application.RefinePeriod) {
//			id_tool.ic.RefineAllStatistics(min_support, max_capacity);
//			all_size %= Application.RefinePeriod;
//		}
		return all_size;
	}

}

class BPEOneProjectHandle implements HandleOneProject {

//	String proj_path, int all_size
	public int Handle(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
//		IJavaProject java_project = null;
//		try {
//			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			int all_size = AppMainRoot.BPEOneProject(proj, id_tool);
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

}

class SktPECountOneProjectHandle implements HandleOneProject {

//	String proj_path, int all_size
	public int Handle(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
//		IJavaProject java_project = null;
//		try {
//			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			int all_size = AppMainRoot.SktPECountOneProject(proj, id_tool, tensor_tool);
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

}

class SktPETranslateOneProjectHandle implements HandleOneProject {

//	String proj_path, int all_size
	public int Handle(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
//		IJavaProject java_project = null;
//		try {
//			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			int all_size = AppMainRoot.SktPETranslateOneProject(proj, id_tool, tensor_tool);
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

}




