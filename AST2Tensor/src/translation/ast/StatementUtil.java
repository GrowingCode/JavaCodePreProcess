package translation.ast;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class StatementUtil {

	public static boolean IsStatement(Class<?> clazz) {// node.GetClazz()
		if (Statement.class.isAssignableFrom(clazz) && !(Block.class.isAssignableFrom(clazz))) {
			return true;
		}
		return false;
	}

	public static boolean IsMethodDeclaration(Class<?> clazz) {// node.GetClazz()
		if (MethodDeclaration.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}

//	public static ArrayList<String> ProcessTokenHandleSkeleton(ICompilationUnit icu, ASTNode node) {
//		TokenHandleSkeletonVisitor sv = new TokenHandleSkeletonVisitor(icu);
//		node.accept(sv);
//		ArrayList<String> result = sv.GetResult();
////		Assert.isTrue(result.size() >= 2);
////		PrintUtil.PrintList(result, "skeleton of statement:" + node);
//		return result;
//	}

}
