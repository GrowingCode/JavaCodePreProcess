package util;

import java.io.File;
import java.util.Map;

import com.google.gson.Gson;

public class JsonPrintUtil {
	
	public static void PrintMapToJsonFile(Map<?, ?> map, String dir, String json_file_name) {
		Gson gson = new Gson();
		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
		}
		FileUtil.WriteToFile(new File(dir + "/" + json_file_name), gson.toJson(map));
	}
	
}
