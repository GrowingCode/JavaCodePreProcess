package statis.trans.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

import bpe.skt.SktPETreesUtil;
import bpe.skt.TreeNodeTwoMerge;
import eclipse.project.ProjectInfo;
import tree.Forest;
import tree.ProjectForests;
import tree.Tree;

public class SkeletonForestRecorder {
	
	ArrayList<ProjectForests> pfs = new ArrayList<ProjectForests>();
	
//	TreeMap<String, ArrayList<String>> token_composes = new TreeMap<String, ArrayList<String>>();
	
	ArrayList<Tree> all_trees = null;
	
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

	public void ApplySktPEMerges(List<TreeNodeTwoMerge> merges) {
		GetAllTrees();
		SktPETreesUtil.ApplySktPEMergesToTrees(merges, all_trees);// , token_composes
	}
	
	public void PreProcessAllForests() {
		GetAllTrees();
		SktPETreesUtil.PreProcessAllTrees(all_trees);
	}
	
	public void FlattenAllOriginTrees() {
		GetAllTrees();
		for (Tree o_tree : all_trees) {
			o_tree.FlattenOriginTree();
		}
	}
	
	private ArrayList<Tree> GetAllTrees() {
		if (all_trees == null) {
			all_trees = new ArrayList<Tree>();
			ArrayList<Forest> fs = GetAllForests();
			for (Forest f : fs) {
				ArrayList<Tree> f_trees = f.GetAllTrees();
				all_trees.addAll(f_trees);
			}
		}
		return all_trees;
	}
	
//	public ArrayList<String> GetComposedTokens(String merged) {
//		ArrayList<String> r = new ArrayList<String>();
//		ArrayList<String> tcs = token_composes.get(merged);
//		if (tcs != null) {
//			r.addAll(tcs);
//		}
//		return r;
//	}
	
//	public TreeMap<String, ArrayList<String>> GetAllTokenComposes() {
//		return token_composes;
//	}
	
}




