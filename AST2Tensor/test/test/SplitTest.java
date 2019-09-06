package test;

import java.util.ArrayList;

import statistic.YTokenizer;

public class SplitTest {
	
	public static void main(String[] args) {
		ArrayList<String> ll = YTokenizer.GetTokens("if (a && b) {i = i+1;System.out.println(\"ha\\\"ha\")}");
		for (String str_l : ll) {
			System.out.println(str_l);
		}
	}
	
}
