package util;

public class StringUtil {
	
	public static String FixedLengthString(String content, int fixed_length) {
		String n_cnt = String.format("%"+fixed_length+"s", content);
		n_cnt = n_cnt.substring(0, fixed_length);
		return n_cnt;
	}
	
	public static void main(String[] args) {
		String n_p = StringUtil.FixedLengthString("sad", 10);
		System.out.println(n_p);
	}
	
}


