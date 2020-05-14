package tree;

import java.util.ArrayList;

public class Forest {
	
	ArrayList<Tree> stmts = new ArrayList<Tree>();
	
	public Forest() {
	}
	
	public void AddTree(Tree t) {
		stmts.add(t);
	}
	
	public void AddTrees(ArrayList<Tree> trees) {
		stmts.addAll(trees);
	}
	
}
