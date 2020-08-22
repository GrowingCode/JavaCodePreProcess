package test;

import java.util.Arrays;
import java.util.List;

public class TestArraySearch {
	
	public static void main(String[] args) {
		Class<?>[] ccs = new Class<?>[] {String.class, Integer.class};
		List<Class<?>> ccsl = Arrays.asList(ccs);
//		int position = Arrays.binarySearch(ccs, String.class);
		int position = ccsl.indexOf(String.class);
		System.out.println("exist position:" + position);
//		int position2 = Arrays.binarySearch(ccs, Boolean.class);
		int position2 = ccsl.indexOf(Boolean.class);
		System.out.println("not exist position:" + position2);
	}
	
}
