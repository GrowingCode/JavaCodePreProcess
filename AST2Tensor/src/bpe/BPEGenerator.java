package bpe;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.IDTools;
import statistic.id.PreProcessContentHelper;

public class BPEGenerator extends ASTVisitor {

	ICompilationUnit icu = null;
	CompilationUnit cu = null;
	IDTools tool = null;
	
	int role = -1;

	public BPEGenerator(ICompilationUnit icu, CompilationUnit cu, IDTools tool) {
		this.icu = icu;
		this.cu = cu;
		this.tool = tool;
//		this.role = tool.role_assigner.AssignRole(icu.getElementName());
//		System.out.println("icu.getElementName():" + icu.getElementName());
	}

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		// handle token
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		int children_size = children == null ? 0 : children.size();
		String type_content = JDTASTHelper.GetTypeRepresentationForASTNode(node);
		tool.bpe_mr.EncounterToken(type_content, 1);
		String token_str = null;
		if (children_size == 0) {
			token_str = JDTASTHelper.GetContentRepresentationForASTNode(node);
			tool.bpe_mr.EncounterToken(token_str, 1);
		}
		// handle APIRecorder
		if (node instanceof SimpleName) {
			Assert.isTrue(token_str != null);
			SimpleName sn = (SimpleName) node;
			IBinding ib = sn.resolveBinding();
			if (ib != null && ib instanceof IMethodBinding) {
				if (!(sn.getParent() instanceof MethodDeclaration)) {
					Assert.isTrue(sn.getParent() instanceof MethodInvocation);
//					System.out.println("sn is method declared name!");
					IMethodBinding imb = (IMethodBinding) ib;
					ITypeBinding dc = imb.getDeclaringClass();
					IMethodBinding[] mds = dc.getDeclaredMethods();
//					System.out.println("==== start print md ====");
//					LinkedList<String> mdnames = new LinkedList<String>();
					for (IMethodBinding md : mds) {
//						System.out.println(md);
						String mdname = md.getName();
						mdname = PreProcessContentHelper.PreProcessTypeContent(mdname);
						if (!mdname.equals(token_str)) {
							tool.bpe_mr.EncounterToken(token_str, 1);
						}
					}
				}
			}
		}
	}
	
}
