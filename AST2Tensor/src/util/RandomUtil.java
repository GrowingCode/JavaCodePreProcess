package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class RandomUtil {

	public static int[] RandomCommon(int min, int max, int n) {
		if (n > (max - min) || max <= min) {
			return null;
		}
		int[] result = new int[n];
		for (int i=0;i<result.length;i++) {
			result[i] = -1;
		}
		int count = 0;
		while (count < n) {
			int num = (int) (Math.random() * (max - min)) + min;
			boolean flag = true;
			for (int j = 0; j < n; j++) {
				if (num == result[j]) {
					flag = false;
					break;
				}
			}
			if (flag) {
				result[count] = num;
				count++;
			}
		}
		return result;
	}
	
	public static <T> Collection<T> RandomFromCollection(Collection<T> collection, int number_of_select) {
		Collection<T> result = new HashSet<T>();
		ArrayList<T> ll = new ArrayList<T>(collection);
		int s = collection.size();
		int[] select = RandomCommon(0, s, number_of_select);
		Arrays.sort(select);
		for (int i=0;i<select.length;i++) {
			T t = ll.get(select[i]);
			result.add(t);
		}
		return result;
	}

}
