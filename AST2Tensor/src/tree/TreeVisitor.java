package tree;

import translation.tensor.StringTensor;

public abstract class TreeVisitor {
	
	public abstract boolean PreVisit(TreeNode node);
	public abstract void PostVisit(TreeNode node);
	public abstract StringTensor GetStringTensor();
	
}
