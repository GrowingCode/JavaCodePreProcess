package eclipse.jdt;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;

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
			PostfixExpression pe = (PostfixExpression)node;
			org.eclipse.jdt.core.dom.PostfixExpression.Operator op = pe.getOperator();
			type_extra += op.toString();
		}
		if (node instanceof PrefixExpression) {
			PrefixExpression pe = (PrefixExpression)node;
			org.eclipse.jdt.core.dom.PrefixExpression.Operator op = pe.getOperator();
			type_extra += op.toString();
		}
		if (node instanceof InfixExpression) {
			InfixExpression pe = (InfixExpression)node;
			org.eclipse.jdt.core.dom.InfixExpression.Operator op = pe.getOperator();
			type_extra += op.toString();
		}
		String represent = type + type_extra + (children_size == 0 ? IDManager.Leaf : IDManager.NonLeaf);
		return represent;
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

}