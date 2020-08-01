package util;

import org.eclipse.core.runtime.Assert;

public class YStringUtil {
	
//	String to_be_replace, 
	public static String ReplaceSpecifiedContentInSpecifiedPosition(String m, String replace_content, int index) {
		Assert.isTrue(index > -1);
		int i = 0;
		int pos = -1;
		while (i <= index) {
			int pos_h = m.indexOf("#h", pos);
			int pos_v = m.indexOf("#v", pos);
			pos = Math.min(pos_h, pos_v);
			Assert.isTrue(pos > -1);
			i++;
		}
		Assert.isTrue(pos != -1);
		StringBuilder sb = new StringBuilder("");
		return sb.replace(pos, pos+2, replace_content).toString();
	}
	
	public static String TrimString(String s, int size) {
		if (s.length() < size) {
			return s;
		} else {
			return s.substring(0, size);
		}
	}
	
}
