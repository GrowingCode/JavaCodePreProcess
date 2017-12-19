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

import statistic.id.IDManager;

public class IDGenerator extends ASTVisitor {
	
	IJavaProject java_project = null;
	ICompilationUnit icu = null;
	CompilationUnit cu = null;
	IDManager im = null;
	
	public IDGenerator(IJavaProject java_project, ICompilationUnit icu, CompilationUnit cu, IDManager im) {
		this.java_project = java_project;
		this.icu = icu;
		this.cu = cu;
		this.im = im;
	}
	
	@Override
	public boolean preVisit2(ASTNode node) {
		if (!IsLeafNode(node)) {
			im.EncounterANode(IDManager.PrimordialNonLeafASTType, node.getClass().getSimpleName());
		}
		return super.preVisit2(node);
	}
	
	private boolean IsLeafNode(ASTNode node) {
		if (node instanceof SimpleName || node instanceof NumberLiteral || node instanceof CharacterLiteral || node instanceof NullLiteral || node instanceof StringLiteral) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean visit(SimpleName node) {
		String type = node.getClass().getSimpleName();
		String content = node.getIdentifier();
		im.EncounterANode(type, content);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NumberLiteral node) {
		String type = node.getClass().getSimpleName();
		String content = node.getToken();
		im.EncounterANode(type, content);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(CharacterLiteral node) {
		String type = node.getClass().getSimpleName();
		String content = node.getEscapedValue();
		im.EncounterANode(type, content);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(StringLiteral node) {
		String type = node.getClass().getSimpleName();
		String content = node.getEscapedValue();
		im.EncounterANode(type, content);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NullLiteral node) {
		String type = node.getClass().getSimpleName();
		String content = node.toString();
		im.EncounterANode(type, content);
		return super.visit(node);
	}
	
}
