package test;

public class UnderScoreTest {
	
	public static void main(String[] args) {
		for (String w : "asd__a_a____s".split("_+")) {
			System.out.println(w);
		}
	}
	
}
