package util;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

public class JsonPrintUtil {
	
	public static void PrintMapToJsonFile(Map<?, ?> map, String dir, String json_file_name) {
		Gson gson = new Gson();
		TreeMap<String, Integer> meta_of_ast2tensor = new TreeMap<String, Integer>();
//		meta_of_ast2tensor.put("AddZeroIfNoVariable", AddZeroIfNoVariable);
//		meta_of_ast2tensor.put("MaximumHandlingNodeNumInOneTree", MaximumHandlingNodeNumInOneTree);
//		meta_of_ast2tensor.put("MaximumStringLength", MaximumStringLength);
//		meta_of_ast2tensor.put("NumberOfMerges", NumberOfMerges);
//		meta_of_ast2tensor.put("MinimumNumberOfNodesInAST", MinimumNumberOfNodesInAST);
//		meta_of_ast2tensor.put("MaximumNumberOfNodesInAST", MaximumNumberOfNodesInAST);
//		meta_of_ast2tensor.put("InBPEForm", InBPEForm ? 1 : 0);
//		meta_of_ast2tensor.put("GenerateSkeletonToken", GenerateSkeletonToken ? 1 : 0);
		
//		meta_of_ast2tensor.put("UseLexicalToken", UseLexicalToken ? 1 : 0);
//		meta_of_ast2tensor.put("TakeUnseenAsUnk", TakeUnseenAsUnk ? 1 : 0);
//		meta_of_ast2tensor.put("VariableNoLimit", VariableNoLimit ? 1 : 0);
//		meta_of_ast2tensor.put("MethodNoLimit", MethodNoLimit ? 1 : 0);
//		meta_of_ast2tensor.put("JavaFileNoLimit", JavaFileNoLimit ? 1 : 0);
//		String dir = System.getProperty("user.home") + "/AST_Metas";
		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
		}
		FileUtil.WriteToFile(new File(dir + "/" + json_file_name), gson.toJson(meta_of_ast2tensor));
	}
	
}
