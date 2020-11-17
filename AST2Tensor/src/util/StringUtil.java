package util;

import java.util.ArrayList;

public class StringUtil {
	
	public static String FixedLengthString(String content, int fixed_length) {
		String n_cnt = String.format("%"+fixed_length+"s", content);
		n_cnt = n_cnt.substring(0, fixed_length);
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


