package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Assert;

public class DataSetUtil {

	public static String FilterNumberPrefix(String with_number_prefix) {
		String reg = "^(\\d+_)";
		Pattern p2 = Pattern.compile(reg);
		Matcher m2 = p2.matcher(with_number_prefix);
		Assert.isTrue(m2.find());
//		System.out.println(m2.group(1));
		return with_number_prefix.substring(m2.group(1).length());
	}
	
	public static void main(String[] args) {
		System.out.println(DataSetUtil.FilterNumberPrefix("261784785189560_JunitTestRunnerFilter.java"));
	}

}
