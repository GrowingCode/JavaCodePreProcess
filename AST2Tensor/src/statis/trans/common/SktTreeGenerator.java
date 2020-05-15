package statis.trans.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import tree.Forest;
import tree.Tree;
import tree.TreeNode;
import tree.TreeVisitor;

public class SktTreeGenerator extends BasicGenerator {
	
	ArrayList<Forest> funcs = new ArrayList<Forest>();
	
	ArrayList<Tree> stmts = new ArrayList<Tree>();
	
	Map<ASTNode, ASTNode> parent_record = new HashMap<ASTNode, ASTNode>();
	Map<ASTNode, TreeNode> node_record = new HashMap<ASTNode, TreeNode>();
	
	public SktTreeGenerator(IDManager im, ICompilationUnit icu, CompilationUnit cu,
			TreeVisitor visitor) {
		super(im, icu, cu);
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (begin_generation) {
			ArrayList<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
			boolean is_leaf = children.size() == 0;
			StringBuilder node_cnt_builder = new StringBuilder(node.toString());
			int n_start = node.getStartPosition();
//			int n_length = node.getLength();
			
			if (!is_leaf) {
				int c_size = children.size();
				int prev_c_start = Integer.MAX_VALUE;
				for (int i = c_size - 1; i >= 0; i--) {
					ASTNode c = children.get(i);
					int c_start = c.getStartPosition();
					int c_length = c.getLength();
					
					Assert.isTrue(prev_c_start > c_start + c_length);
					
					int r_start = c_start - n_start;
					node_cnt_builder.delete(r_start, r_start + c_length);
					
					prev_c_start = c_start;
				}
				String node_cnt = node_cnt_builder.toString();
				if (node_cnt.equals("")) {
					Assert.isTrue(c_size == 1);
					ASTNode c0 = children.get(0);
					parent_record.put(c0, node.getParent());
				} else {
					
				}
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
		parent_record.clear();
		node_record.clear();
	}
	
	public ArrayList<Forest> GetFunctions() {
		return funcs;
	}
	
}
