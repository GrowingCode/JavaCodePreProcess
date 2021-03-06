package tree;

import org.eclipse.jdt.core.dom.IBinding;

public class ExprSpecTreeNode extends TreeNode {
	
	boolean has_expression = false;
	
	public ExprSpecTreeNode(Class<?> clazz, boolean is_non_comp_leaf, IBinding bind, String content, String tree_whole_content, boolean has_expression) {//, int sib_index
		super(clazz, is_non_comp_leaf, bind, content, tree_whole_content);//, sib_index
		this.has_expression = has_expression;
	}
	
	public boolean HasExpression() {
		return has_expression;
	}
	
}
