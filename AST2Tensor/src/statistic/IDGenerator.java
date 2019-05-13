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
import statistic.id.PreProcessContentHelper;
import translation.roles.RoleAssigner;

public class IDGenerator extends ASTVisitor {

	ICompilationUnit icu = null;
	CompilationUnit cu = null;
	IDTools tool = null;
	
	int role = -1;

	public IDGenerator(ICompilationUnit icu, CompilationUnit cu, IDTools tool) {
		this.icu = icu;
		this.cu = cu;
		this.tool = tool;
		this.role = tool.role_assigner.AssignRole(icu.getElementName());
		System.out.println("icu.getElementName():" + icu.getElementName());
	}

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		// handle grammar
		tool.gr.RecordGrammar(node);
		// handle token
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		int children_size = children == null ? 0 : children.size();
		String type_content = JDTASTHelper.GetTypeRepresentationForASTNode(node);
		if (role <= RoleAssigner.train_seen_k) {
			tool.tr.TokenHitInTrainSet(type_content);
		} else {
			tool.tr.TokenNotHitInTrainSet(type_content);
		}
		if (children_size == 0) {
			String token_str = node.toString();
			if (role <= RoleAssigner.train_seen_k) {
				tool.tr.TokenHitInTrainSet(token_str);
			} else {
				tool.tr.TokenNotHitInTrainSet(token_str);
			}
		}
		// handle api
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
						String mdname = md.getName();
						mdname = PreProcessContentHelper.PreProcessTypeContent(mdname);
						if (!mdnames.contains(mdname)) {
							mdnames.add(mdname);
						}
					}
					String joined = String.join("#", mdnames);
					tool.ar.RecordAPIComb(joined);
					if (role <= RoleAssigner.train_seen_k) {
						for (String md : mdnames) {
							tool.tr.TokenHitInTrainSet(md);
						}
					} else {
						for (String md : mdnames) {
							tool.tr.TokenHitInTrainSet(md);
						}
//						tool.ar.APINotHitInTrainSet(joined);
					}
//					System.out.println("mds:" + joined);
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
