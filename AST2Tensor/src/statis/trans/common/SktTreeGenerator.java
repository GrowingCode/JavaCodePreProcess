package statis.trans.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.bind.BindingResolveUtil;
import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import main.MetaOfApp;
import statistic.id.IDManager;
import translation.tensor.StringTensor;
import tree.Forest;
import tree.Tree;
import tree.TreeNode;
import tree.TreeVisit;
import tree.TreeVisitor;
import util.visitor.SkeletonVisitor;

public class SktTreeGenerator extends BasicGenerator {
	
	ArrayList<Tree> stmts = new ArrayList<Tree>();
	
	ArrayList<Forest> funcs = new ArrayList<Forest>();

	public SktTreeGenerator(IDManager im, ICompilationUnit icu, CompilationUnit cu,
			TreeVisitor visitor) {
		super(im, icu, cu);
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
			TreeNode tn = new TreeNode(node.getClass(), BindingResolveUtil.ResolveVariableBinding(node), r_content, stmt_content);// type + add_content
			tree.put(node, tn);
			ASTNode parent = node.getParent();
			TreeNode parent_tn = tree.get(parent);
			if (parent_tn == null) {
				Assert.isTrue(node.equals(begin_generation_node));
			} else {
				parent_tn.AppendToChildren(tn);
			}
			if (is_leaf && MetaOfApp.LeafTypeContentSeparate) {
				String content = JDTASTHelper.GetContentRepresentationForASTNode(node);
				TreeNode chd_tn = new TreeNode(String.class, null, content, stmt_content);
				tn.AppendToChildren(chd_tn);
			}
		}
	}
	
	@Override
	protected void WholePostHandle(ASTNode node) {
		Forest f = new Forest();
		f.AddTrees(stmts);
		funcs.add(f);
	}
	
	@Override
	protected void WholePostClear(ASTNode node) {
		stmts.clear();
	}
	
	public ArrayList<Forest> GetFunctions() {
		return funcs;
	}
	
}
