package test.source;

public class TT {
	
	public static void main(String[] args) {
		String a = "\"LUCENE-7604: For some unknown reason the non-constant BadQuery#hashCode() does not trigger ConcurrentModificationException on Java 9 b150\"";
		a = a.replaceAll("#h", "\\$h");
		System.out.println(a);
	}
	
}
