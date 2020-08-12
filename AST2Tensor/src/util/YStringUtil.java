package util;

import org.eclipse.core.runtime.Assert;

public class YStringUtil {
	
//	String to_be_replace, 
	public static String ReplaceSpecifiedContentInSpecifiedPosition(String base, String replace_content, int index) {
		Assert.isTrue(index > -1);
		int i = 0;
		int pos = -2;
		while (i <= index) {
			int pos_h = base.indexOf("#h", pos+2);
			if (pos_h == -1) {
				pos_h = Integer.MAX_VALUE;
			}
			int pos_v = base.indexOf("#v", pos+2);
			if (pos_v == -1) {
				pos_v = Integer.MAX_VALUE;
			}
			int pos_m = base.indexOf("#m", pos+2);
			if (pos_m == -1) {
				pos_m = Integer.MAX_VALUE;
			}
			pos = Math.min(Math.min(pos_h, pos_v), pos_m);
			Assert.isTrue(pos > -1 && pos < Integer.MAX_VALUE, "m:" + base + "======" + replace_content);
			i++;
		}
		Assert.isTrue(pos != -1);
		StringBuilder sb = new StringBuilder(base);
		return sb.replace(pos, pos+2, replace_content).toString();
	}
	
	public static String TrimString(String s, int size) {
		if (s.length() < size) {
			return s;
		} else {
			return s.substring(0, size);
		}
	}
	
	public static int CountSubStringInString(String s, String sub_s) {
		int count = 0;
		int s_pos = 0;
		while (true) {
			s_pos = s.indexOf(sub_s, s_pos);
			if (s_pos >= 0) {
				count++;
			} else {
				break;
			}
			s_pos++;
		}
		return count;
	}
	
}
