package statis.trans.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import main.MetaOfApp;
import statistic.id.IDManager;
import translation.helper.DecodeType;
import translation.helper.TestDataDecodeType;
import translation.roles.RoleAssigner;
import translation.tensor.StringTensor;
import translation.tensor.Tensor;
import tree.TreeNode;
import tree.TreeVisit;
import tree.TreeVisitor;

public class BasicGenerator extends ASTVisitor {

	protected RoleAssigner role_assigner = null;
	protected IDManager im = null;

	protected ICompilationUnit icu = null;
	protected CompilationUnit cu = null;

	protected DecodeType decode_type_generator = null;

	protected LinkedList<Tensor> tensor_list = new LinkedList<Tensor>();

	protected TreeVisitor visitor = null;
	
	public int unsuitable_method_count = 0;
	public int total_method_count = 0;
	
	public BasicGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu, CompilationUnit cu,
			TreeVisitor visitor) {
		this.role_assigner = role_assigner;
		this.im = im;
		this.icu = icu;
		this.cu = cu;
		this.visitor = visitor;
	}

	public List<Tensor> GetGeneratedTensors() {
		return tensor_list;
	}

	protected ASTNode cared_parent = null;
	protected boolean begin_generation = false;
	protected ASTNode begin_generation_node = null;
	protected Map<ASTNode, TreeNode> tree = new HashMap<ASTNode, TreeNode>();

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		ASTNode node_parent = node.getParent();
		if (node_parent == null) {
			cared_parent = node;
		}
		if (cared_parent != null && StandAtRootNodeOfTensorGeneration(node, node_parent)
				&& UnHandledASTNodesFilterPassed(node)) {
			decode_type_generator = new TestDataDecodeType();
			begin_generation = true;
			begin_generation_node = node;
			Assert.isTrue(cared_parent == null && begin_generation == false && begin_generation_node == null && tree.isEmpty());
		}
		if (begin_generation) {
			String type = JDTASTHelper.GetTypeRepresentationForASTNode(node);
			TreeNode tn = new TreeNode(node.getClass(), type);
			tree.put(node, tn);
			ASTNode parent = node.getParent();
			TreeNode parent_tn = tree.get(parent);
			if (parent_tn == null) {
				Assert.isTrue(node.equals(begin_generation_node));
			} else {
				parent_tn.AppendToChildren(tn);
			}

			List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
			boolean is_leaf = children.size() == 0;
			if (is_leaf) {
				String content = JDTASTHelper.GetContentRepresentationForASTNode(node);
				TreeNode chd_tn = new TreeNode(String.class, content);
				tn.AppendToChildren(chd_tn);
			}
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
				if (MetaOfApp.StatementNoLimit || (tree.size() >= MetaOfApp.MinimumNumberOfNodesInAST)) {
					TreeNode root = tree.get(node);
					visitor.Clear();
					TreeVisit.Visit(root, visitor);
					StringTensor st = visitor.GetStringTensor();
					st.SetRole(role_assigner.GetRole(icu.getPath().toOSString()));
					tensor_list.add(st);
					decode_type_generator = null;
					begin_generation = false;
					begin_generation_node = null;
					tree.clear();
				}
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
