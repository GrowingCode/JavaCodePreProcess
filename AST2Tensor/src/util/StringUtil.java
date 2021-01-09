package util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Assert;

public class StringUtil {
	
	public static String EliminateNonStrSplitWhiteSpace(String str) {
		String tp = str;
		String reg1 = "(?<![a-zA-Z_\\$#][a-zA-Z\\d_\\$]*)\\s+(?=[a-zA-Z_\\$#][a-zA-Z\\d_\\$]*)";
		tp = EliminateRegMatchedWhiteSpace(tp, reg1);
		String reg2 = "(?<=[a-zA-Z_\\$#][a-zA-Z\\d_\\$]*)\\s+(?![a-zA-Z_\\$#][a-zA-Z\\d_\\$]*)";
		tp = EliminateRegMatchedWhiteSpace(tp, reg2);
		String reg3 = "(?<![a-zA-Z_\\$#][a-zA-Z\\d_\\$]*)\\s+(?![a-zA-Z_\\$#][a-zA-Z\\d_\\$]*)";
		tp = EliminateRegMatchedWhiteSpace(tp, reg3);
		return tp;
	}
	
	private static String EliminateRegMatchedWhiteSpace(String str, String reg) {
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
//		System.err.println("=== begin printing ===");
		while(m.find()){
//			System.err.println("group count:" + m.groupCount());
//			for (int i=0;i<m.groupCount();i++) {
//				System.err.println("gp " + i + ":" + m.group(i));
//			}
//			System.err.println("group:" + m.group());
//			String tmp = m.group(rep_index);
//			System.err.println("tmp:" + tmp);
			String v = "";
			m.appendReplacement(sb, v);
		}
		m.appendTail(sb);
		String sb_str = sb.toString();
//		System.out.println(sb_str);
		return sb_str;
	}

//	public static String EliminateWhiteSpace(String str) {
//		return str.replaceAll("\\s+", "");
//	}

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
		String n_cnt = String.format("%" + r_f_len + "s", content);
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
//		String n_p = StringUtil.FixedLengthString("sad", 10);
//		System.out.println(n_p);
//		String e_p = StringUtil.EliminateWhiteSpace("a b");
//		System.out.println(e_p);
		String c_p = StringUtil.EliminateNonStrSplitWhiteSpace(" a b c ; #v + #h ;  	#h   #h; new #v(  );");
		System.out.println(c_p);
//		String reg = null;
//		reg = "(?<=[a-zA-Z_\\$#]+)\\s+(?=.*)";
////		String tp = 
//		EliminateRegMatchedWhiteSpace("String a;", reg);
//		System.err.println("==== split line ====");
//		reg = "(?<!String)\\s+(?=.*)";
////		String tp = 
//		EliminateRegMatchedWhiteSpace("String a;", reg);
//		System.out.println("tp:" + tp);
	}

}
