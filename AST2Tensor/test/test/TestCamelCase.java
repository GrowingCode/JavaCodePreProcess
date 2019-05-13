package test;

public class TestCamelCase {
	
	private static final String reg = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
	
	public static String[] split(String cnt) {
		return cnt.split(reg);
	}

	public static void main(String[] args) {
		for (String w : TestCamelCase.split("REGValue")) {
			System.out.println(w);
		}
		System.out.println("==== split line ====");
		for (String w : TestCamelCase.split("camelValue")) {
			System.out.println(w);
		}
		System.out.println("==== split line ====");
		for (String w : TestCamelCase.split("Ha_sdValue")) {
			System.out.println(w);
		}
		System.out.println("==== split line ====");
		for (String w : "Ha_sdValue".split("_")) {
			System.out.println(w);
		}
	}

}
