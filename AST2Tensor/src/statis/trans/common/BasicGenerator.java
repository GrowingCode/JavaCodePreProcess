package statis.trans.common;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import main.MetaOfApp;
import statistic.id.IDManager;
import translation.roles.RoleAssigner;
import translation.tensor.Tensor;

public class BasicGenerator extends ASTVisitor {

	protected RoleAssigner role_assigner = null;
	protected IDManager im = null;

	protected ICompilationUnit icu = null;
	protected CompilationUnit cu = null;

	protected LinkedList<Tensor> tensor_list = new LinkedList<Tensor>();

	public int total_method_count = 0;
	public int unsuitable_method_count = 0;
	protected int method_node_count = 0;

	public BasicGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu, CompilationUnit cu) {
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
		if (cared_parent != null && StandAtRootNodeOfTensorGeneration(node, node_parent)
				&& UnHandledASTNodesFilterPassed(node)) {
			Assert.isTrue(begin_generation == false && begin_generation_node == null && method_node_count == 0,// && tree.isEmpty()
					"begin_generation:" + begin_generation + "#begin_generation_node:" + (begin_generation_node == null));// + "#tree.isEmpty():" + tree.isEmpty()
			begin_generation = true;
			begin_generation_node = node;
		}
		if (begin_generation) {
			method_node_count++;
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
				total_method_count++;
				if (MetaOfApp.StatementNoLimit || (method_node_count >= MetaOfApp.MinimumNumberOfNodesInAST)) {
					WholePostHandle(node);
				} else {
					unsuitable_method_count++;
				}
				WholePostClear(node);
				begin_generation = false;
				begin_generation_node = null;
				method_node_count = 0;
			}
		}
		super.postVisit(node);
	}
	
	protected void WholePostHandle(ASTNode node) {
	}
	
	protected void WholePostClear(ASTNode node) {
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
