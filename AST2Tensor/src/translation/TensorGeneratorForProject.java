package translation;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTParser;
import eclipse.search.EclipseSearchForICompilationUnits;
import logger.DebugLogger;
import translation.tensor.TensorForProject;

public class TensorGeneratorForProject {
	
	IJavaProject java_project = null;
	
	public TensorGeneratorForProject(IJavaProject java_project) {
		this.java_project = java_project;
	}

	public TensorForProject GenerateForOneProject() {
		// TODO Auto-generated method stub
		List<ICompilationUnit> units = null;
		try {
			units = EclipseSearchForICompilationUnits.SearchForAllICompilationUnits(java_project);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		DebugLogger.Log("ICompilationUnit_size:" + units.size());
		if (units != null) {
			for (ICompilationUnit icu : units) {
				CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
				// CreateJDTParserWithJavaProject(java_project).
				TensorGenerator tg = new TensorGenerator(
						java_project, icu, cu);
				cu.accept(tg);
			}
		}
		return null;
	}
	
}
