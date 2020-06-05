package tree;

import org.eclipse.jdt.core.dom.IBinding;

public class ExprSpecTreeNode extends TreeNode {
	
	boolean has_expression = false;
	
	public ExprSpecTreeNode(Class<?> clazz, IBinding bind, String content, String tree_whole_content, boolean has_expression) {
		super(clazz, bind, content, tree_whole_content);
		this.has_expression = has_expression;
	}
	
	public boolean HasExpression() {
		return has_expression;
	}
	
}
