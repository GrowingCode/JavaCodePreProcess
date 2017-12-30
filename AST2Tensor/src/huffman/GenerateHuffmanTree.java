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

	public static int[][] BuildEncodeTensor(HuffmanNode root) {
		int width = root.getMaxDepth();
		int height = root.getLeafNodeNum();
		// System.out.println("=== height:" + height);
		int[][] tensor = new int[height][width];
		for (int i=0;i<height;i++) {
			Arrays.fill(tensor[i], -1);
		}
		RecursiveBuildEncodeTensor(root, tensor, new Stack<Integer>());
		return tensor;
	}

	private static void RecursiveBuildEncodeTensor(HuffmanNode root, int[][] tensor, Stack<Integer> path) {
		if (root.isLeaf()) {
			Integer[] path_arr = new Integer[path.size()];
			path.toArray(path_arr);
			int[] path_arr_primitive = ArrayUtils.toPrimitive(path_arr);
			// System.out.println("=== tensor_length:" + tensor.length);
			// System.out.println("=== tensor_content:" + root.getContent());
			System.arraycopy(path_arr_primitive, 0, tensor[root.getContent()], 0, path_arr_primitive.length);
		} else {
			if (root.hasLeftChild()) {
				path.push(0);
				RecursiveBuildEncodeTensor(root.getLeftNode(), tensor, path);
				path.pop();
			}
			if (root.hasRightChild()) {
				path.push(1);
				RecursiveBuildEncodeTensor(root.getRightNode(), tensor, path);
				path.pop();
			}
		}
	}

}
