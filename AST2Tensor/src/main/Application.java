package main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
// import org.eclipse.jdt.core.IJavaProject;
// import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IJavaProject;

import eclipse.project.AnalysisEnvironment;
import eclipse.project.ProjectLoader;
import logger.DebugLogger;
import statistic.IDGeneratorForProject;
import statistic.IDTools;
import statistic.ast.ChildrenNumCounter;
import statistic.id.TokenRecorder;
import statistic.id.APIRecorder;
import statistic.id.BPEMergeRecorder;
import statistic.id.GrammarRecorder;
import statistic.id.IDManager;
import translation.TensorGeneratorForProject;
import translation.TensorTools;
import translation.roles.RoleAssigner;
import translation.tensor.TensorForProject;
import translation.tensor.serialize.SaveTensorToFile;
import util.FileUtil;
import util.SystemUtil;
import util.ZIPUtil;

public class Application implements IApplication {

	public static final String user_home = System.getProperty("user.home");
	public static final String witness = user_home + "/YYXWitness";
	public static final String data = user_home + "/YYXData";
	
	public final static int RefinePeriod = 10000000;
	public final static int MinSupport = 3;
	public final static int MaxCapacity = 10000000;
	
	public final static String TemporaryUnzipWorkingSpace = "temp_unzip";
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
//		DebugLogger.Log("Start is invoked!");
//		SystemUtil.Delay(5000);
//		while (!PlatformUI.isWorkbenchRunning()) {
//			DebugLogger.Log("Waiting the creation of the workbench.");
//			SystemUtil.Delay(1000);
//		}
		{
			// Clear data.
			DebugLogger.Log("===== Data Directory:" + System.getProperty("user.home") + "/AST_Tensors" + "; created!!! =====");
			File dd = new File(MetaOfApp.DataDirectory);
			if (dd.exists()) {
				FileUtil.DeleteFile(dd);
			}
			dd.mkdir();
			Assert.isTrue(dd.listFiles().length == 0);
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
		RoleAssigner role_assigner = new RoleAssigner();
		TokenRecorder tr = new TokenRecorder();
		GrammarRecorder gr = new GrammarRecorder();
		APIRecorder ar = new APIRecorder();
		ChildrenNumCounter cnc = new ChildrenNumCounter();
		IDTools id_tool = new IDTools(bpe_mr, role_assigner, tr, gr, ar, cnc);
		{
			BPEOneProjectHandle handle = new BPEOneProjectHandle();
			HandleEachProjectFramework(bpe_dir, handle, id_tool, null);
		}
		bpe_mr.GenerateBPEMerges();
		{
			CountOneProjectHandle handle = new CountOneProjectHandle();
			HandleEachProjectFramework(root_dir, handle, id_tool, null);
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
		}
		IDManager im = new IDManager(id_tool);
//		{
//			tr.RefineAllStatistics(MinSupport, MaxCapacity);
//			tr.FullFillIDManager(im);
//		}
		{
			TensorTools tensor_tool = new TensorTools(role_assigner, im);
			if (root_dir.isDirectory()) {
				TranslateOneProjectHandle handle = new TranslateOneProjectHandle();
				HandleEachProjectFramework(root_dir, handle, null, tensor_tool);
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
			} else {
				DebugLogger.Error("The root path given in parameter should be a directory which contains zip files or with-project directories");
			}
		}
		{
			MetaOfApp.SaveToDirectory(MetaOfApp.DataDirectory);
			cnc.SaveToDirectory(MetaOfApp.DataDirectory);
			im.SaveToDirectory(MetaOfApp.DataDirectory);
			gr.SaveToDirectory(MetaOfApp.DataDirectory, im);
		}
		System.out.println(im.WordVocabularyInfo());
		SystemUtil.Flush();
		SystemUtil.Delay(1000);
		return IApplication.EXIT_OK;
	}
	
	// int max_handle_projs, RoleAssigner role_assigner
	private static void HandleEachProjectFramework(File root_dir, HandleOneProject run, IDTools id_tool, TensorTools tensor_tool) {
		System.err.println(root_dir.getAbsolutePath());
		File[] files = root_dir.listFiles();
//		int count_projs = 0;
		int all_size = 0;
		for (File f : files) {
//			if (max_handle_projs >= 0 && count_projs >= max_handle_projs) {
//				break;
//			}
			if (f.isDirectory()) {
//				count_projs++;
				all_size = run.Handle(f.getAbsolutePath(), all_size, id_tool, tensor_tool);
			} else {
				File unzip_out_dir = new File(TemporaryUnzipWorkingSpace);
				if (unzip_out_dir.exists()) {
					FileUtil.DeleteFile(unzip_out_dir);
				}
				unzip_out_dir.mkdirs();
				if (f.getName().endsWith(".zip")) {
					try {
						ZIPUtil.Unzip(f, unzip_out_dir);
					} catch (IOException e) {
						e.printStackTrace();
					}
					all_size = run.Handle(unzip_out_dir.getAbsolutePath(), all_size, id_tool, tensor_tool);
				}
//				if (unzip_out_dir.exists()) {
//					FileUtil.DeleteFile(unzip_out_dir);
//				}
			}
		}
	}
	
	static int BPEOneProject(IJavaProject java_project, IDTools id_tool) {
		int project_size = 0;
		try {
			SystemUtil.Delay(1000);
			IDGeneratorForProject irgfop = new IDGeneratorForProject(java_project, id_tool);
			project_size = irgfop.GenerateForOneProject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return project_size;
	}
	
	static int CountOneProject(IJavaProject java_project, IDTools id_tool) {
		int project_size = 0;
		try {
			SystemUtil.Delay(1000);
			IDGeneratorForProject irgfop = new IDGeneratorForProject(java_project, id_tool);
			project_size = irgfop.GenerateForOneProject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return project_size;
	}
	
	// , File dest, File debug_dest, File oracle_dest
	// int total_num_tensors, 
	// String proj_path, 
	// , TensorGeneratorForProject irgfop, String kind
	static void TranslateOneProject(IJavaProject java_project, TensorTools tensor_tool) {
		try {
			SystemUtil.Delay(1000);
			TensorGeneratorForProject tgfp = new TensorGeneratorForProject(java_project, tensor_tool);
			List<TensorForProject> project_tensors = tgfp.GenerateForOneProject();
			// one_project_tensor.GetNumOfTensors();
			for (TensorForProject one_project_tensor : project_tensors) {
				SaveTensorToFile.SaveTensors(one_project_tensor);// , Best, debug_dest, oracle_dest
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
//		finally {
//			try {
//				AnalysisEnvironment.DeleteAllAnalysisEnvironment();
//			} catch (CoreException e1) {
//				e1.printStackTrace();
//			}
//		}
	}
	
	@Override
	public void stop() {
		// DebugLogger.Log("Force Stop is invoked!");
	}
	
}

interface HandleOneProject {
	public int Handle(String proj_path, int all_size, IDTools id_tool, TensorTools tensor_tool);
}

class TranslateOneProjectHandle implements HandleOneProject {

	@Override
	public int Handle(String proj_path, int all_size, IDTools id_tool, TensorTools tensor_tool) {
		IJavaProject java_project = null;
		try {
			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
//			TensorGeneratorForProject ttgfop = new TreeTensorGeneratorForProject(role_assigner, java_project, im);
			Application.TranslateOneProject(java_project, tensor_tool);// proj_path, , ttgfop
//			TensorGeneratorForProject stgfop = new SequenceTensorGeneratorForProject(role_assigner, java_project, im);
//			Application.TranslateOneProject(role_assigner, im, stgfop, "sequence");// proj_path, 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ProjectLoader.CloseAllProjects();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return -1;
	}
	
}

class CountOneProjectHandle implements HandleOneProject {

	@Override
	public int Handle(String proj_path, int all_size, IDTools id_tool, TensorTools tensor_tool) {
		IJavaProject java_project = null;
		try {
			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			all_size += Application.CountOneProject(java_project, id_tool);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				AnalysisEnvironment.DeleteAllAnalysisEnvironment();
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		}
//		if (all_size >= Application.RefinePeriod) {
//			id_tool.ic.RefineAllStatistics(min_support, max_capacity);
//			all_size %= Application.RefinePeriod;
//		}
		return all_size;
	}
	
}

class BPEOneProjectHandle implements HandleOneProject {
	
	public int Handle(String proj_path, int all_size, IDTools id_tool, TensorTools tensor_tool) {
		IJavaProject java_project = null;
		try {
			java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			all_size += Application.BPEOneProject(java_project, id_tool);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				AnalysisEnvironment.DeleteAllAnalysisEnvironment();
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		}
		return all_size;
	}
	
}
