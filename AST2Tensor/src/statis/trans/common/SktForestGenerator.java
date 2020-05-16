package statis.trans.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import translation.ast.StatementUtil;
import tree.Forest;
import tree.Tree;
import tree.TreeNode;
import tree.TreeVisitor;

public class SktForestGenerator extends BasicGenerator {
	
	ArrayList<Forest> funcs = new ArrayList<Forest>();
	
	ArrayList<ASTNode> stmt_roots = new ArrayList<ASTNode>();
	
	public SktForestGenerator(IDManager im, ICompilationUnit icu, CompilationUnit cu,
			TreeVisitor visitor) {
		super(im, icu, cu);
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (begin_generation) {
			if (StatementUtil.IsStatement(node.getClass()) || StatementUtil.IsMethodDeclaration(node.getClass())) {
				stmt_roots.add(node);
			}
		}
	}
	
	@Override
	protected void WholePostHandle(ASTNode node) {
		ArrayList<Tree> stmts = new ArrayList<Tree>();
		for (ASTNode stmt_root : stmt_roots) {
			SktTreeGenerator stg = new SktTreeGenerator();
			stmt_root.accept(stg);
			TreeNode root_tree_node = stg.node_record.get(stg.t_root);
			Tree t = new Tree(root_tree_node);
			t.AddTreeNodes(stg.node_record.values());
			stmts.add(t);
		}
		Forest f = new Forest();
		f.AddTrees(stmts);
		funcs.add(f);
	}
	
	@Override
	protected void WholePostClear(ASTNode node) {
		stmt_roots.clear();
	}
	
	public ArrayList<Forest> GetFunctions() {
		return funcs;
	}
	
}

class SktTreeGenerator extends ASTVisitor {
	
	ASTNode t_root = null;
	Map<ASTNode, ASTNode> parent_record = new HashMap<ASTNode, ASTNode>();
	Map<ASTNode, TreeNode> node_record = new HashMap<ASTNode, TreeNode>();
	
	public SktTreeGenerator() {
	}
	
	@Override
	public boolean preVisit2(ASTNode node) {
		boolean ctn = super.preVisit2(node);
		if (t_root == null) {
			t_root = node;
		}
		ArrayList<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		boolean is_leaf = children.size() == 0;
		StringBuilder node_cnt_builder = new StringBuilder(node.toString());
		int n_start = node.getStartPosition();
//		int n_length = node.getLength();

		boolean to_create_tree_node = true;
		String node_cnt = node.toString();
		String node_whole_cnt = node.toString();
		if (node instanceof Block) {
			ctn = ctn && false;
			node_cnt = "{}";
			node_whole_cnt = "{}";
		} else {
			Assert.isTrue(!(node instanceof Statement));
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
				node_cnt = node_cnt_builder.toString();
				if (node_cnt.trim().equals("")) Assert.isTrue(node_cnt.equals(""));
				if (node_cnt.equals("")) {
					Assert.isTrue(c_size == 1);
					ASTNode c0 = children.get(0);
					parent_record.put(c0, node.getParent());
					to_create_tree_node = false;
				}
			}
		}
		if (to_create_tree_node) {
			// create tree node
			TreeNode tn = new TreeNode(node.getClass(), null, node_cnt, node_whole_cnt);
			node_record.put(node, tn);
			
			ASTNode r_parent = parent_record.get(node);
			if (r_parent == null) {
				r_parent = node.getParent();
			}
			TreeNode rp_tn = node_record.get(r_parent);
			rp_tn.AppendToChildren(tn);
		}
		return ctn;
	}
	
}



