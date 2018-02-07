package eclipse.project;

import org.eclipse.jdt.core.IJavaProject;

import logger.DebugLogger;

public class ProjectLoader {
	
	public static IJavaProject LoadProjectAccordingToArgs(String proj_path) throws Exception {
		// load projects
		// proj_path - example: D:/eclipse-workspace-pool/eclipse-rcp-neon-codecompletion/cn.yyx.research.program.snippet.extractor
		ProjectInfo epi = new ProjectInfo("no_use", proj_path);
		IJavaProject jproj = AnalysisEnvironment.CreateAnalysisEnvironment(epi);
		DebugLogger.Log("Just for test, this is the args:" + proj_path);
		return jproj;
	}
	
}
