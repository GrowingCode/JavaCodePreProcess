package bpe.skt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import statis.trans.common.BasicGenerator;
import statis.trans.common.RoleAssigner;
import statistic.IDTools;
import statistic.id.IDManager;
import translation.ast.StatementUtil;
import tree.ExprSpecTreeNode;
import tree.Forest;
import tree.Tree;
import tree.TreeNode;

public class SktForestGenerator extends BasicGenerator {
	
	ArrayList<Forest> funcs = new ArrayList<Forest>();
	
	ArrayList<ASTNode> stmt_roots = new ArrayList<ASTNode>();
	
	IDTools tool = null;
	
	public SktForestGenerator(IDManager im, ICompilationUnit icu, CompilationUnit cu,
			IDTools tool) {
		super(im, icu, cu);
		this.tool = tool;
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
			SktTreeGenerator stg = null;
			try {
				stg = new SktTreeGenerator(icu.getBuffer().getContents());
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			Assert.isTrue(stg != null);
			stmt_root.accept(stg);
			TreeNode root_tree_node = stg.node_record.get(stg.t_root);
			Tree t = new Tree(root_tree_node);
//			t.AddTreeNodes(stg.node_record.values());
			stmts.add(t);
		}
		Assert.isTrue(stmt_roots.get(0).equals(node));
		Forest f = new Forest(icu.getPath().toOSString(), JDTASTHelper.GetSimplifiedSignatureForMethodDeclaration(node), RoleAssigner.GetInstance().GetRole(icu));
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
	
	String icu_buffer = null;
	
	ASTNode t_root = null;
	Map<ASTNode, ASTNode> parent_record = new HashMap<ASTNode, ASTNode>();
	Map<ASTNode, TreeNode> node_record = new HashMap<ASTNode, TreeNode>();
	
	public SktTreeGenerator(String icu_buffer) {
		this.icu_buffer = icu_buffer;
	}
	
	@Override
	public boolean preVisit2(ASTNode node) {
		boolean ctn = super.preVisit2(node);
		if (t_root == null) {
			t_root = node;
		}
		ArrayList<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		boolean is_leaf = children.size() == 0;
		if (node instanceof Block) {
			Assert.isTrue(t_root != node);
			is_leaf = true;
		}
		
		int n_start = node.getStartPosition();
		int n_length = node.getLength();
		
		String node_cnt = icu_buffer.substring(n_start, n_start + n_length);
		StringBuilder node_cnt_builder = new StringBuilder(node_cnt);

		boolean ctn_handle = true;
		boolean to_create_tree_node = true;
		String node_whole_cnt = node.toString();
		if (node instanceof Statement) {
			if ((node instanceof Block) || (t_root != node)) {
				ctn = ctn && false;
				ctn_handle = ctn_handle && false;
			}
			if (node instanceof Block) {
				ctn_handle = true;
				node_cnt = "{}";
				node_whole_cnt = "{}";
			}
		}
		if (ctn_handle) {
//		} else {
//			Assert.isTrue(!(node instanceof Statement));
			if (!is_leaf) {
				int c_size = children.size();
				int prev_c_start = Integer.MAX_VALUE;
				for (int i = c_size - 1; i >= 0; i--) {
					ASTNode c = children.get(i);
					
					String holder = "#h";
					ASTNode r_c = JDTASTHelper.SkipPassThroughNodes(c);
					if (JDTASTHelper.IsIDLeafNode(r_c.getClass())) {
						holder = "#v";
					}
					
					int c_start = c.getStartPosition();
					int c_length = c.getLength();
					
					Assert.isTrue(prev_c_start >= c_start + c_length, "prev_c_start:" + prev_c_start + "#c_start:" + c_start + "#c_length:" + c_length);
					
					int r_start = c_start - n_start;
//					System.err.println("start:" + r_start + "#end:" + (r_start + c_length) + "#total:" + node_cnt_builder.length());
					node_cnt_builder.replace(r_start, r_start + c_length, holder);
					
					prev_c_start = c_start;
				}
				node_cnt = node_cnt_builder.toString().trim();
//				if (node_cnt.trim().equals("")) Assert.isTrue(node_cnt.equals(""));
//				node_cnt.equals("#h") || node_cnt.equals("#v")
				if (JDTASTHelper.IsPassThroughNode(node)) {
					Assert.isTrue(node_cnt.equals("#h") || node_cnt.equals("#v"), "strange node_cnt:" + node_cnt);
					Assert.isTrue(c_size == 1);
					ASTNode c0 = children.get(0);
//					System.err.println("pass through:" + c0.getClass());
					ASTNode r_parent = parent_record.get(node);
					if (r_parent == null) {
						r_parent = node.getParent();
					}
					parent_record.put(c0, r_parent);
					to_create_tree_node = false;
				}
			}
			if (to_create_tree_node) {
				// create tree node
				TreeNode tn = null;
				if (JDTASTHelper.IsExprSpecPattern(node)) {
					tn = new ExprSpecTreeNode(node.getClass(), null, node_cnt, node_whole_cnt, JDTASTHelper.GetExprSpec(node) != null);
				} else {
					tn = new TreeNode(node.getClass(), null, node_cnt, node_whole_cnt);
				}
				node_record.put(node, tn);
				
				ASTNode r_parent = parent_record.get(node);
				if (r_parent == null) {
					r_parent = node.getParent();
				}
				Assert.isTrue(r_parent != null);
				TreeNode rp_tn = node_record.get(r_parent);
				if (rp_tn != null) {
					rp_tn.AppendToChildren(tn);
				} else {
					Assert.isTrue(node == t_root);
				}
			}
		}
		return ctn;
	}
	
}



