package bpe.skt;

import tree.util.TreeNodePairUtil;

public class TreeNodeTwoMerge implements Comparable<TreeNodeTwoMerge> {
	
	String node = null;
	int node_index = 0;
	String parent = null;
	String merged = null;
	
	public TreeNodeTwoMerge(String node, int node_index, String parent, String merged) {
		this.node = node;
		this.node_index = node_index;
		this.parent = parent;
		this.merged = merged;
	}
	
	public String GetNode() {
		return node;
	}
	
	public int GetNodeIndex() {
		return node_index;
	}
	
	public String GetParent() {
		return parent;
	}
	
	public String GetMerged() {
		return merged;
	}
	
	public String GetParentChildPairPresentation() {
		return TreeNodePairUtil.GetParentChildPairPresentation(parent, node);
	}
	
	@Override
	public String toString() {
		return "merged:" + merged + "====node:" + node + "====node_index:" + node_index + "====parent:" + parent;
	}

	@Override
	public int compareTo(TreeNodeTwoMerge o) {
		return toString().compareTo(o.toString());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return toString().equals(obj.toString());
//		return super.equals(obj);
	}
	
}
