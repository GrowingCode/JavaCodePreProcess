package test.link;

import util.visitor.Range;

public class TImplement extends TInterface {

	@Override
	public int HaHaHello(Range r, int x) {
		int y = r.toString().length() * x;
		return y;
	}

}
