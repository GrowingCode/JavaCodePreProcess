package huffman;

import java.util.Arrays;

public class HuffmanNode implements Comparable<HuffmanNode> {
	private Integer content = null;
	private int frequence = 0;
	private HuffmanNode parent = null;
	private HuffmanNode leftNode = null;
	private HuffmanNode rightNode = null;
	private int maxDepth = 0;
	private int leafNodeNum = 0;
	private int totalNodeNum = 1;
	
	public void setTotalNodeNum(int totalNodeNum) {
		this.totalNodeNum = totalNodeNum;
	}
	
	public int getTotalNodeNum() {
		return totalNodeNum;
	}

	@Override
	public int compareTo(HuffmanNode n) {
		return frequence - n.frequence;
	}

	public boolean isLeaf() {
		return leftNode == null && rightNode == null;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeftChild() {
		return parent != null && this == parent.leftNode;
	}
	
	public boolean hasLeftChild() {
		return leftNode != null;
	}
	
	public boolean hasRightChild() {
		return rightNode != null;
	}

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
	
	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
	public int getLeafNodeNum() {
		return leafNodeNum;
	}

	public void setLeafNodeNum(int leafNodeNum) {
		this.leafNodeNum = leafNodeNum;
	}
	
	// ArrayList<Integer> node, ArrayList<Integer> left_child, ArrayList<Integer> right_child
	public int[][] ToTensor() {
		int[][] tensor = new int[3][totalNodeNum];
		Arrays.fill(tensor, -1);
		IDAssigner ida = new IDAssigner();
		ToTensor(tensor, ida);
		return tensor;
	}
	
	private int ToTensor(int[][] tensor, IDAssigner ida) {
		int id = ida.GetNewID();
		int infix_right_id = rightNode.ToTensor(tensor, ida);
		int infix_left_id = leftNode.ToTensor(tensor, ida);
		tensor[0][id] = infix_right_id;
		tensor[1][id] = infix_left_id;
		tensor[2][id] = (content == null ? -1 : content);
		return id;
	}
	
}

class IDAssigner {
	
	int id = -1;
	
	public IDAssigner() {
	}
	
	public int GetNewID() {
		id++;
		return id;
	}
	
}