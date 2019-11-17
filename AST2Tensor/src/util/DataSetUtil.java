package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSetUtil {

	public static String FilterNumberPrefix(String with_number_prefix) {
		String reg = "^(\\d+_)";
		Pattern p2 = Pattern.compile(reg);
		Matcher m2 = p2.matcher(with_number_prefix);
		if (m2.find()) {
			return with_number_prefix.substring(m2.group(1).length());
		} else {
			return with_number_prefix;
		}
//		System.out.println(m2.group(1));
	}
	
	public static void main(String[] args) {
		System.out.println(DataSetUtil.FilterNumberPrefix("261784785189560_JunitTestRunnerFilter.java"));
	}

}
