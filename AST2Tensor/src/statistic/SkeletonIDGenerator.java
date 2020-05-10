package statistic;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import statis.trans.common.BasicGenerator;
import statis.trans.common.RoleAssigner;
import statistic.id.IDManager;
import statistic.id.PreProcessContentHelper;
import translation.ast.StatementUtil;
import util.visitor.TokenHandleSkeletonVisitor;

public class SkeletonIDGenerator extends BasicGenerator {
	
	IDTools tool = null;
	int role = -1;
	ArrayList<ArrayList<String>> stmts = new ArrayList<ArrayList<String>>();
	
	public SkeletonIDGenerator(IDManager im, ICompilationUnit icu, CompilationUnit cu, IDTools tool) {
		super(im, icu, cu);
		this.tool = tool;
		this.role = RoleAssigner.GetInstance().GetRole(icu);
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (begin_generation) {
			if (StatementUtil.IsStatement(node.getClass()) || StatementUtil.IsMethodDeclaration(node.getClass())) {
				TokenHandleSkeletonVisitor sv = new TokenHandleSkeletonVisitor(icu);
				node.accept(sv);
				ArrayList<String> lls = sv.GetResult();
				stmts.add(lls);
			}
		}
	}

	@Override
	protected void WholePostHandle(ASTNode node) {
		for (ArrayList<String> stmt : stmts) {
			String sk = stmt.get(0);
			if (role <= RoleAssigner.train_seen_k) {
				tool.sr.TokenHitInTrainSet(sk, 1);// "skt", 
			} else {
				tool.sr.TokenNotHitInTrainSet(sk, 1);
			}
			for (int i=1;i<stmt.size();i++) {
				String tk = stmt.get(i);
				String pp_tk = PreProcessContentHelper.PreProcessTypeContent(tk);
				if (role <= RoleAssigner.train_seen_k) {
					tool.tr.TokenHitInTrainSet(pp_tk, 0);// sk, 
//					tool.str.TokenHitInTrainSet(pp_tk, 1);
				} else {
					tool.tr.TokenNotHitInTrainSet(pp_tk, 0);
//					tool.str.TokenNotHitInTrainSet(pp_tk, 1);
				}
			}
		}
	}
	
	@Override
	protected void WholePostClear(ASTNode node) {
		stmts.clear();
	}
	
}
