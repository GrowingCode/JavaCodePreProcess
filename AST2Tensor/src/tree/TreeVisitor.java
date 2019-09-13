package tree;

public abstract class TreeVisitor {
	
	public abstract boolean PreVisit(TreeNode node);
	public abstract void PostVisit(TreeNode node);
	
}
