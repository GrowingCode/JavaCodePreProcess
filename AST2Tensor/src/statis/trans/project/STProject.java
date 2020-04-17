package statis.trans.project;

import org.eclipse.jdt.core.IJavaProject;

import eclipse.project.ProjectInfo;

public class STProject {
	
	IJavaProject java_project = null;
	ProjectInfo info = null;
	
	public STProject(ProjectInfo info, IJavaProject java_project) {
		this.info = info;
		this.java_project = java_project;
	}
	
	public IJavaProject GetJavaProject() {
		return java_project;
	}
	
	public ProjectInfo GetInfo() {
		return info;
	}
	
}
