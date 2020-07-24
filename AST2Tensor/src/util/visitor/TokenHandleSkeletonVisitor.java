package util.visitor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;

import eclipse.bind.BindingResolveUtil;
import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import translation.tensor.util.TokenKindUtil;

public class TokenHandleSkeletonVisitor extends SkeletonVisitor {

	public TokenHandleSkeletonVisitor(ICompilationUnit icu) {
		super(icu);
	}
	
	@Override
	protected Range HandleNonStatement(ASTNode node) {
		Range r = null;
//		if (node instanceof SimpleName || node instanceof StringLiteral || node instanceof CharacterLiteral
//				|| node instanceof NumberLiteral) {
		if (JDTASTHelper.IsIDLeafNode(node.getClass())) {
//			SimpleType or TypeLiteral are not leaf node
//			if (node.toString().contains("Maven")) {
//				System.out.println("cared node:" + node + "#node_type:" + node.getClass());
//			}
			Assert.isTrue(JDTSearchForChildrenOfASTNode.GetChildren(node).size() == 0, "#node_type:" + node.getClass() + "node_content:" + node.toString());
			String cnt = node.toString();
//			if (node instanceof TypeLiteral) {
//				TypeLiteral tl = (TypeLiteral) node;
//				cnt = tl.getType().toString();
//			}
			ElementInfo ei = record.get(cnt);
			if (ei == null) {
				int index = record.size();
				int is_var = 0;
				if (BindingResolveUtil.ResolveVariableBinding(node) != null) {
					is_var = 1;
				}
				int kind = TokenKindUtil.GetTokenKind(node);
				ei = new ElementInfo(index, cnt, is_var, kind);
				content.put(ei, cnt);
				record.put(cnt, ei);
			}
			r = new Range(ei, node.getStartPosition(), node.getStartPosition() + node.getLength() - 1,
					OperationKind.replace);
		}
		return r;
	}
	
}
