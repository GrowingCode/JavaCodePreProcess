package huffman;

public class WordInfo {
	
	private int[][] encode_direction = null;
	private int[][] encode_state = null;
//	private int[] huff_tree_index = null;
	private int[] huff_tree_valid_children_num = null;
	
	// the origin of position of huff_tree_standard_children_num is huff_tree_index
	public WordInfo(int[][] encode_direction, int[][] encode_state, int[] huff_tree_valid_children_num) {
		this.setEncodeDirection(encode_direction);
		this.setEncodeState(encode_state);
//		this.setHuffTreeIndex(huff_tree_index);
		this.setHuffTreeValidChildrenNum(huff_tree_valid_children_num);
	}
	
	public int[][] getEncodeState() {
		return encode_state;
	}

	private void setEncodeState(int[][] encode_state) {
		this.encode_state = encode_state;
	}
	
	public int[][] getEncodeDirection() {
		return encode_direction;
	}

	private void setEncodeDirection(int[][] encode_direction) {
		this.encode_direction = encode_direction;
	}

//	public int[] getHuffTreeIndex() {
//		return huff_tree_index;
//	}
//
//	private void setHuffTreeIndex(int[] huff_tree_index) {
//		this.huff_tree_index = huff_tree_index;
//	}
	
	public int[] getHuffTreeValidChildrenNum() {
		return huff_tree_valid_children_num;
	}

	private void setHuffTreeValidChildrenNum(int[] huff_tree_valid_children_num) {
		this.huff_tree_valid_children_num = huff_tree_valid_children_num;
	}
	
}
