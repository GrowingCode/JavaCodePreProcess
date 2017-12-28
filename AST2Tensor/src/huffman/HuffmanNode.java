package huffman;

import java.util.ArrayList;

public class HuffmanNode implements Comparable<HuffmanNode> {
	private String chars = null;
	private int frequence = 0;
	private HuffmanNode parent = null;
	private HuffmanNode leftNode = null;
	private HuffmanNode rightNode = null;

	@Override
	public int compareTo(HuffmanNode n) {
		return frequence - n.frequence;
	}

	public boolean isLeaf() {
		return chars.length() == 1;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeftChild() {
		return parent != null && this == parent.leftNode;
	}

	public int getFrequence() {
		return frequence;
	}

	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}

	public String getContent() {
		return chars;
	}

	public void setContent(String chars) {
		this.chars = chars;
	}

	public HuffmanNode getParent() {
		return parent;
	}

	public void setParent(HuffmanNode parent) {
		this.parent = parent;
	}

	public HuffmanNode getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(HuffmanNode leftNode) {
		this.leftNode = leftNode;
	}

	public HuffmanNode getRightNode() {
		return rightNode;
	}

	public void setRightNode(HuffmanNode rightNode) {
		this.rightNode = rightNode;
	}
	
	public void ToTensor(ArrayList<Integer> node, ArrayList<Integer> left_child, ArrayList<Integer> right_child) {
		node.add();
	}
	
}