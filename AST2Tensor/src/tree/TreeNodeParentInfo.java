package tree;

public class TreeNodeParentInfo {
	
	public static final int h_type = 0;
	public static final int v_type = 1;
	
	public String tree_node_content = null;
	public int index; // relative to h or v;
	public int type = 0;// 0: h; 1: v;
	
	public TreeNodeParentInfo(String tree_node_content, int index, int type) {
		this.tree_node_content = tree_node_content;
		this.index = index;
		this.type = type;
	}
	
}
