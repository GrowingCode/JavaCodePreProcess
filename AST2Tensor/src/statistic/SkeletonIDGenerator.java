package statistic;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import statis.trans.common.BasicGenerator;
import statistic.id.IDManager;
import translation.ast.StatementUtil;
import translation.roles.RoleAssigner;

public class SkeletonIDGenerator extends BasicGenerator {
	
	IDTools tool = null;
	int role = -1;
	ArrayList<ArrayList<String>> stmts = new ArrayList<ArrayList<String>>();
	
	public SkeletonIDGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu, CompilationUnit cu, IDTools tool) {
		super(role_assigner, im, icu, cu);
		this.tool = tool;
		this.role = tool.role_assigner.AssignRole(icu.getPath().toOSString());
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (begin_generation) {
			if (StatementUtil.IsStatement(node.getClass()) || StatementUtil.IsMethodDeclaration(node.getClass())) {
				ArrayList<String> lls = StatementUtil.ProcessSkeleton(icu, node);
				stmts.add(lls);
			}
		}
	}

	@Override
	protected void WholePostHandle(ASTNode node) {
		for (ArrayList<String> stmt : stmts) {
			String sk = stmt.get(0);
			if (role <= RoleAssigner.train_seen_k) {
				tool.sr.TokenHitInTrainSet(sk, 1);
			} else {
				tool.sr.TokenNotHitInTrainSet(sk, 1);
			}
			for (int i=1;i<stmt.size();i++) {
				String tk = stmt.get(i);
				if (role <= RoleAssigner.train_seen_k) {
					tool.tr.TokenHitInTrainSet(tk, 1);
				} else {
					tool.tr.TokenNotHitInTrainSet(tk, 1);
				}
			}
		}
	}
	
	@Override
	protected void WholePostClear(ASTNode node) {
		stmts.clear();
	}
	
}
