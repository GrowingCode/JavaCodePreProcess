package translation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import main.MetaOfApp;
import statistic.id.IDManager;
import translation.helper.DecodeType;
import translation.helper.TestDataDecodeType;
import translation.roles.RoleAssigner;
import translation.tensor.StringTensor;
import translation.tensor.Tensor;

public class TensorGenerator extends ASTVisitor {
	
	protected RoleAssigner role_assigner = null;
	protected IDManager im = null;

	protected ICompilationUnit icu = null;
	protected CompilationUnit cu = null;

	protected DecodeType decode_type_generator = null;
	
	protected LinkedList<Tensor> tensor_list = new LinkedList<Tensor>();
	
	public TensorGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu,
			CompilationUnit cu) {
		this.role_assigner = role_assigner;
		this.im = im;
		this.icu = icu;
		this.cu = cu;
	}
	
	public List<Tensor> GetGeneratedTensors() {
		return tensor_list;
	}
	
	protected ASTNode cared_parent = null;
	protected boolean begin_generation = false;
	protected ASTNode begin_generation_node = null;

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		ASTNode node_parent = node.getParent();
		if (node_parent == null) {
			cared_parent = node;
		}
		if (cared_parent != null && StandAtRootNodeOfTensorGeneration(node, node_parent) && UnHandledASTNodesFilterPassed(node)) {
			int role = role_assigner.AssignRole(icu.getElementName());
			tensor_list.add(new StringTensor(icu.getElementName(), role));
			decode_type_generator = new TestDataDecodeType();
			begin_generation = true;
			begin_generation_node = node;
		}
	}

	@Override
	public void postVisit(ASTNode node) {
		ASTNode node_parent = node.getParent();
		if (node_parent == null) {
			cared_parent = null;
		}
		if (begin_generation) {
			if (begin_generation_node.equals(node)) {
				decode_type_generator = null;
				begin_generation = false;
				begin_generation_node = null;
			}
		}
		super.postVisit(node);
	}

	private boolean StandAtRootNodeOfTensorGeneration(ASTNode node, ASTNode node_parent) {
		boolean to_generate = false;
		if (MetaOfApp.ClassLevelTensorGeneration) {
			if (cared_parent.equals(node_parent)) {
				to_generate = true;
			}
		} else {
			if (node_parent != null && cared_parent.equals(node_parent.getParent())
					&& (node instanceof MethodDeclaration)) {
				to_generate = true;
			}
		}
		return to_generate;
	}
	
	private boolean UnHandledASTNodesFilterPassed(ASTNode node) {
		if ((node instanceof PackageDeclaration) || (node instanceof ImportDeclaration)) {
			return false;
		}
		return true;
	}
	
}
