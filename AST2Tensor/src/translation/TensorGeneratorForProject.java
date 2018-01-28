package translation;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTParser;
import eclipse.search.EclipseSearchForICompilationUnits;
import logger.DebugLogger;
import statistic.id.IDManager;
import translation.tensor.TensorForProject;

public abstract class TensorGeneratorForProject {
	
	RoleAssigner role_assigner = null;
	IJavaProject java_project = null;
	IDManager im = null;
	
	public TensorGeneratorForProject(RoleAssigner role_assigner, IJavaProject java_project, IDManager im) {
		this.role_assigner = role_assigner;
		this.java_project = java_project;
		this.im = im;
	}

	public TensorForProject GenerateForOneProject() {
		TensorForProject result = new TensorForProject();
		List<ICompilationUnit> units = null;
		try {
			units = EclipseSearchForICompilationUnits.SearchForAllICompilationUnits(java_project);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		DebugLogger.Log("Tensor: ICompilationUnit_size:" + units.size());
		if (units != null) {
			for (ICompilationUnit icu : units) {
				CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
				// CreateJDTParserWithJavaProject(java_project).
				TensorGenerator tg = GenerateTensorGeneratorVisitor(icu, cu);
				// role_assigner, java_project, im, 
				// TensorGenerator tg = new TensorGenerator(
				//		role_assigner, java_project, im, icu, cu);
				cu.accept(tg);
				result.AddTensors(tg.GetGeneratedTensors());
			}
		}
		return result;
	}
	
	// RoleAssigner role_assigner, IJavaProject java_project, IDManager im, 
	protected abstract TensorGenerator GenerateTensorGeneratorVisitor(ICompilationUnit icu, CompilationUnit cu);
	
}
