package statistic.id;

import main.MetaOfApp;

public class PreProcessContentHelper {
	
	public static String PreProcessTypeContent(String type_content) {
		int pos = type_content.indexOf('#');
		String type = type_content.substring(0, pos);
		String content = type_content.substring(pos+1);
		if (content.startsWith("\"") && content.endsWith("\"") && content.length() > MetaOfApp.MaximumStringLength+2) {
			content = content.substring(1, content.length()-1);
			content = content.substring(0, MetaOfApp.MaximumStringLength);
			content = "\"" + content + "\"";
		}
		return type + "#" + content;
	}
	
}
