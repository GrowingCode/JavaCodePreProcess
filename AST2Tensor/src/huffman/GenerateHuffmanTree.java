package huffman;

import java.util.Arrays;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.core.runtime.Assert;

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
			node.setLeafNodeNum(1);
			priorityQueue.add(node);
		}

		int size = priorityQueue.size();
		for (int i = 1; i <= size - 1; i++) {
			HuffmanNode node1 = priorityQueue.poll();
			HuffmanNode node2 = priorityQueue.poll();

			HuffmanNode sumNode = new HuffmanNode();
			sumNode.setContent(null);// node1.getContent() + node2.getContent()
			sumNode.setFrequence(node1.getFrequence() + node2.getFrequence());
			sumNode.setMaxDepth(Math.max(node1.getMaxDepth(), node2.getMaxDepth()) + 1);
			sumNode.setLeftNode(node1);
			sumNode.setRightNode(node2);
			sumNode.setLeafNodeNum(node1.getLeafNodeNum() + node2.getLeafNodeNum());
			sumNode.setTotalNodeNum(sumNode.getTotalNodeNum() + node1.getTotalNodeNum() + node2.getTotalNodeNum());

			node1.setParent(sumNode);
			node2.setParent(sumNode);

			priorityQueue.add(sumNode);
		}
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
		for (int i=0;i<height;i++) {
			Arrays.fill(encode_direction[i], -1);
			Arrays.fill(encode_state[i], -1);
		}
		IDAssigner ida = new IDAssigner();
		RecursiveBuildEncodeTensor(root, encode_direction, encode_state, huff_tree_index, new Stack<Integer>(), new Stack<Integer>(), ida);
		return new WordInfo(encode_direction, encode_state, huff_tree_index);
	}

	private static void RecursiveBuildEncodeTensor(HuffmanNode root, int[][] encode_direction, int[][] encode_state, int[] huff_tree_index, Stack<Integer> path, Stack<Integer> state, IDAssigner ida) {
		int id = ida.GetNewID();
		if (root.isLeaf()) {
			Integer[] path_arr = new Integer[path.size()];
			path.toArray(path_arr);
			int[] path_arr_primitive = ArrayUtils.toPrimitive(path_arr);
			// System.out.println("=== tensor_length:" + tensor.length);
			// System.out.println("=== tensor_content:" + root.getContent());
			System.arraycopy(path_arr_primitive, 0, encode_direction[root.getContent()], 0, path_arr_primitive.length);
			// 
			
			huff_tree_index[root.getContent()] = id;
		} else {
			if (root.hasLeftChild()) {
				path.push(0);
				state.push(id);
				RecursiveBuildEncodeTensor(root.getLeftNode(), encode_direction, encode_state, huff_tree_index, path, state, ida);
				state.pop();
				path.pop();
			}
			if (root.hasRightChild()) {
				path.push(1);
				state.push(id);
				RecursiveBuildEncodeTensor(root.getRightNode(), encode_direction, encode_state, huff_tree_index, path, state, ida);
				state.pop();
				path.pop();
			}
		}
	}

}
