package tree;

import java.util.ArrayList;

public class Forest {
	
	int role = -1;
	ArrayList<Tree> stmts = new ArrayList<Tree>();
	
	public Forest(int role) {
		this.role = role;
	}
	
	public void AddTree(Tree t) {
		stmts.add(t);
	}
	
	public void AddTrees(ArrayList<Tree> trees) {
		stmts.addAll(trees);
	}
	
	public int GetRole() {
		return role;
	}
	
	public ArrayList<Tree> GetAllTrees() {
		return stmts;
	}
	
}
