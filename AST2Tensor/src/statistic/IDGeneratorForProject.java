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
import statistic.ast.ChildrenNumCounter;
import statistic.id.IDCounter;
import translation.roles.RoleAssigner;

public class IDGeneratorForProject {
	
	IJavaProject java_project = null;
	RoleAssigner role_assigner = null;
	IDCounter ic = null;
	ChildrenNumCounter cnc = null;
	
	public IDGeneratorForProject(IJavaProject java_project, RoleAssigner role_assigner, IDCounter ic, ChildrenNumCounter cnc) {
		this.java_project = java_project;
		this.role_assigner = role_assigner;
		this.ic = ic;
		this.cnc = cnc;
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
				int role = role_assigner.AssignRole(icu.getElementName());
				if (role <= RoleAssigner.train_seen_k) {
					IDGenerator tg = new IDGenerator(icu, cu, ic, cnc);
					cu.accept(tg);
				}
				IDGenerator tg = new IDGenerator(icu, cu, ic, cnc);
				cu.accept(tg);
			}
		}
		return length;
	}
	
}
