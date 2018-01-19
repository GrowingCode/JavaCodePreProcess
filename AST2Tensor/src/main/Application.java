package main;

import java.io.File;
import java.io.IOException;

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
import statistic.id.IDCounter;
import statistic.id.IDManager;
import statistic.id.serialize.SaveIDMapToFile;
import translation.RoleAssigner;
import translation.TensorGeneratorForProject;
import translation.tensor.TensorForProject;
import translation.tensor.serialize.SaveTensorToFile;
import util.FileUtil;
import util.SystemUtil;
import util.ZIPUtil;

public class Application implements IApplication {
	
	public final static int RefinePeriod = 10000000;
	public final static int MinSupport = 3;
	public final static int MaxCapacity = 10000000;
	
	public final static String TemporaryUnzipWorkingSpace = "temp_unzip";
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		// DebugLogger.Log("Start is invoked!");
		// SystemUtil.Delay(1000);
//		while (!PlatformUI.isWorkbenchRunning()) {
//			DebugLogger.Log("Waiting the creation of the workbench.");
//			SystemUtil.Delay(1000);
//		}
		{
			// Clear data.
			DebugLogger.Log("===== Data Directory:" + System.getProperty("user.home") + "/AST_Tensors" + "; created!!! =====");
			File dd = new File(Meta.DataDirectory);
			if (dd.exists()) {
				FileUtil.DeleteFile(dd);
			}
			dd.mkdir();
		}
		// load and execute the project.
		String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		if (args.length != 2) {
			System.err.println("Wrong: argument size should be 2 and must be the directory which contains the tested files with max number of should-handle projects!");
			return IApplication.EXIT_OK;
		}
		String all_proj_paths = args[0];
		File root_dir = new File(all_proj_paths);
		int max_handle_projs = Integer.parseInt(args[1]);
		IDCounter ic = new IDCounter();
		{
			CountOneProjectHandle handle = new CountOneProjectHandle();
			HandleEachProjectFramework(max_handle_projs, root_dir, handle, ic, MinSupport, MaxCapacity, null, null);
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
		IDManager im = new IDManager();
		{
			ic.FinalRefineAllStatistics(MinSupport, MaxCapacity);
			ic.FullFillIDManager(im);
			SaveIDMapToFile.SaveIDMaps(im, Meta.DataDirectory);
		}
		{
			RoleAssigner role_assigner = new RoleAssigner();
			if (root_dir.isDirectory()) {
				TranslateOneProjectHandle handle = new TranslateOneProjectHandle();
				HandleEachProjectFramework(max_handle_projs, root_dir, handle, null, -1, -1, im, role_assigner);
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
		SystemUtil.Flush();
		SystemUtil.Delay(1000);
		return IApplication.EXIT_OK;
	}
	
	private void HandleEachProjectFramework(int max_handle_projs, File root_dir, HandleOneProject run, IDCounter ic, int min_support, int max_capacity, IDManager im, RoleAssigner role_assigner) {
		File[] files = root_dir.listFiles();
		int count_projs = 0;
		int all_size = 0;
		for (File f : files) {
			if (f.isDirectory()) {
				count_projs++;
				all_size = run.Handle(f.getAbsolutePath(), ic, all_size, min_support, max_capacity, im, role_assigner);
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
					all_size = run.Handle(unzip_out_dir.getAbsolutePath(), ic, all_size, min_support, max_capacity, im, role_assigner);
				}
//				if (unzip_out_dir.exists()) {
//					FileUtil.DeleteFile(unzip_out_dir);
//				}
			}
			if (count_projs > max_handle_projs) {
				break;
			}
		}
	}
	
	static int CountOneProject(String proj_path, IDCounter im) {
		int project_size = 0;
		try {
			IJavaProject java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			SystemUtil.Delay(1000);
			IDGeneratorForProject irgfop = new IDGeneratorForProject(java_project, im);
			project_size = irgfop.GenerateForOneProject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				AnalysisEnvironment.DeleteAllAnalysisEnvironment();
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		}
		return project_size;
	}

	@Override
	public void stop() {
		// DebugLogger.Log("Force Stop is invoked!");
	}
	
	// , File dest, File debug_dest, File oracle_dest
	// int total_num_tensors, 
	static void TranslateOneProject(RoleAssigner role_assigner, String proj_path, IDManager im) {
		try {
			IJavaProject java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			SystemUtil.Delay(1000);
			TensorGeneratorForProject irgfop = new TensorGeneratorForProject(role_assigner, java_project, im);
			TensorForProject one_project_tensor = irgfop.GenerateForOneProject();
			one_project_tensor.GetNumOfTensors();
			SaveTensorToFile.SaveTensors(one_project_tensor);// , Best, debug_dest, oracle_dest
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				AnalysisEnvironment.DeleteAllAnalysisEnvironment();
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		}
	}

}

interface HandleOneProject {
	public int Handle(String proj_path, IDCounter ic, int all_size, int min_support, int max_capacity, IDManager im, RoleAssigner role_assigner);
}

class TranslateOneProjectHandle implements HandleOneProject {

	@Override
	public int Handle(String proj_path, IDCounter ic, int all_size, int min_support, int max_capacity, IDManager im, RoleAssigner role_assigner) {
		Application.TranslateOneProject(role_assigner, proj_path, im);
		return -1;
	}
	
}

class CountOneProjectHandle implements HandleOneProject {

	@Override
	public int  Handle(String proj_path, IDCounter ic, int all_size, int min_support, int max_capacity, IDManager im, RoleAssigner role_assigner) {
		all_size += Application.CountOneProject(proj_path, ic);
		if (all_size >= Application.RefinePeriod) {
			ic.TempRefineAllStatistics(min_support, max_capacity);
			all_size %= Application.RefinePeriod;
		}
		return all_size;
	}
	
}