package eclipse.search;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

public class JDTSearchForChildrenOfASTNode {

	@SuppressWarnings("unchecked")
	public static ArrayList<ASTNode> GetChildren(ASTNode node) {
		ArrayList<ASTNode> children = new ArrayList<ASTNode>();
		List<StructuralPropertyDescriptor> list = node.structuralPropertiesForType();
		for (int i = 0; i < list.size(); i++) {
			StructuralPropertyDescriptor curr = (StructuralPropertyDescriptor) list.get(i);
			Object child = node.getStructuralProperty(curr);
			if (child instanceof List) {
				List<ASTNode> child_nodes = (List<ASTNode>) child;
				children.addAll(child_nodes);
			} else if (child instanceof ASTNode) {
				// return new ASTNode[] { (ASTNode) child };
				children.add((ASTNode) child);
			}
		}
		return children;
	}

}
