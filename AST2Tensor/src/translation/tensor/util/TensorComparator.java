package translation.tensor.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import translation.tensor.Tensor;
import util.PrintUtil;

public class TensorComparator implements java.util.Comparator<Tensor> {
	
	@Override
	public int compare(Tensor o1, Tensor o2) {
		return Integer.valueOf(o1.GetSize()).compareTo(Integer.valueOf(o2.GetSize()));
	}
	
	public static void main(String[] args) {
		System.out.println("Integer.valueOf(1).compareTo(2):" + Integer.valueOf(1).compareTo(2));
		ArrayList<Integer> ll = new ArrayList<Integer>();
		ll.add(0);
		ll.add(5);
		ll.add(2);
		ll.add(4);
		ll.add(3);
		ll.add(1);
		Collections.sort(ll, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		PrintUtil.PrintList(ll, "ll info:");
	}
	
}



