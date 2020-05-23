package main;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bpe.BPEGeneratorForProject;
import bpe.skt.SktPEGeneratorForProject;
import bpe.skt.TreeNodeTwoMerge;
import eclipse.project.AnalysisEnvironment;
import logger.DebugLogger;
import statis.trans.common.RoleAssigner;
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
import translation.TensorGeneratorForProject;
import translation.TensorTools;
import translation.tensor.StatementTensor;
import translation.tensor.TensorForProject;
import util.FileUtil;
import util.SystemUtil;

public class Application implements IApplication {

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
//		SystemUtil.Delay(10000);
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
		File bpe_dir = new File(witness);

//		int max_handle_projs = -1;
//		if (args.length >= 2) {
//			max_handle_projs = Integer.parseInt(args[1]);
//		}
		BPEMergeRecorder bpe_mr = new BPEMergeRecorder();
		SktPEMergeRecorder sktpe_mr = new SktPEMergeRecorder();
//		RoleAssigner role_assigner = new RoleAssigner();
		TokenRecorder tr = new TokenRecorder();
		TokenRecorder sr = new TokenRecorder();
//		TokenRecorder str = new TokenRecorder();
		GrammarRecorder gr = new GrammarRecorder();
		APIRecorder ar = new APIRecorder();
		ChildrenNumCounter cnc = new ChildrenNumCounter();
//		str, 
		IDTools id_tool = new IDTools(bpe_mr, tr, sr, gr, ar, cnc);
		{
			File bpe_mj = new File(bpe_merges_json);
//			File bpe_ttj = new File(bpe_token_times_json);
			if (bpe_mj.exists()) {
//				Assert.isTrue(bpe_ttj.exists());
				List<String> merges = new Gson().fromJson(FileUtil.ReadFromFile(bpe_mj), new TypeToken<List<String>>(){}.getType());
//				String bpe_mj_content = FileUtil.ReadFromFile(bpe_mj);
//				System.out.println("bpe_mj_content:" + bpe_mj_content);
//				Map<String, Integer> token_times = new Gson().fromJson(FileUtil.ReadFromFile(bpe_ttj), new TypeToken<Map<String, Integer>>(){}.getType());
//				, token_times
				bpe_mr.Initialize(merges);
				System.out.println("==== BPECount Loaded ====");
			} else {
				System.out.println("==== BPECount Begin ====");
				List<STProject> all_projs = AnalysisEnvironment.LoadAllProjects(bpe_dir);
				BPEOneProjectHandle handle = new BPEOneProjectHandle();
				HandleEachProjectFramework(all_projs, handle, id_tool, null);
	//			System.out.println("==== BPEMerge Begin ====");
				bpe_mr.GenerateBPEMerges(MetaOfApp.NumberOfMerges);
	//			System.out.println("==== BPEMerge End ====");
				bpe_mr.SaveTo(bpe_mj);// , bpe_ttj
				AnalysisEnvironment.DeleteAllProjects();
				RoleAssigner.GetInstance().ClearRoles();
				System.out.println("==== BPECount End ====");
			}
		}
		{
			File sktpe_mj = new File(sktpe_merges_json);
//			File sktpe_ttj = new File(sktpe_token_times_json);
			if (sktpe_mj.exists()) {
//				Assert.isTrue(sktpe_ttj.exists());
				List<TreeNodeTwoMerge> merges = new Gson().fromJson(FileUtil.ReadFromFile(sktpe_mj), new TypeToken<List<LinkedList<String>>>(){}.getType());
//				Map<String, Integer> token_times = new Gson().fromJson(FileUtil.ReadFromFile(sktpe_ttj), new TypeToken<Map<String, Integer>>(){}.getType());
				sktpe_mr.Initialize(merges);// , token_times
				System.out.println("==== SktPECount Loaded ====");
			} else {
				System.out.println("==== SktPECount Begin ====");
				List<STProject> all_projs = AnalysisEnvironment.LoadAllProjects(bpe_dir);
				SktPEOneProjectHandle handle = new SktPEOneProjectHandle();
				HandleEachProjectFramework(all_projs, handle, id_tool, null);
				sktpe_mr.GenerateSktPEMerges(MetaOfApp.NumberOfSkeletonMerges);
				sktpe_mr.SaveTo(sktpe_mj);// , sktpe_ttj
				AnalysisEnvironment.DeleteAllProjects();
				RoleAssigner.GetInstance().ClearRoles();
				System.out.println("==== SktPECount End ====");
			}
		}
		List<STProject> all_projs = AnalysisEnvironment.LoadAllProjects(root_dir);
		{
			System.out.println("==== IDCount Begin ====");
			CountOneProjectHandle handle = new CountOneProjectHandle();
			HandleEachProjectFramework(all_projs, handle, id_tool, null);
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
			cnc.SaveToDirectory(MetaOfApp.DataDirectory);
			im.SaveToDirectory(MetaOfApp.DataDirectory);
//			gr.SaveToDirectory(MetaOfApp.DataDirectory, im);
		}
//		{
//			tr.RefineAllStatistics(MinSupport, MaxCapacity);
//			tr.FullFillIDManager(im);
//		}
		{
			System.out.println("==== GenerateTensor Begin ====");
			TensorTools tensor_tool = new TensorTools(im);
			Assert.isTrue((root_dir.isDirectory()), "The root path given in parameter should be a directory which contains zip files or with-project directories");
			TranslateOneProjectHandle handle = new TranslateOneProjectHandle();
			HandleEachProjectFramework(all_projs, handle, null, tensor_tool);
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
		SystemUtil.Flush();
		SystemUtil.Delay(2500);
		return IApplication.EXIT_OK;
	}

	// int max_handle_projs, RoleAssigner role_assigner
	// File root_dir
	private static void HandleEachProjectFramework(List<STProject> projs, HandleOneProject run, IDTools id_tool,
			TensorTools tensor_tool) {
//		System.err.println(root_dir.getAbsolutePath());
//		File[] files = root_dir.listFiles();
//		int count_projs = 0;
//		int all_size = 0;
//		for (File f : files) {
		for (STProject proj : projs) {
//			if (max_handle_projs >= 0 && count_projs >= max_handle_projs) {
//				break;
//			}
//			all_size += 
			run.Handle(proj, id_tool, tensor_tool);
//			if (f.isDirectory()) {
////				count_projs++;
////				f.getAbsolutePath(), all_size
//				all_size += run.Handle(proj, id_tool, tensor_tool);
//			} else {
//				Assert.isTrue(false);
//				File unzip_out_dir = new File(TemporaryUnzipWorkingSpace);
//				if (unzip_out_dir.exists()) {
//					FileUtil.DeleteFile(unzip_out_dir);
//				}
//				unzip_out_dir.mkdirs();
//				if (f.getName().endsWith(".zip")) {
//					try {
//						ZIPUtil.Unzip(f, unzip_out_dir);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
////					unzip_out_dir.getAbsolutePath(), all_size
//					all_size += run.Handle(proj, id_tool, tensor_tool);
//				}
////				if (unzip_out_dir.exists()) {
////					FileUtil.DeleteFile(unzip_out_dir);
////				}
//			}
		}
	}
	
	static int SktPEOneProject(STProject proj, IDTools id_tool) {
		int project_size = 0;
		try {
			SystemUtil.Delay(1000);
			SktPEGeneratorForProject irgfop = new SktPEGeneratorForProject(proj.GetJavaProject(), id_tool);
			project_size = irgfop.GenerateForOneProject();
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

interface HandleOneProject {
//	String proj_path, int all_size
	public int Handle(STProject proj, IDTools id_tool, TensorTools tensor_tool);
}

class TranslateOneProjectHandle implements HandleOneProject {

//	String proj_path, int all_size, 
	@Override
	public int Handle(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
//		IJavaProject java_project = null;
//		try {
//			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
//			TensorGeneratorForProject ttgfop = new TreeTensorGeneratorForProject(role_assigner, java_project, im);
			Application.TranslateOneProject(proj, tensor_tool);// proj_path, , ttgfop
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
			int all_size = Application.CountOneProject(proj, id_tool);
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
			int all_size = Application.BPEOneProject(proj, id_tool);
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

class SktPEOneProjectHandle implements HandleOneProject {

//	String proj_path, int all_size
	public int Handle(STProject proj, IDTools id_tool, TensorTools tensor_tool) {
//		IJavaProject java_project = null;
//		try {
//			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			int all_size = Application.SktPEOneProject(proj, id_tool);
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
