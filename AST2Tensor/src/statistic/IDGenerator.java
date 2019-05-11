package statistic;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import main.MetaOfApp;

public class IDGenerator extends ASTVisitor {

	ICompilationUnit icu = null;
	CompilationUnit cu = null;
	IDTools tool = null;

	public IDGenerator(ICompilationUnit icu, CompilationUnit cu, IDTools tool) {
		this.icu = icu;
		this.cu = cu;
		this.tool = tool;
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
		tool.ic.EncounterANode(JDTASTHelper.GetRepresentationForASTNode(node), children_size == 0);
		/**
		 * handle api and all its relevant apis
		 */
		if (node instanceof SimpleName) {
			SimpleName sn = (SimpleName) node;
			IBinding ib = sn.resolveBinding();
			if (ib != null && ib instanceof IMethodBinding) {
				if (!(sn.getParent() instanceof MethodDeclaration)) {
//					System.out.println("sn is method declared name!");
					IMethodBinding imb = (IMethodBinding) ib;
					ITypeBinding dc = imb.getDeclaringClass();
					IMethodBinding[] mds = dc.getDeclaredMethods();
//					System.out.println("==== start print md ====");
					LinkedList<String> mdnames = new LinkedList<String>();
					for (IMethodBinding md : mds) {
//						System.out.println(md);
						if (!mdnames.contains(md.getName())) {
							mdnames.add(md.getName());
						}
					}
					String joined = String.join("#", mdnames);
					System.out.println("mds:" + joined);
//					System.out.println("==== end print md ====");
				}
			}
		}
		if (MetaOfApp.ClassLevelTensorGeneration) {
			if (node.getParent() != null) {
				tool.cnc.EncounterChildrenNum(children_size+2);
			}
		} else {
			if (node.getParent() != null && node.getParent().getParent() != null) {
				tool.cnc.EncounterChildrenNum(children_size+2);
			}
		}
	}
	
}
