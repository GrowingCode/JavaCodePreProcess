package statis.trans.common;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;

import eclipse.project.ProjectInfo;
import tree.Forest;
import tree.ProjectForests;

public class SkeletonForestRecorder {
	
	ArrayList<ProjectForests> pfs = new ArrayList<ProjectForests>();
	
	public SkeletonForestRecorder() {
	}
	
	public void EncounterNewProject(ProjectInfo pi) {
		pfs.add(new ProjectForests(pi));
	}
	
	public ArrayList<ProjectForests> GetAllProjectsWithForests() {
		return pfs;
	}
	
	public ArrayList<Forest> GetAllForests() {
		ArrayList<Forest> funcs = new ArrayList<Forest>();
		for (ProjectForests pf : pfs) {
			funcs.addAll(pf.GetAllForests());
		}
		return funcs;
	}
	
	public void AddForests(ArrayList<Forest> funcs) {
		Assert.isTrue(pfs.size() > 0);
		ProjectForests pf = pfs.get(pfs.size()-1);
		pf.AddForests(funcs);
	}
	
	public void Clear() {
		pfs.clear();
	}
	
}




