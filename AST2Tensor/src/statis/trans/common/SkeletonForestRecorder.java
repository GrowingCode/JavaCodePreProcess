package statis.trans.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.StringLiteral;

import bpe.skt.SktPETreesUtil;
import bpe.skt.TreeNodeTwoMerge;
import eclipse.project.ProjectInfo;
import tree.Forest;
import tree.ProjectForests;
import tree.Tree;
import tree.TreeNode;
import util.PrintUtil;
import util.YStringUtil;

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

	public void ApplySktPEMerges(List<TreeNodeTwoMerge> merges, int merge_num) {
		GetAllTrees();
		SktPETreesUtil.ApplySktPEMergesToTrees(merges, all_trees, merge_num);// , token_composes
	}
	
	public void PreProcessAllForests() {
		GetAllTrees();
		SktPETreesUtil.PreProcessAllTrees(all_trees);
	}
	
//	public void FlattenAllOriginTrees() {
//		GetAllTrees();
//		for (Tree o_tree : all_trees) {
//			o_tree.FlattenOriginTree();
//		}
//	}
	
	public void FlattenAllTrees() {
		GetAllTrees();
		for (Tree o_tree : all_trees) {
			o_tree.FlattenTree(true);
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
			ValidateAllTrees();
		}
		return all_trees;
	}
	
	private void ValidateAllTrees() {
		for (Tree o_tree : all_trees) {
			ArrayList<TreeNode> ans = o_tree.GetAllNodes();
			for (TreeNode an : ans) {
				ArrayList<TreeNode> childs = an.GetChildren();
				if (an.GetClazz().equals(StringLiteral.class) || an.GetContent().indexOf("\"") != an.GetContent().lastIndexOf("\"")) {
					// do nothing. 
				} else {
					int h_count = YStringUtil.CountSubStringInString(an.GetContent(), "#h");
					int v_count = YStringUtil.CountSubStringInString(an.GetContent(), "#v");
					
					Assert.isTrue(h_count + v_count == childs.size(), "Origin Tree Validation Failed!" + "#h_count:" + h_count + "#v_count:" + v_count + "#childs.size():" + childs.size() + "#rt.GetContent():" + an.GetClazz() + "#rt.GetContent():" + an.GetContent() + "#childs:" + PrintUtil.PrintListToString(childs, "tns"));
				}
			}
		}
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




