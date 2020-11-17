package test;

public class TestStringFormat {
	
	public static void main(String[] args) {
		String a = String.format("%2s", "aaaaaaa");
		a = a.substring(0, 2);
		System.out.println("a:" + a);
	}
	
}
