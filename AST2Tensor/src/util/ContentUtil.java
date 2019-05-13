package util;

import java.util.ArrayList;
import java.util.Arrays;

public class ContentUtil {
	
	private static final String reg = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
	
	public static String[] SplitByCamelCase(String cnt) {
		return cnt.split(reg);
	}
	
	public static String[] SplitByUnderScore(String cnt) {
		return cnt.split("_+");
	}
	
	public static ArrayList<String> SplitByUnderScoreWithCamelCase(String cnt) {
		ArrayList<String> result = new ArrayList<String>();
		String[] strs = SplitByUnderScore(cnt);
		for (String str : strs) {
			String[] cc = SplitByCamelCase(str);
			result.addAll(Arrays.asList(cc));
		}
		return result;
	}
	
}
