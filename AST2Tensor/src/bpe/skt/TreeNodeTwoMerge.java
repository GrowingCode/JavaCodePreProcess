package bpe.skt;

public class TreeNodeTwoMerge implements Comparable<TreeNodeTwoMerge> {
	
	String node = null;
	String parent = null;
	String merged = null;
	
	public TreeNodeTwoMerge(String node, String parent, String merged) {
		this.node = node;
		this.parent = parent;
		this.merged = merged;
	}
	
	public String GetNode() {
		return node;
	}
	
	public String GetParent() {
		return parent;
	}
	
	public String GetMerged() {
		return merged;
	}
	
	@Override
	public String toString() {
		return merged;
	}

	@Override
	public int compareTo(TreeNodeTwoMerge o) {
		return toString().compareTo(o.toString());
	}
	
}
