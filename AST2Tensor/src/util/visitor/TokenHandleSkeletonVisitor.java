package util.visitor;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

public class TokenHandleSkeletonVisitor extends SkeletonVisitor {

	public TokenHandleSkeletonVisitor(ICompilationUnit icu) {
		super(icu);
	}
	
	@Override
	protected Range HandleNonStatement(ASTNode node) {
		Range r = null;
		if (node instanceof SimpleName || node instanceof StringLiteral || node instanceof CharacterLiteral
				|| node instanceof NumberLiteral) {//  || node instanceof TypeLiteral
//			if (node.toString().contains("Maven")) {
//				System.out.println("cared node:" + node + "#node_type:" + node.getClass());
//			}
			String cnt = node.toString();
//			if (node instanceof TypeLiteral) {
//				TypeLiteral tl = (TypeLiteral) node;
//				cnt = tl.getType().toString();
//			}
			ElementInfo ei = record.get(cnt);
			if (ei == null) {
				int index = record.size();
				ei = new ElementInfo(index, cnt);
				content.put(ei, cnt);
				record.put(cnt, ei);
			}
			r = new Range(ei, node.getStartPosition(), node.getStartPosition() + node.getLength() - 1,
					OperationKind.replace);
		}
		return r;
	}

}
