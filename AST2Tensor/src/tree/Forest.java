package tree;

import java.util.ArrayList;

public class Forest {
	
	String sig = null;
	int role = -1;
	ArrayList<Tree> stmts = new ArrayList<Tree>();
	
	public Forest(String sig, int role) {
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

	public int GetRole() {
		return role;
	}
	
}
