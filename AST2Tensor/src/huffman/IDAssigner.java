package huffman;

public class IDAssigner {
	
	int id = -1;
	
	public IDAssigner() {
	}
	
	public int GetNewID() {
		id++;
		return id;
	}
	
}