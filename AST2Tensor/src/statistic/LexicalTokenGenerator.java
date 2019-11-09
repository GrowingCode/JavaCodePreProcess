package statistic;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import statis.trans.common.YTokenizer;
import translation.roles.RoleAssigner;

public class LexicalTokenGenerator extends ASTVisitor {
	
	ICompilationUnit icu = null;
	CompilationUnit cu = null;
	IDTools tool = null;
	
	int role = -1;

	public LexicalTokenGenerator(ICompilationUnit icu, CompilationUnit cu, IDTools tool) {
		this.icu = icu;
		this.cu = cu;
		this.tool = tool;
		this.role = tool.role_assigner.AssignRole(icu.getPath().toOSString());
	}

	@Override
	public boolean preVisit2(ASTNode node) {
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
			return false;
		}
		return super.preVisit2(node);
	}
	
}
