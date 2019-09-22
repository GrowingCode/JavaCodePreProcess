package statistic;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTParser;
import eclipse.search.EclipseSearchForICompilationUnits;
import logger.DebugLogger;
import statis.trans.common.YTreeGenerator;

public class IDGeneratorForProject {

	IJavaProject java_project = null;
	IDTools tool = null;

	public IDGeneratorForProject(IJavaProject java_project, IDTools tool) {
		this.java_project = java_project;
		this.tool = tool;
	}

	public int GenerateForOneProject() {
		List<ICompilationUnit> units = null;
		try {
			units = EclipseSearchForICompilationUnits.SearchForAllICompilationUnits(java_project);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		DebugLogger.Log("ID: ICompilationUnit_size of project:" + units.size());
		int length = 0;
		if (units != null) {
			for (ICompilationUnit icu : units) {
				CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
				length += cu.getLength();
				// CreateJDTParserWithJavaProject(java_project).
				Assert.isTrue(icu != null);
				IDGenerator tg_id_visitor = new IDGenerator(tool, icu);
				YTreeGenerator tg = new YTreeGenerator(tool.role_assigner, null, icu, cu, tg_id_visitor);
				cu.accept(tg);
				SkeletonIDGenerator skg = new SkeletonIDGenerator(tool.role_assigner, null, icu, cu, tool);
				cu.accept(skg);
			}
		}
		return length;
	}

}
