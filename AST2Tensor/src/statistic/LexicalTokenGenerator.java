package statistic;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import statis.trans.common.BasicGenerator;
import statis.trans.common.YTokenizer;
import statistic.id.IDManager;
import translation.roles.RoleAssigner;

public class LexicalTokenGenerator extends BasicGenerator {
	
	IDTools tool = null;
	int role = -1;

	public LexicalTokenGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu, CompilationUnit cu, IDTools tool) {
		super(role_assigner, im, icu, cu);
		this.tool = tool;
		this.role = tool.role_assigner.GetRole(icu.getPath().toOSString());
	}
	
	@Override
	protected void WholePostHandle(ASTNode node) {
		if (node instanceof MethodDeclaration) {
			String content = node.toString();
			ArrayList<String> tks = YTokenizer.GetTokens(content);
			for (String tk : tks) {
				if (role <= RoleAssigner.train_seen_k) {
					tool.tr.TokenHitInTrainSet("null", tk, 1);
				} else {
					tool.tr.TokenNotHitInTrainSet(tk, 1);
				}
			}
		}
	}
	
}
