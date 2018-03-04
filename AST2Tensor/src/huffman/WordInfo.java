package huffman;

public class WordInfo {
	
	private int[][] encode_direction = null;
	private int[][] encode_state = null;
	private int[] huff_tree_index = null;
	
	public WordInfo(int[][] encode_direction, int[][] encode_state, int[] huff_tree_index) {
		this.setEncodeDirection(encode_direction);
		this.setEncodeState(encode_state);
		this.setHuffTreeIndex(huff_tree_index);
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

	public int[] getHuffTreeIndex() {
		return huff_tree_index;
	}

	private void setHuffTreeIndex(int[] huff_tree_index) {
		this.huff_tree_index = huff_tree_index;
	}
	
}
