package test;

import java.io.File;

import main.Meta;
import net.sf.json.JSONArray;
import util.FileUtil;

public class TestEncodeAndTree {
	
	public void TestOneEncodeAndTree() {
		
	}

	public static void main(String[] args) {
		String json_encode = FileUtil.ReadFromFile(new File(Meta.DataDirectory + "/" + "All_type_huff_leaf_encode.json"));
		String json_tree = FileUtil.ReadFromFile(new File(Meta.DataDirectory + "/" + "All_type_huff_tree.json"));
		JSONArray jarr = JSONArray.fromObject(json_encode);
		JSONArray jarr_tree = JSONArray.fromObject(json_tree);
		Object[] encode = (Object[])JSONArray.toArray(jarr);
		System.err.println(encode);
		System.err.println("jarr:" + jarr);
		System.err.println("jarr_tree:" + jarr_tree);
	}

}
