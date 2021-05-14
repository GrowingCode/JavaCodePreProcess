package statis.trans.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import eclipse.bind.BindingResolveUtil;
import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import main.MetaOfApp;
import statistic.id.IDManager;
import translation.tensor.StringTensor;
import translation.tensor.TensorInfo;
import tree.ExprSpecTreeNode;
import tree.TreeNode;
import tree.TreeVisit;
import tree.TreeVisitor;
import util.visitor.SkeletonVisitor;

public class YTreeGenerator extends BasicGenerator {
	
	public int min_num_node_in_one_ast = Integer.MAX_VALUE;
	public int max_num_node_in_one_ast = Integer.MIN_VALUE;

	protected TreeVisitor visitor = null;
	
	protected Map<ASTNode, TreeNode> tree = new HashMap<ASTNode, TreeNode>();

	public YTreeGenerator(IDManager im, ICompilationUnit icu, CompilationUnit cu,
			TreeVisitor visitor) {
		super(im, icu, cu);
		this.visitor = visitor;
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (begin_generation) {
			String type = JDTASTHelper.GetTypeRepresentationForASTNode(node);
			List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
			boolean is_leaf = children.size() == 0;
			SkeletonVisitor sv = new SkeletonVisitor(icu);
			node.accept(sv);
			Assert.isTrue(sv.GetResult().size() == 1);
			String stmt_content = sv.GetResult().get(0);
			String r_content = type;
			if (is_leaf && !MetaOfApp.LeafTypeContentSeparate) {
				r_content = JDTASTHelper.GetContentRepresentationForASTNode(node);
			}
			
//			int sib_index = 0;
			ASTNode parent = node.getParent();
			TreeNode parent_tn = tree.get(parent);
			if (parent_tn == null) {
				Assert.isTrue(node.equals(begin_generation_node));
			} else {
//				sib_index = parent_tn.GetChildren().size();
			}
			
			TreeNode tn = null;
			if (JDTASTHelper.IsExprSpecPattern(node)) {
				tn = new ExprSpecTreeNode(node.getClass(), is_leaf, BindingResolveUtil.ResolveBinding(node), r_content, stmt_content, JDTASTHelper.GetExprSpec(node) != null);//, sib_index
				} else {
				tn = new TreeNode(node.getClass(), is_leaf, BindingResolveUtil.ResolveBinding(node), r_content, stmt_content);//, sib_index
			}
			tree.put(node, tn);
			if (parent_tn != null) {
				parent_tn.AppendToChildren(tn);
			}
			
			if (is_leaf && MetaOfApp.LeafTypeContentSeparate) {
				String content = JDTASTHelper.GetContentRepresentationForASTNode(node);
				TreeNode chd_tn = new TreeNode(String.class, is_leaf, null, content, stmt_content);//, tn.GetChildren().size()
				tn.AppendToChildren(chd_tn);
			}
		}
	}
	
	@Override
	protected void WholePostHandle(ASTNode node) {
		min_num_node_in_one_ast = min_num_node_in_one_ast > tree.size() ? tree.size() : min_num_node_in_one_ast;
		max_num_node_in_one_ast = max_num_node_in_one_ast < tree.size() ? tree.size() : max_num_node_in_one_ast;
		
		TreeNode root = tree.get(node);
		Assert.isTrue(node instanceof MethodDeclaration);
		visitor.ClearAndInitialize(new TensorInfo(icu.getPath().toOSString(), ((MethodDeclaration)node).getName().toString()));
		TreeVisit.Visit(root, visitor);
		StringTensor st = visitor.GetStringTensor();
		if (st != null) {
			st.SetRole(RoleAssigner.GetInstance().GetRole(icu));
			tensor_list.add(st);
		}
	}
	
	@Override
	protected void WholePostClear(ASTNode node) {
		tree.clear();
	}
	
}
