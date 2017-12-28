package huffman;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class GenerateHuffmanTree {
	
	public static HuffmanNode buildTree(Map<String, Integer> statistics) {
		// , List<HuffmanNode> leafs
		Set<String> keys = statistics.keySet();
		// Character[] keys = .toArray(new Character[0]);

		PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<HuffmanNode>();
		for (String key : keys) {
			HuffmanNode node = new HuffmanNode();
			node.setContent(key);
			node.setFrequence(statistics.get(key));
			priorityQueue.add(node);
		}

		int size = priorityQueue.size();
		for (int i = 1; i <= size - 1; i++) {
			HuffmanNode node1 = priorityQueue.poll();
			HuffmanNode node2 = priorityQueue.poll();
			
			HuffmanNode sumNode = new HuffmanNode();
			sumNode.setContent(null);// node1.getContent() + node2.getContent()
			sumNode.setFrequence(node1.getFrequence() + node2.getFrequence());
			
			sumNode.setLeftNode(node1);
			sumNode.setRightNode(node2);
			
			node1.setParent(sumNode);
			node2.setParent(sumNode);

			priorityQueue.add(sumNode);
		}

		return priorityQueue.poll();
	}
	
}
