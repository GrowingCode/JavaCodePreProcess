package test;

import java.util.ArrayList;

public class TestGenericIntArray {
	
	public static void main(String[] args) {
		ArrayList<int[][]> arr = new ArrayList<int[][]>();
		int[][] a = new int[2][3];
		arr.add(a);
		System.out.println(arr);
		// Java can not fill multiple-dimension
		// Arrays.fill(a, -1);
	}
	
}
