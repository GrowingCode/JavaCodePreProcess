package tree;

import java.util.ArrayList;

public class Forest {
	
	String file_path = null;
	String sig = null;
	int role = -1;
	ArrayList<Tree> stmts = new ArrayList<Tree>();
	
	public Forest(String file_path, String sig, int role) {
		this.file_path = file_path;
		this.sig = sig;
		this.role = role;
	}
	
	public void AddTree(Tree t) {
		stmts.add(t);
	}
	
	public void AddTrees(ArrayList<Tree> trees) {
		stmts.addAll(trees);
	}
	
	public ArrayList<Tree> GetAllTrees() {
		return stmts;
	}

	public String GetSignature() {
		return sig;
	}
	
	public String GetFilePath() {
		return file_path;
	}

	public int GetRole() {
		return role;
	}
	
}
