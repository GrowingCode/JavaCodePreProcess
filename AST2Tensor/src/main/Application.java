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
import statistic.IDGeneratorForProject;
import statistic.id.IDCounter;
import statistic.id.IDManager;
import statistic.id.serialize.SaveIDMapToFile;
import translation.TensorGeneratorForProject;
import translation.tensor.TensorForProject;
import translation.tensor.serialize.SaveTensorToFile;
import util.FileUtil;
import util.SystemUtil;

public class Application implements IApplication {
	
	public final static int RefinePeriod = 10000000;
	public final static int MinSupport = 3;
	public final static int MaxCapacity = 10000000;
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		// DebugLogger.Log("Start is invoked!");
		// SystemUtil.Delay(1000);
//		while (!PlatformUI.isWorkbenchRunning()) {
//			DebugLogger.Log("Waiting the creation of the workbench.");
//			SystemUtil.Delay(1000);
//		}
		// load and execute the project.
		String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		if (args.length != 1) {
			System.err.println("Wrong: argument size should be 1 and must be the directory which contains the tested files!");
			return IApplication.EXIT_OK;
		}
		String all_proj_paths = args[0];
		IDCounter ic = new IDCounter();
		{
			List<String> proj_paths = FileUtil.ReadLineFromFile(new File(all_proj_paths));
			Iterator<String> pitr = proj_paths.iterator();
			int all_size = 0;
			while (pitr.hasNext()) {
				String proj_path = pitr.next();
				all_size += CountOneProject(proj_path, ic);
				if (all_size >= RefinePeriod) {
					RefineAllStatistics(ic);
					all_size %= RefinePeriod;
				}
			}
		}
		IDManager im = new IDManager();
		{
			RefineAllStatistics(ic);
			ic.FullFillIDManager(im);
			SaveIDMapToFile.SaveIDMaps(im, Meta.DataDirectory);
		}
		{
			File f = new File(Meta.DataDirectory + "/all_data.txt");
			if (f.exists()) {
				f.delete();
			}
			f.createNewFile();
			
			List<String> proj_paths = FileUtil.ReadLineFromFile(new File(all_proj_paths));
			Iterator<String> pitr = proj_paths.iterator();
			while (pitr.hasNext()) {
				String proj_path = pitr.next();
				TranslateOneProject(proj_path, im, f);
			}
		}
		SystemUtil.Flush();
		SystemUtil.Delay(1000);
		return IApplication.EXIT_OK;
	}

	private void RefineAllStatistics(IDCounter im) {
		im.RefineAllStatistics(MinSupport, MaxCapacity);
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

	private void TranslateOneProject(String proj_path, IDManager im, File dest) {
		try {
			IJavaProject java_project = ProjectLoader.LoadProjectAccordingToArgs(proj_path);
			SystemUtil.Delay(1000);
			TensorGeneratorForProject irgfop = new TensorGeneratorForProject(java_project, im);
			TensorForProject one_project_tensor = irgfop.GenerateForOneProject();
			SaveTensorToFile.SaveTensors(one_project_tensor, dest);
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
