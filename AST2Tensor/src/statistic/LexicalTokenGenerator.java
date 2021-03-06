package statistic;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import statis.trans.common.BasicGenerator;
import statis.trans.common.RoleAssigner;
import statis.trans.common.YTokenizer;
import statistic.id.IDManager;

public class LexicalTokenGenerator extends BasicGenerator {
	
	IDTools tool = null;
	int role = -1;

	public LexicalTokenGenerator(IDManager im, ICompilationUnit icu, CompilationUnit cu, IDTools tool) {
		super(im, icu, cu);
		this.tool = tool;
		this.role = RoleAssigner.GetInstance().GetRole(icu);
	}
	
	@Override
	protected void WholePostHandle(ASTNode node) {
		if (node instanceof MethodDeclaration) {
			String content = node.toString();
			ArrayList<String> tks = YTokenizer.GetTokens(content);
			for (String tk : tks) {
				if (role <= RoleAssigner.train_seen_k) {
					tool.tr.TokenHitInTrainSet(tk, 1);// "null", 
				} else {
					tool.tr.TokenNotHitInTrainSet(tk, 1);
				}
			}
		}
	}
	
}
