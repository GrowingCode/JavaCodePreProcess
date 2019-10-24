package statistic;

import org.eclipse.jdt.core.ICompilationUnit;

import translation.roles.RoleAssigner;
import translation.tensor.StringTensor;
import tree.TreeNode;
import tree.TreeVisitor;

public class IDGenerator extends TreeVisitor {

//	ICompilationUnit icu = null;
//	CompilationUnit cu = null;
	IDTools tool = null;

	int role = -1;

	public IDGenerator(IDTools tool, ICompilationUnit icu) {// CompilationUnit cu, IDTools tool
		super(null);
//		this.icu = icu;
//		this.cu = cu;
		this.tool = tool;
		this.role = tool.role_assigner.AssignRole(icu.getPath().toOSString());
//		System.out.println(icu.getPath().toOSString() + "#role:" + role);
//		icu.getElementName()
//		System.out.println("icu.getPath().toOSString():" + icu.getPath().toOSString());
	}

//	@Override
//	public void preVisit(ASTNode node) {
//		super.preVisit(node);
//		// handle token
//		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
////		if (node instanceof BreakStatement && children.size() == 0) {
////			System.out.println("============ encounter BreakStatementL ============");
////		}
//		int children_size = children == null ? 0 : children.size();
//		// handle GrammarRecorder
//		tool.gr.RecordGrammar(node);
//		if (children_size == 0) {
//			tool.gr.RecordExtraGrammarForLeaf(node, null);
//		}
//		// handle TokenRecorder
//		String type_content = JDTASTHelper.GetTypeRepresentationForASTNode(node);
//		if (role <= RoleAssigner.train_seen_k) {
//			tool.tr.TokenHitInTrainSet(type_content, 1);
//		} else {
//			tool.tr.TokenNotHitInTrainSet(type_content, 1);
//		}
//		if (children_size == 0) {
//			String token_str = JDTASTHelper.GetContentRepresentationForASTNode(node);
//			if (role <= RoleAssigner.train_seen_k) {
//				tool.tr.TokenHitInTrainSet(token_str, 1);
//			} else {
//				tool.tr.TokenNotHitInTrainSet(token_str, 1);
//			}
//		}
//		// handle APIRecorder
//		if (node instanceof SimpleName) {
//			SimpleName sn = (SimpleName) node;
//			IBinding ib = sn.resolveBinding();
//			if (ib != null && ib instanceof IMethodBinding) {
//				if (!(sn.getParent() instanceof MethodDeclaration) && (sn.getParent() instanceof MethodInvocation)) {
////					Assert.isTrue(sn.getParent() instanceof MethodInvocation);
////					System.out.println("sn is method declared name!");
//					IMethodBinding imb = (IMethodBinding) ib;
//					ITypeBinding dc = imb.getDeclaringClass();
//					IMethodBinding[] mds = dc.getDeclaredMethods();
////					System.out.println("==== start print md ====");
//					LinkedList<String> mdnames = new LinkedList<String>();
//					for (IMethodBinding md : mds) {
////						System.out.println(md);
//						String mdname = md.getName();
//						mdname = PreProcessContentHelper.PreProcessTypeContent(mdname);
//						if (!mdnames.contains(mdname)) {
//							mdnames.add(mdname);
//						}
//						tool.gr.RecordExtraGrammarForLeaf(node, mdname);
//						if (role <= RoleAssigner.train_seen_k) {
//							tool.tr.TokenHitInTrainSet(mdname, 0);
//						} else {
//							tool.tr.TokenHitInTrainSet(mdname, 0);
//						}
//					}
//					String joined = String.join("#", mdnames);
//					tool.ar.RecordAPIComb(joined);
////					System.out.println("mds:" + joined);
////					System.out.println("==== end print md ====");
//				}
//			}
//		}
//		if (MetaOfApp.ClassLevelTensorGeneration) {
//			if (node.getParent() != null) {
//				tool.cnc.EncounterChildrenNum(children_size+2);
//			}
//		} else {
//			if (node.getParent() != null && node.getParent().getParent() != null) {
//				tool.cnc.EncounterChildrenNum(children_size+2);
//			}
//		}
//	}

	@Override
	public boolean PreVisit(TreeNode node) {
//		tool.gr.RecordGrammar(node);
		// handle TokenRecorder
		if (role <= RoleAssigner.train_seen_k) {
			tool.tr.TokenHitInTrainSet(node.GetContent(), 1);
		} else {
			tool.tr.TokenNotHitInTrainSet(node.GetContent(), 1);
		}
		return true;
	}

	@Override
	public void PostVisit(TreeNode node) {
	}

	@Override
	public StringTensor GetStringTensor() {
		return null;
	}

	@Override
	public void Clear() {
	}

}
