package statis.trans.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import translation.roles.RoleAssigner;
import translation.tensor.StringTensor;
import tree.TreeNode;
import tree.TreeVisit;
import tree.TreeVisitor;

public class YTreeGenerator extends BasicGenerator {

	protected TreeVisitor visitor = null;
	
	protected Map<ASTNode, TreeNode> tree = new HashMap<ASTNode, TreeNode>();

	public YTreeGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu, CompilationUnit cu,
			TreeVisitor visitor) {
		super(role_assigner, im, icu, cu);
		this.visitor = visitor;
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
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
	protected void WholePostHandle(ASTNode node) {
		TreeNode root = tree.get(node);
		visitor.Clear();
		TreeVisit.Visit(root, visitor);
		StringTensor st = visitor.GetStringTensor();
		if (st != null) {
			st.SetRole(role_assigner.GetRole(icu.getPath().toOSString()));
			tensor_list.add(st);
		}
	}
	
	@Override
	protected void WholePostClear(ASTNode node) {
		tree.clear();
	}
	
}
