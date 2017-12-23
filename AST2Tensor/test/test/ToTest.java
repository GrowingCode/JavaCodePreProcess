package test;

import java.util.List;

public class ToTest {

	int x = 0;

	public ToTest(int x) {
		this.x = x;
	}

	public void HaTest(List<CharSequence> stack, CharSequence encoded) {
		int encodedLen = encoded.length();
		int idx = 0;
		char c = '\0';

		// Read all of the characters for this node.
		for (; idx < encodedLen; idx++) {
			c = encoded.charAt(idx);
			if (c == '&' || c == '?' || c == '!' || c == ':' || c == ',') {
				break;
			}
		}
		stack.add(0, reverse(encoded.subSequence(0, idx)));
	}

	private CharSequence reverse(CharSequence s) {
		return new StringBuilder(s).reverse();
	}

}
