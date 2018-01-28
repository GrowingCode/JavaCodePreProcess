package translation;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.CompilationUnit;

import statistic.id.IDManager;

public class TreeTensorGeneratorForProject extends TensorGeneratorForProject {

	public TreeTensorGeneratorForProject(RoleAssigner role_assigner, IJavaProject java_project, IDManager im) {
		super(role_assigner, java_project, im);
	}

	@Override
	protected TensorGenerator GenerateTensorGeneratorVisitor(ICompilationUnit icu, CompilationUnit cu) {
		return new TreeTensorGenerator(role_assigner, java_project, im, icu, cu);
	}

}
