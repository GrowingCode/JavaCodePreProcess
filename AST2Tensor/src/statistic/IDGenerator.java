package statistic;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import main.MetaOfApp;
import statistic.ast.ChildrenNumCounter;
import statistic.id.IDCounter;

public class IDGenerator extends ASTVisitor {

	ICompilationUnit icu = null;
	CompilationUnit cu = null;
	IDCounter ic = null;
	ChildrenNumCounter cnc = null;

	public IDGenerator(ICompilationUnit icu, CompilationUnit cu, IDCounter ic, ChildrenNumCounter cnc) {
		this.icu = icu;
		this.cu = cu;
		this.ic = ic;
		this.cnc = cnc;
	}

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		int children_size = children == null ? 0 : children.size();
//		if (children_size > 0) {
//			ic.EncounterANode(node.getClass().getSimpleName() + "#" + IDManager.DefaultPart, false);
//		} else {
//			ic.EncounterANode(node.getClass().getSimpleName() + "#" + node.toString().trim(), true);
//		}
		ic.EncounterANode(JDTASTHelper.GetRepresentationForASTNode(node), children_size == 0);
		if (MetaOfApp.ClassLevelTensorGeneration) {
			if (node.getParent() != null) {
				cnc.EncounterChildrenNum(children_size+2);
			}
		} else {
			if (node.getParent() != null && node.getParent().getParent() != null) {
				cnc.EncounterChildrenNum(children_size+2);
			}
		}
	}
	
}
