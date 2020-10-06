package util;

import java.util.Iterator;
import java.util.List;

public class ListUtil {
	
	public static <T> boolean TwoListEachElementEqual(List<T> t1, List<T> t2) {
		if (t1 == null || t2 == null) {
			if (t1 == null && t2 == null) {
				return true;
			}
			return false;
		}
		if (t1.size() != t2.size()) {
			return false;
		}
		Iterator<T> itr1 = t1.iterator();
		Iterator<T> itr2 = t2.iterator();
		while (itr1.hasNext()) {
			T ele1 = itr1.next();
			T ele2 = itr2.next();
			if (!ele1.equals(ele2)) {
				return false;
			}
		}
		return true;
	}
	
}
