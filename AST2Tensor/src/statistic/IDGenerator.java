package statistic;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

import logger.DebugLogger;
import statistic.id.IDCounter;
import statistic.id.IDManager;
import util.SystemUtil;

public class IDGenerator extends ASTVisitor {

	IJavaProject java_project = null;
	ICompilationUnit icu = null;
	CompilationUnit cu = null;
	IDCounter ic = null;

	public IDGenerator(IJavaProject java_project, ICompilationUnit icu, CompilationUnit cu, IDCounter ic) {
		this.java_project = java_project;
		this.icu = icu;
		this.cu = cu;
		this.ic = ic;
	}

	@Override
	public boolean preVisit2(ASTNode node) {
		if (!IsLeafNode(node)) {
			ic.EncounterANode(IDManager.PrimordialNonLeafASTType, node.getClass().getSimpleName());
		}
		return super.preVisit2(node);
	}

	private boolean IsLeafNode(ASTNode node) {
		if (node instanceof SimpleName || node instanceof NumberLiteral || node instanceof CharacterLiteral
				|| node instanceof NullLiteral || node instanceof StringLiteral) {
			return true;
		}
		return false;
	}

	@Override
	public boolean visit(SimpleName node) {
		if (node.getParent() != null) {
			ASTNode parent = node.getParent();
			String type = parent.getClass().getSimpleName();
			String content = node.getIdentifier();
			ic.EncounterANode(type, content);
		} else {
			DebugLogger.Error("this should not happen: SimpleName has no parent ASTNode!!!");
			SystemUtil.Delay(10000);
			String type = node.getClass().getSimpleName();
			String content = node.getIdentifier();
			ic.EncounterANode(type, content);
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(NumberLiteral node) {
		String type = node.getClass().getSimpleName();
		String content = node.getToken();
		ic.EncounterANode(type, content);
		return super.visit(node);
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		String type = node.getClass().getSimpleName();
		String content = node.getEscapedValue();
		ic.EncounterANode(type, content);
		return super.visit(node);
	}

	@Override
	public boolean visit(StringLiteral node) {
		String type = node.getClass().getSimpleName();
		String content = node.getEscapedValue();
		ic.EncounterANode(type, content);
		return super.visit(node);
	}

	@Override
	public boolean visit(NullLiteral node) {
		String type = node.getClass().getSimpleName();
		String content = node.toString();
		ic.EncounterANode(type, content);
		return super.visit(node);
	}

}
