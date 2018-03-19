package huffman;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

public class HuffmanNode implements Comparable<HuffmanNode> {
	private Integer content = null;
	private int frequence = 0;
	// private HuffmanNode parent = null;
	// private HuffmanNode leftNode = null;
	// private HuffmanNode rightNode = null;
	private List<HuffmanNode> childrenNodes = new ArrayList<HuffmanNode>();
	private int maxDepth = 0;
	private int leafNodeNum = 0;
	private int nonLeafNodeNum = 0;
	// private int leafNodeNum = 0;
	// private int totalNodeNum = 1;

	public List<HuffmanNode> getChildren() {
		return childrenNodes;
	}

	public void appendToChildren(HuffmanNode child) {
		childrenNodes.add(child);
	}

	public void setNonLeafNodeNum(int nonLeafNodeNum) {
		this.nonLeafNodeNum = nonLeafNodeNum;
	}

	public int getNonLeafNodeNum() {
		return nonLeafNodeNum;
	}

	public boolean isLeaf() {
		boolean is_leaf = nonLeafNodeNum == 0;
		if (is_leaf) {
			Assert.isTrue(childrenNodes.size() == 0);
		}
		return is_leaf;
	}

	// public void setTotalNodeNum(int totalNodeNum) {
	// this.totalNodeNum = totalNodeNum;
	// }
	//
	// public int getTotalNodeNum() {
	// return totalNodeNum;
	// }

	@Override
	public int compareTo(HuffmanNode n) {
		return frequence - n.frequence;
	}

	// public boolean isLeaf() {
	// return leftNode == null && rightNode == null;
	// }
	//
	// public boolean isRoot() {
	// return parent == null;
	// }

	public int getFrequence() {
		return frequence;
	}

	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}

	public Integer getContent() {
		return content;
	}

	public void setContent(Integer content) {
		this.content = content;
	}

	// public HuffmanNode getParent() {
	// return parent;
	// }
	//
	// public void setParent(HuffmanNode parent) {
	// this.parent = parent;
	// }
	//
	// public HuffmanNode getLeftNode() {
	// return leftNode;
	// }
	//
	// public void setLeftNode(HuffmanNode leftNode) {
	// this.leftNode = leftNode;
	// }
	//
	// public HuffmanNode getRightNode() {
	// return rightNode;
	// }
	//
	// public void setRightNode(HuffmanNode rightNode) {
	// this.rightNode = rightNode;
	// }

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	// public int getLeafNodeNum() {
	// return leafNodeNum;
	// }
	//
	// public void setLeafNodeNum(int leafNodeNum) {
	// this.leafNodeNum = leafNodeNum;
	// }

	public void setLeafNodeNum(int i) {
		this.leafNodeNum = i;
	}

	public int getLeafNodeNum() {
		return leafNodeNum;
	}

}
