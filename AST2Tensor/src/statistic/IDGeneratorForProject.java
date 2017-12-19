package statistic;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTParser;
import eclipse.search.EclipseSearchForICompilationUnits;
import logger.DebugLogger;
import statistic.id.IDManager;

public class IDGeneratorForProject {
	
	IJavaProject java_project = null;
	IDManager im = null;
	
	public IDGeneratorForProject(IJavaProject java_project, IDManager im) {
		this.java_project = java_project;
		this.im = im;
	}
	
	public int GenerateForOneProject() {
		List<ICompilationUnit> units = null;
		try {
			units = EclipseSearchForICompilationUnits.SearchForAllICompilationUnits(java_project);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		DebugLogger.Log("ICompilationUnit_size of project:" + units.size());
		int length = 0;
		if (units != null) {
			for (ICompilationUnit icu : units) {
				CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
				length += cu.getLength();
				// CreateJDTParserWithJavaProject(java_project).
				IDGenerator tg = new IDGenerator(java_project, icu, cu, im);
				cu.accept(tg);
			}
		}
		return length;
	}
	
}
