package test;

import util.ContentUtil;

public class CamelCaseTest {

	public static void main(String[] args) {
		for (String w : ContentUtil.SplitByCamelCase("REGValue")) {
			System.out.println(w);
		}
		System.out.println("==== split line ====");
		for (String w : ContentUtil.SplitByCamelCase("camelValue")) {
			System.out.println(w);
		}
		System.out.println("==== split line ====");
		for (String w : ContentUtil.SplitByCamelCase("Ha_sdValue")) {
			System.out.println(w);
		}
		System.out.println("==== split line ====");
		for (String w : "Ha_sdValue".split("_")) {
			System.out.println(w);
		}
	}

}
