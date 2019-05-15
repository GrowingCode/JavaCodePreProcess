package test;

import java.util.LinkedList;
import java.util.List;

public class TestSubList {
	
	public static void main(String[] args) {
		List<String> ll = new LinkedList<String>();
		ll.add("1");
		ll.add("2");
		ll.add("3");
		List<String> ll_s = ll.subList(0, 0);
		System.out.println("==== begin ====");
		for (String s : ll_s) {
			System.out.println(s);
		}
		System.out.println("==== end ====");
	}
	
}
