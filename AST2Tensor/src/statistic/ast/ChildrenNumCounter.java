package statistic.ast;

import java.io.File;
import java.util.TreeMap;

import com.google.gson.Gson;

import util.FileUtil;

public class ChildrenNumCounter {
	
	int max_children_num = 0;
	
	public void EncounterChildrenNum(int children_num) {
		if (max_children_num < children_num) {
			max_children_num = children_num;
		}
	}
	
	public int GetMaxChildrenNum() {
		return max_children_num;
	}

	public void SaveToDirectory(String dir) {
//		Gson gson = new Gson();
//		TreeMap<String, Integer> meta_of_ast = new TreeMap<String, Integer>();
//		meta_of_ast.put("MaxChildrenNum", max_children_num);
//		FileUtil.WriteToFile(new File(dir + "/" + "Meta_of_ast.json"), gson.toJson(meta_of_ast));
		System.out.println("MaxChildrenNum:" + max_children_num);
	}
	
}
