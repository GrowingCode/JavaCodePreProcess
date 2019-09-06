package statistic;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class YTokenizer {

//	public static final String TokenReg = "\\||\\.|\\s+|(|)|{|}|+|-|*|/|%|\"|'|:|&|^|~|!|++|--|==|!=|>|<|>=|<=|=|+=|-=|*=|/=|%=|&=|^=|\\|=|<<=|>>=|<<|>>|>>>|&&|\\|\\|;";
	public static final String TokenDelim = "|.(){}+-*/%\"':&^~!=><\\;";

	public static ArrayList<String> GetTokens(String content) {
		String content_w = content.replaceAll("\\s+", " ");
		String[] cws = content_w.split(" ");
		ArrayList<String> result = new ArrayList<String>();
		for (String cw : cws) {
			StringTokenizer st = new StringTokenizer(cw, TokenDelim, true);
			while (st.hasMoreTokens()) {
				result.add(st.nextToken());
			}
		}
		return result;
	}
	
}
