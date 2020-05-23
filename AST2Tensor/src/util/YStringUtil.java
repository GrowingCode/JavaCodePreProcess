package util;

import org.eclipse.core.runtime.Assert;

public class YStringUtil {
	
	public static String ReplaceSpecifiedContentInSpecifiedPosition(String m, String to_be_replace, String replace_content, int index) {
		Assert.isTrue(index > -1);
		int i = 0;
		int pos = 0;
		while (i <= index) {
			pos = m.indexOf(to_be_replace, pos);
			Assert.isTrue(pos > -1);
		}
		StringBuilder sb = new StringBuilder("");
		return sb.replace(pos, pos+to_be_replace.length(), replace_content).toString();
	}
	
}
