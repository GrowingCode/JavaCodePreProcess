package tree;

public class TreeVisit {
	
	public static void Visit(TreeNode root, TreeVisitor visitor) {
		boolean ctn = visitor.PreVisit(root);
		if (ctn) {
			for (TreeNode child: root.children) {
				Visit(child, visitor);
			}
		}
		visitor.PostVisit(root);
	}
	
}
