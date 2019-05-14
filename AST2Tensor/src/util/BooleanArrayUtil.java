package util;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;

public class BooleanArrayUtil {
	
	/**
	 * the result is merged into the first parameter.
	 * @param longer
	 * @param shorter
	 */
	public static void BooleanArrayElementOr(ArrayList<Boolean> longer, ArrayList<Boolean> shorter) {
		int lsize = longer.size();
		int ssize = shorter.size();
		Assert.isTrue(lsize > ssize);
		
		for (int i=0;i<ssize;i++) {
			Boolean bs = shorter.get(i);
			Boolean bl = longer.get(i);
			longer.set(i, bs || bl);
		}
	}
	
}
