package translation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import statistic.id.IDManager;
import translation.helper.DecodeType;
import translation.tensor.Tensor;

public class TensorGenerator extends ASTVisitor {
	
	RoleAssigner role_assigner = null;
	IJavaProject java_project = null;
	IDManager im = null;

	ICompilationUnit icu = null;
	CompilationUnit cu = null;

	DecodeType decode_type_generator = null;
	
	LinkedList<Tensor> tensor_list = new LinkedList<Tensor>();
	
	public TensorGenerator(RoleAssigner role_assigner, IJavaProject java_project, IDManager im, ICompilationUnit icu,
			CompilationUnit cu) {
		this.role_assigner = role_assigner;
		this.java_project = java_project;
		this.im = im;
		this.icu = icu;
		this.cu = cu;
	}
	
	public List<Tensor> GetGeneratedTensors() {
		return tensor_list;
	}
	
}
