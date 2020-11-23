package util;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Assert;

public class StringUtil {
	
	public static String UniformEmptyBlockString(String block_str) {
		Assert.isTrue(block_str.startsWith("{"));
		Assert.isTrue(block_str.endsWith("}"));
//			with_block_types.add(node.getClass().getName());
		Assert.isTrue(Pattern.matches("\\{\\s*\\}", block_str), "unmatch content:" + block_str);
		return "{}";
	}
	
	public static String FixedLengthString(String content, int fixed_length) {
		int r_f_len = fixed_length;
		if (content.length() > fixed_length) {
			r_f_len = fixed_length - 1;
		}
		String n_cnt = String.format("%"+r_f_len+"s", content);
		n_cnt = n_cnt.substring(0, r_f_len);
		if (content.length() > fixed_length) {
			n_cnt += "~";
		}
		return n_cnt;
	}
	
	public static ArrayList<String> ArrayElementToFixedLength(ArrayList<?> arr, int fixed_length) {
		ArrayList<String> result = new ArrayList<String>();
		for (Object ele : arr) {
			String ele_str = ele.toString();
			String r_ele_str = FixedLengthString(ele_str, fixed_length);
			result.add(r_ele_str);
		}
		return result;
	}
	
	public static void main(String[] args) {
		String n_p = StringUtil.FixedLengthString("sad", 10);
		System.out.println(n_p);
	}
	
}


