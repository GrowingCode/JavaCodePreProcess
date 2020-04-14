package statistic.id;

import main.MetaOfApp;

public class PreProcessContentHelper {
	
	public static String PreProcessTypeContent(String content) {
//		int pos = type_content.indexOf('#');
//		String type = type_content.substring(0, pos);
//		String content = type_content.substring(pos+1);
		content = content.trim();
		if (content.startsWith("\"") && content.endsWith("\"") && content.length() > MetaOfApp.MaximumStringLength+2) {
			content = content.substring(1, content.length()-1);
			content = content.substring(0, MetaOfApp.MaximumStringLength);
			content = "\"" + content + "\"";
		}
		content = content.replaceAll("\\s+", "#");
		content = content.replaceAll("_+", "`");
		content = content.replaceAll("\\$+", "`");
		content = "$" + content + "_";
		return content;
	}
	
	public static void main(String[] args) {
		System.out.println(PreProcessContentHelper.PreProcessTypeContent("a_b"));
	}
	
}
