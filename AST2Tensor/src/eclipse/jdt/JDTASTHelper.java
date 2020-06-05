package eclipse.jdt;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import statistic.id.PreProcessContentHelper;

public class JDTASTHelper {

//	public static AST CreateEmptyASTWithRewriteRecorded() {
//		ASTParser parser = ASTParser.newParser(AST.JLS10);
//		parser.setKind(ASTParser.K_COMPILATION_UNIT);
//		parser.setSource("".toCharArray());
//		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
//		compilationUnit.recordModifications();
//		AST ast = compilationUnit.getAST();
//		return ast;
//	}
//	
//	public static void RetainOnlyRoot(ASTNode root) {
////		root.setStructuralProperty(property, value);
//		System.out.println("root:" + root);
//		ArrayList<ASTNode> childs = JDTSearchForChildrenOfASTNode.GetChildren(root);
//		for (ASTNode child : childs) {
//			child.delete();
//		}
//	}

	public static String GetTypeRepresentationForASTNode(ASTNode node) {
		String type = node.getClass().getSimpleName();
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		int children_size = children == null ? 0 : children.size();
		String type_extra = "";
		if (node instanceof PostfixExpression) {
			PostfixExpression pe = (PostfixExpression) node;
			org.eclipse.jdt.core.dom.PostfixExpression.Operator op = pe.getOperator();
			type_extra += op.toString();
		}
		if (node instanceof PrefixExpression) {
			PrefixExpression pe = (PrefixExpression) node;
			org.eclipse.jdt.core.dom.PrefixExpression.Operator op = pe.getOperator();
			type_extra += op.toString();
		}
		if (node instanceof InfixExpression) {
			InfixExpression pe = (InfixExpression) node;
			org.eclipse.jdt.core.dom.InfixExpression.Operator op = pe.getOperator();
			type_extra += op.toString();
		}
		String represent = type + type_extra + (children_size == 0 ? IDManager.Leaf : IDManager.NonLeaf);
		return PreProcessContentHelper.PreProcessTypeContent(represent);
	}

	public static String GetContentRepresentationForASTNode(ASTNode node) {
		Assert.isTrue(JDTSearchForChildrenOfASTNode.GetChildren(node).isEmpty());
		String raw_cnt = node.toString();
		if (node instanceof Block || node instanceof AnonymousClassDeclaration) {
			raw_cnt = "{}";
		}
//		System.err.println("raw_cnt:" + raw_cnt);
		String cnt = PreProcessContentHelper.PreProcessTypeContent(raw_cnt);
//		System.err.println("cnt:" + cnt);
		return cnt;
	}

//	public static String GetRepresentationForASTNode(ASTNode node) {
//		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
//		int children_size = children == null ? 0 : children.size();
//		String pre = GetTypeRepresentationForASTNode(node);
//		String post = "";
//		if (children_size > 0) {
//			post = IDManager.DefaultPart;
//		} else {
//			post = node.toString().trim();
//		}
//		String represent = pre + "#" + post;
//		return represent;
//	}
	
	public static String GetSimplifiedSignatureForMethodDeclaration(ASTNode node) {
		Assert.isTrue(node instanceof MethodDeclaration);
		MethodDeclaration md = (MethodDeclaration) node;
		return md.getName().toString();
	}
	
	public static boolean IsExprSpecPattern(ASTNode node) {
		if (node instanceof MethodInvocation || node instanceof SuperConstructorInvocation) {
			return true;
		}
		return false;
	}
	
	public static Expression GetExprSpec(ASTNode node) {
		Assert.isTrue(IsExprSpecPattern(node));
		if (node instanceof MethodInvocation) {
			return ((MethodInvocation) node).getExpression();
		}
		if (node instanceof SuperConstructorInvocation) {
			return ((SuperConstructorInvocation) node).getExpression();
		}
		return null;
	}

}
