package unit.util;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;

import unit.PairContainer;

public class PairContainerUtil {
	
	public static <K, V> PairContainer<K, V> RemovePairContainerFromListAccordingToKeyValue(ArrayList<PairContainer<K, V>> pcnps, K k, V v) {
		PairContainer<K, V> res = null;
		for (PairContainer<K, V> pcnp : pcnps) {
			if (pcnp.k.equals(k) && pcnp.v.equals(v)) {
				res = pcnp;
			}
		}
		if (res != null) {
			boolean b = pcnps.remove(res);
			Assert.isTrue(b);
		}
		return res;
	}
	
}
