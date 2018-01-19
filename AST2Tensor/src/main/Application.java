package main;

import java.io.File;
import java.util.Iterator;
import java.util.List;

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
		int max_handle_projs = Integer.parseInt(args[1]);
		IDCounter ic = new IDCounter();
		{
			List<String> proj_paths = FileUtil.ReadLineFromFile(new File(all_proj_paths));
			Iterator<String> pitr = proj_paths.iterator();
			int all_size = 0;
			while (pitr.hasNext()) {
				String proj_path = pitr.next();
				all_size += CountOneProject(proj_path, ic);
				if (all_size >= RefinePeriod) {
					ic.TempRefineAllStatistics(MinSupport, MaxCapacity);
					all_size %= RefinePeriod;
				}
			}
		}
		IDManager im = new IDManager();
		{
			ic.FinalRefineAllStatistics(MinSupport, MaxCapacity);
			ic.FullFillIDManager(im);
			SaveIDMapToFile.SaveIDMaps(im, Meta.DataDirectory);
		}
		{
			RoleAssigner role_assigner = new RoleAssigner();
			File root_dir = new File(all_proj_paths);
			if (root_dir.isDirectory()) {
				File[] files = root_dir.listFiles();
				int count_projs = 0;
				for (File f : files) {
					if (f.isDirectory()) {
						count_projs++;
						TranslateOneProject(role_assigner, f.getAbsolutePath(), im);
					} else {
						File unzip_out_dir = new File(TemporaryUnzipWorkingSpace);
						if (unzip_out_dir.exists()) {
							FileUtil.DeleteFile(unzip_out_dir);
						}
						unzip_out_dir.mkdirs();
						if (f.getName().endsWith(".zip")) {
							ZIPUtil.Unzip(f, unzip_out_dir);
							TranslateOneProject(role_assigner, unzip_out_dir.getAbsolutePath(), im);
						}
//						if (unzip_out_dir.exists()) {
//							FileUtil.DeleteFile(unzip_out_dir);
//						}
					}
					if (count_projs > max_handle_projs) {
						break;
					}
				}
			} else {
				DebugLogger.Error("The root path given in parameter should be a directory which contains zip files or with-project directories");
			}
//			List<String> proj_paths = FileUtil.ReadLineFromFile();
//			Iterator<String> pitr = proj_paths.iterator();
//			while (pitr.hasNext()) {
//				String proj_path = pitr.next();
//				TranslateOneProject(role_assigner, proj_path, im);// , f, debug_f, oracle_f
//			}
		}
		SystemUtil.Flush();
		SystemUtil.Delay(1000);
		return IApplication.EXIT_OK;
	}
	
	private int CountOneProject(String proj_path, IDCounter im) {
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
	private void TranslateOneProject(RoleAssigner role_assigner, String proj_path, IDManager im) {
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
