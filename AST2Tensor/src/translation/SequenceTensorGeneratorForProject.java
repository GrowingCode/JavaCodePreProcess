package translation;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.CompilationUnit;

import statistic.id.IDManager;

public class SequenceTensorGeneratorForProject extends TensorGeneratorForProject {
	
	public SequenceTensorGeneratorForProject(RoleAssigner role_assigner, IJavaProject java_project, IDManager im) {
		super(role_assigner, java_project, im);
	}

	@Override
	protected TensorGenerator GenerateTensorGeneratorVisitor(ICompilationUnit icu, CompilationUnit cu) {
		// RoleAssigner role_assigner, IJavaProject java_project, IDManager im, 
		return new SequenceTensorGenerator(role_assigner, java_project, im, icu, cu);
	}
	
}
