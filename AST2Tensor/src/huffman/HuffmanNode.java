package huffman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import main.Meta;

public class HuffmanNode implements Comparable<HuffmanNode> {
	private Integer content = null;
	private int frequence = 0;
//	private HuffmanNode parent = null;
//	private HuffmanNode leftNode = null;
//	private HuffmanNode rightNode = null;
	private List<HuffmanNode> childrenNodes = new ArrayList<HuffmanNode>();
	private int maxDepth = 0;
	private int leafNodeNum = 0;
	private int nonLeafNodeNum = 0;
//	private int leafNodeNum = 0;
//	private int totalNodeNum = 1;
	
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
		return nonLeafNodeNum == 0;
	}
	
//	public void setTotalNodeNum(int totalNodeNum) {
//		this.totalNodeNum = totalNodeNum;
//	}
//	
//	public int getTotalNodeNum() {
//		return totalNodeNum;
//	}

	@Override
	public int compareTo(HuffmanNode n) {
		return frequence - n.frequence;
	}

//	public boolean isLeaf() {
//		return leftNode == null && rightNode == null;
//	}
//
//	public boolean isRoot() {
//		return parent == null;
//	}

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

//	public HuffmanNode getParent() {
//		return parent;
//	}
//
//	public void setParent(HuffmanNode parent) {
//		this.parent = parent;
//	}
//
//	public HuffmanNode getLeftNode() {
//		return leftNode;
//	}
//
//	public void setLeftNode(HuffmanNode leftNode) {
//		this.leftNode = leftNode;
//	}
//
//	public HuffmanNode getRightNode() {
//		return rightNode;
//	}
//
//	public void setRightNode(HuffmanNode rightNode) {
//		this.rightNode = rightNode;
//	}
	
	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
//	public int getLeafNodeNum() {
//		return leafNodeNum;
//	}
//
//	public void setLeafNodeNum(int leafNodeNum) {
//		this.leafNodeNum = leafNodeNum;
//	}
	
	// ArrayList<Integer> node, ArrayList<Integer> left_child, ArrayList<Integer> right_child
	public int[][][] ToTensor() {
//		int[][] tensor = new int[3][totalNodeNum];
//		Arrays.fill(tensor[0], -1);
//		Arrays.fill(tensor[1], -1);
//		Arrays.fill(tensor[2], -1);
		int[][][] tensor = new int[nonLeafNodeNum][2][Meta.HuffTreeStandardChildrenNum];
		for (int i=0;i<nonLeafNodeNum;i++) {
			for (int j=0;j<2;j++) {
				Arrays.fill(tensor[i][j], -1);
			}
		}
		IDAssigner ida = new IDAssigner();
		ToTensor(tensor, ida);
		return tensor;
	}
	
	private int ToTensor(int[][][] tensor, IDAssigner ida) {
		int id = ida.GetNewID();
		Iterator<HuffmanNode> citr = childrenNodes.iterator();
		int child_index = 0;
		while (citr.hasNext()) {
			HuffmanNode child = citr.next();
			int infix_child_id = child.ToTensor(tensor, ida);
			tensor[id][0][child_index] = infix_child_id;
			tensor[id][1][child_index] = child.isLeaf() ? 1 : 0;
			child_index++;
		}
//		int infix_left_id = -1;
//		if (leftNode != null) {
//			infix_left_id = leftNode.ToTensor(tensor, ida);
//		}
//		int infix_right_id = -1;
//		if (rightNode != null) {
//			infix_right_id = rightNode.ToTensor(tensor, ida);
//		}
//		tensor[0][id] = infix_left_id;
//		tensor[1][id] = infix_right_id;
//		tensor[2][id] = (content == null ? -1 : content);
		return id;
	}

	public void setLeafNodeNum(int i) {
		this.leafNodeNum = i;
	}
	
	public int getLeafNodeNum() {
		return leafNodeNum;
	}
	
}
