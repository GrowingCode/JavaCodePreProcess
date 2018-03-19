package huffman;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.core.runtime.Assert;

public class GenerateHuffmanTreeTensor {

	int standard_children_num = -1;
	int maximum_children_num = 0;
	int minimum_children_num = -1;
	HuffmanNode root = null;
	WordInfo wi = null;
	int[][][] huff_tree_tensor = null;

	public GenerateHuffmanTreeTensor(int standard_children_num, Map<Integer, Integer> statistics) {
		this.standard_children_num = standard_children_num;
		this.minimum_children_num = standard_children_num;
		this.root = BuildTree(statistics);
		this.wi = BuildEncodeTensor(this.root);
		this.huff_tree_tensor = ToTensor();
	}

	private HuffmanNode BuildTree(Map<Integer, Integer> statistics) {
		// , List<HuffmanNode> leafs
		Set<Integer> keys = statistics.keySet();
		// Character[] keys = .toArray(new Character[0]);

		PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<HuffmanNode>();
		for (Integer key : keys) {
			HuffmanNode node = new HuffmanNode();
			node.setContent(key);
			node.setFrequence(statistics.get(key));
			node.setMaxDepth(0);
			node.setLeafNodeNum(1);
			node.setNonLeafNodeNum(0);
			priorityQueue.add(node);
		}
		while (priorityQueue.size() > 1) {
			HuffmanNode sumNode = new HuffmanNode();
			int i_len = Math.min(standard_children_num, priorityQueue.size());
			int frequency = 0;
			int max_depth = 0;
			int leaf_node_num = 0;
			int non_leaf_node_num = 1;
			for (int i = 0; i < i_len; i++) {
				HuffmanNode child = priorityQueue.poll();
				frequency += child.getFrequence();
				if (max_depth < child.getMaxDepth()) {
					max_depth = child.getMaxDepth();
				}
				leaf_node_num += child.getLeafNodeNum();
				non_leaf_node_num += child.getNonLeafNodeNum();
				sumNode.appendToChildren(child);
			}
			sumNode.setContent(null);// node1.getContent() + node2.getContent()
			sumNode.setFrequence(frequency);
			sumNode.setMaxDepth(max_depth + 1);
			sumNode.setLeafNodeNum(leaf_node_num);
			sumNode.setNonLeafNodeNum(non_leaf_node_num);
			priorityQueue.add(sumNode);
		}
		// int size = priorityQueue.size();
		// for (int i = 1; i <= size - 1; i++) {
		// HuffmanNode node1 = priorityQueue.poll();
		// HuffmanNode node2 = priorityQueue.poll();
		//
		// HuffmanNode sumNode = new HuffmanNode();
		// sumNode.setContent(null);// node1.getContent() + node2.getContent()
		// sumNode.setFrequence(node1.getFrequence() + node2.getFrequence());
		// sumNode.setMaxDepth(Math.max(node1.getMaxDepth(), node2.getMaxDepth()) + 1);
		// sumNode.setLeftNode(node1);
		// sumNode.setRightNode(node2);
		// sumNode.setLeafNodeNum(node1.getLeafNodeNum() + node2.getLeafNodeNum());
		// sumNode.setTotalNodeNum(sumNode.getTotalNodeNum() + node1.getTotalNodeNum() +
		// node2.getTotalNodeNum());
		//
		// node1.setParent(sumNode);
		// node2.setParent(sumNode);
		//
		// priorityQueue.add(sumNode);
		// }
		Assert.isTrue(priorityQueue.size() == 1);
		return priorityQueue.poll();
	}

	private WordInfo BuildEncodeTensor(HuffmanNode root) {
		int width = root.getMaxDepth();
		int height = root.getLeafNodeNum();
		// System.out.println("=== height:" + height);
		int[][] encode_direction = new int[height][width];
		int[][] encode_state = new int[height][width];
		// int[] huff_tree_index = new int[height];
		int[] huff_tree_valid_children_num = new int[height];
		for (int i = 0; i < height; i++) {
			Arrays.fill(encode_direction[i], -1);
			Arrays.fill(encode_state[i], -1);
		}
		IDAssigner ida = new IDAssigner();
		// the origin of position of huff_tree_valid_children_num is huff_tree_index
		RecursiveBuildEncodeTensor(root, encode_direction, encode_state, huff_tree_valid_children_num,
				new Stack<Integer>(), new Stack<Integer>(), ida);
		// the origin of position of huff_tree_valid_children_num is huff_tree_index
		return new WordInfo(encode_direction, encode_state, huff_tree_valid_children_num);
	}

	private void RecursiveBuildEncodeTensor(HuffmanNode root, int[][] encode_direction, int[][] encode_state,
			// the origin of position of huff_tree_valid_children_num is huff_tree_index
			int[] huff_tree_valid_children_num, Stack<Integer> path, Stack<Integer> state, IDAssigner ida) {
		int id = ida.GetNewID();
		if (root.isLeaf()) {
			{
				// handle encode path which is for example: 0,1,0,1,-1 etc.
				Integer[] path_arr = new Integer[path.size()];
				path.toArray(path_arr);
				int[] path_arr_primitive = ArrayUtils.toPrimitive(path_arr);
				// System.out.println("=== tensor_length:" + tensor.length);
				// System.out.println("=== tensor_content:" + root.getContent());
				System.arraycopy(path_arr_primitive, 0, encode_direction[root.getContent()], 0,
						path_arr_primitive.length);
			}
			{
				// handle encode state which is for example: 0,12,26,78,-1 etc.
				// every number in above line is is just the encountered huff node id when
				// iterating huff tree
				Integer[] state_arr = new Integer[state.size()];
				state.toArray(state_arr);
				int[] state_arr_primitive = ArrayUtils.toPrimitive(state_arr);
				System.arraycopy(state_arr_primitive, 0, encode_state[root.getContent()], 0,
						state_arr_primitive.length);
			}
			// huff_tree_index[root.getContent()] = id;
		} else {
			List<HuffmanNode> children = root.getChildren();
			int child_size = children.size();
			huff_tree_valid_children_num[id] = child_size;
			if (maximum_children_num < child_size) {
				maximum_children_num = child_size;
			}
			if (minimum_children_num > child_size) {
				minimum_children_num = child_size;
			}
			int child_index = 0;
			Iterator<HuffmanNode> citr = children.iterator();
			while (citr.hasNext()) {
				HuffmanNode child = citr.next();
				path.push(child_index);
				state.push(id);
				RecursiveBuildEncodeTensor(child, encode_direction, encode_state, huff_tree_valid_children_num, path,
						state, ida);
				state.pop();
				path.pop();
				child_index++;
			}
			// if (root.hasLeftChild()) {
			// path.push(0);
			// state.push(id);
			// RecursiveBuildEncodeTensor(root.getLeftNode(), encode_direction,
			// encode_state, huff_tree_index, path,
			// state, ida);
			// state.pop();
			// path.pop();
			// }
			// if (root.hasRightChild()) {
			// path.push(1);
			// state.push(id);
			// RecursiveBuildEncodeTensor(root.getRightNode(), encode_direction,
			// encode_state, huff_tree_index, path,
			// state, ida);
			// state.pop();
			// path.pop();
			// }
		}
	}
	
	// ArrayList<Integer> node, ArrayList<Integer> left_child, ArrayList<Integer>
	// right_child
	private int[][][] ToTensor() {
		// int[][] tensor = new int[3][totalNodeNum];
		// Arrays.fill(tensor[0], -1);
		// Arrays.fill(tensor[1], -1);
		// Arrays.fill(tensor[2], -1);
		int nonLeafNodeNum = this.root.getNonLeafNodeNum();
		int[][][] tensor = new int[nonLeafNodeNum][2][maximum_children_num];
		for (int i = 0; i < nonLeafNodeNum; i++) {
			for (int j = 0; j < 2; j++) {
				Arrays.fill(tensor[i][j], -1);
			}
		}
		IDAssigner ida = new IDAssigner();
		ToTensor(this.root, tensor, ida);
		return tensor;
	}

	private int ToTensor(HuffmanNode curr_node, int[][][] tensor, IDAssigner ida) {
		int id = -1;
		if (curr_node.isLeaf()) {
			Integer id_raw = curr_node.getContent();
			Assert.isTrue(id_raw != null);
			id = id_raw;
		} else {
			id = ida.GetNewID();
			List<HuffmanNode> childrenNodes = curr_node.getChildren();
			Iterator<HuffmanNode> citr = childrenNodes.iterator();
			int child_index = 0;
			while (citr.hasNext()) {
				HuffmanNode child = citr.next();
				int infix_child_id = ToTensor(child, tensor, ida);
				tensor[id][0][child_index] = infix_child_id;
				tensor[id][1][child_index] = child.isLeaf() ? 1 : 0;
				child_index++;
			}
		}
		// int infix_left_id = -1;
		// if (leftNode != null) {
		// infix_left_id = leftNode.ToTensor(tensor, ida);
		// }
		// int infix_right_id = -1;
		// if (rightNode != null) {
		// infix_right_id = rightNode.ToTensor(tensor, ida);
		// }
		// tensor[0][id] = infix_left_id;
		// tensor[1][id] = infix_right_id;
		// tensor[2][id] = (content == null ? -1 : content);
		return id;
	}
	
	public int GetStandardChildrenNum() {
		return standard_children_num;
	}
	
	public int GetMaximumChildrenNum() {
		return maximum_children_num;
	}
	
	public int GetMinimumChildrenNum() {
		return minimum_children_num;
	}
	
	public int GetMaximumDepth() {
		return this.root.getMaxDepth();
	}
	
	public WordInfo GetWordInfo() {
		return wi;
	}
	
	public int[][][] GetHuffTreeTensor() {
		return huff_tree_tensor;
	}
	
}
