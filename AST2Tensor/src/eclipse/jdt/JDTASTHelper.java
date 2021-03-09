package eclipse.jdt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.WildcardType;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import main.MetaOfApp;
import statistic.id.IDManager;
import statistic.id.PreProcessContentHelper;
import util.StringUtil;

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
		String raw_cnt = node.toString().trim();
		if (raw_cnt.startsWith("{")) {
			raw_cnt = StringUtil.UniformEmptyBlockString(raw_cnt);
		}
//		if (node instanceof Block || node instanceof AnonymousClassDeclaration) {
//			raw_cnt = "{}";
//		}
		if (MetaOfApp.ignore_ast_type.length > 0) {
			int positon = Arrays.asList(MetaOfApp.ignore_ast_type).indexOf(node.getClass());
			if (positon > -1)
			{
				raw_cnt = MetaOfApp.ignore_ast_dft_value[positon];
			}
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
		if (node instanceof MethodInvocation || node instanceof SuperConstructorInvocation || node instanceof SuperMethodInvocation) {
			return true;
		}
		return false;
	}
	
	public static boolean IsV(boolean is_non_comp_leaf, Class<?> node_clz) {
		return is_non_comp_leaf && (JDTASTHelper.IsIDLeafNode(node_clz) || MetaOfApp.SktNotOnlyExcludeIDLeafButAllLeaf);
	}
	
	public static boolean IsNonCompLeaf(ASTNode node, boolean is_leaf) {// , String filter_substmt_cnt
		boolean is_non_comp_leaf = is_leaf && !JDTASTHelper.IsComplexLeafNode(is_leaf, node.getClass());
		if (node.getClass().equals(Block.class) || node.getClass().equals(AnonymousClassDeclaration.class)) {
//			Assert.isTrue(filter_substmt_cnt.equals("{}"));
			is_non_comp_leaf = true;
		}
		return is_non_comp_leaf;
	}
	
	public static Expression GetExprSpec(ASTNode node) {
		Assert.isTrue(IsExprSpecPattern(node));
		if (node instanceof MethodInvocation) {
			return ((MethodInvocation) node).getExpression();
		}
		if (node instanceof SuperConstructorInvocation) {
			return ((SuperConstructorInvocation) node).getExpression();
		}
		if (node instanceof SuperMethodInvocation) {
			return ((SuperMethodInvocation) node).getQualifier();
		}
		return null;
	}
	
	public static boolean IsComplexLeafNode(boolean is_leaf, Class<?> node_class) {
		if (is_leaf) {
			if (node_class.equals(BreakStatement.class) || node_class.equals(ContinueStatement.class) || node_class.equals(ReturnStatement.class) || node_class.equals(SwitchCase.class) || node_class.equals(EmptyStatement.class) || node_class.equals(ConstructorInvocation.class) || node_class.equals(SuperConstructorInvocation.class)) {
				return true;
			} else {
				Assert.isTrue(IsIDLeafNode(node_class) || node_class.equals(PrimitiveType.class) || node_class.equals(Modifier.class) || node_class.equals(NullLiteral.class) || node_class.equals(BooleanLiteral.class) || node_class.equals(ThisExpression.class) || node_class.equals(Dimension.class) || node_class.equals(Block.class) || node_class.equals(AnonymousClassDeclaration.class) || node_class.equals(WildcardType.class) || node_class.equals(ArrayInitializer.class), "Strange Leaf Class:" + node_class);
			}
		}
		return false;
	}
	
	public static boolean IsIDLeafNode(Class<?> node_class) {
		if (node_class.equals(SimpleName.class) || node_class.equals(StringLiteral.class) || node_class.equals(CharacterLiteral.class)
				|| node_class.equals(NumberLiteral.class)) {
			return true;
		}
		return false;
	}
	
	public static boolean IsPassThroughNode(ASTNode node) {
		ArrayList<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		if (children.size() == 1 && children.get(0).toString().equals(node.toString())) {
			return true;
		}
		return false;
	}
	
	public static ASTNode SkipPassThroughNodes(ASTNode node) {
		ArrayList<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		if (children.size() == 1 && children.get(0).toString().equals(node.toString())) {
			return SkipPassThroughNodes(children.get(0));
		}
		return node;
	}
	
}
