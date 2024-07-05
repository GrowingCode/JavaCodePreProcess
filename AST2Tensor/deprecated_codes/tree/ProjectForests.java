package tree;

import java.util.ArrayList;

import eclipse.project.ProjectInfo;

public class ProjectForests {
	
	ProjectInfo pi = null;

	ArrayList<Forest> funcs = new ArrayList<Forest>();
	
	public ProjectForests(ProjectInfo pi) {
		this.pi = pi;
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
	
	public ProjectInfo GetProjectInfo() {
		return pi;
	}
	
}
