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

import main.Meta;

public class GenerateHuffmanTree {

	public static HuffmanNode BuildTree(Map<Integer, Integer> statistics) {
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
			int i_len = Math.min(Meta.HuffTreeStandardChildrenNum, priorityQueue.size());
			int frequency = 0;
			int max_depth = 0;
			int leaf_node_num = 0;
			int non_leaf_node_num = 1;
			for (int i=0;i<i_len;i++) {
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
		}
//		int size = priorityQueue.size();
//		for (int i = 1; i <= size - 1; i++) {
//			HuffmanNode node1 = priorityQueue.poll();
//			HuffmanNode node2 = priorityQueue.poll();
//
//			HuffmanNode sumNode = new HuffmanNode();
//			sumNode.setContent(null);// node1.getContent() + node2.getContent()
//			sumNode.setFrequence(node1.getFrequence() + node2.getFrequence());
//			sumNode.setMaxDepth(Math.max(node1.getMaxDepth(), node2.getMaxDepth()) + 1);
//			sumNode.setLeftNode(node1);
//			sumNode.setRightNode(node2);
//			sumNode.setLeafNodeNum(node1.getLeafNodeNum() + node2.getLeafNodeNum());
//			sumNode.setTotalNodeNum(sumNode.getTotalNodeNum() + node1.getTotalNodeNum() + node2.getTotalNodeNum());
//
//			node1.setParent(sumNode);
//			node2.setParent(sumNode);
//
//			priorityQueue.add(sumNode);
//		}
		Assert.isTrue(priorityQueue.size() == 1);
		return priorityQueue.poll();
	}

	public static WordInfo BuildEncodeTensor(HuffmanNode root) {
		int width = root.getMaxDepth();
		int height = root.getLeafNodeNum();
		// System.out.println("=== height:" + height);
		int[][] encode_direction = new int[height][width];
		int[][] encode_state = new int[height][width];
		int[] huff_tree_index = new int[height];
		for (int i = 0; i < height; i++) {
			Arrays.fill(encode_direction[i], -1);
			Arrays.fill(encode_state[i], -1);
		}
		IDAssigner ida = new IDAssigner();
		RecursiveBuildEncodeTensor(root, encode_direction, encode_state, huff_tree_index, new Stack<Integer>(),
				new Stack<Integer>(), ida);
		return new WordInfo(encode_direction, encode_state, huff_tree_index);
	}

	private static void RecursiveBuildEncodeTensor(HuffmanNode root, int[][] encode_direction, int[][] encode_state,
			int[] huff_tree_index, Stack<Integer> path, Stack<Integer> state, IDAssigner ida) {
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
			huff_tree_index[root.getContent()] = id;
		} else {
			List<HuffmanNode> children = root.getChildren();
			int child_index = 0;
			Iterator<HuffmanNode> citr = children.iterator();
			while (citr.hasNext()) {
				HuffmanNode child = citr.next();
				path.push(child_index);
				state.push(id);
				RecursiveBuildEncodeTensor(child, encode_direction, encode_state, huff_tree_index, path,
						state, ida);
				state.pop();
				path.pop();
				child_index++;
			}
//			if (root.hasLeftChild()) {
//				path.push(0);
//				state.push(id);
//				RecursiveBuildEncodeTensor(root.getLeftNode(), encode_direction, encode_state, huff_tree_index, path,
//						state, ida);
//				state.pop();
//				path.pop();
//			}
//			if (root.hasRightChild()) {
//				path.push(1);
//				state.push(id);
//				RecursiveBuildEncodeTensor(root.getRightNode(), encode_direction, encode_state, huff_tree_index, path,
//						state, ida);
//				state.pop();
//				path.pop();
//			}
		}
	}

}
