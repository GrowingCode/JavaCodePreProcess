package statis.trans.common;

import java.util.ArrayList;

import tree.Forest;

public class SkeletonForestRecorder {
	
	ArrayList<Forest> funcs = new ArrayList<Forest>();
	
	public SkeletonForestRecorder() {
	}
	
	public ArrayList<Forest> GetAllForests() {
		return funcs;
	}
	
	public void AddForests(ArrayList<Forest> funcs) {
		this.funcs.addAll(funcs);
	}
	
	public void Clear() {
		funcs.clear();
	}
	
}
