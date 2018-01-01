package huffman;

public class WordInfo {
	
	private int[][] encode = null;
	private int[] huff_tree_index = null;
	
	public WordInfo(int[][] encode, int[] huff_tree_index) {
		this.setEncode(encode);
		this.setHuffTreeIndex(huff_tree_index);
	}

	public int[][] getEncode() {
		return encode;
	}

	public void setEncode(int[][] encode) {
		this.encode = encode;
	}

	public int[] getHuffTreeIndex() {
		return huff_tree_index;
	}

	public void setHuffTreeIndex(int[] huff_tree_index) {
		this.huff_tree_index = huff_tree_index;
	}
	
}
