package translation.tensor.util;

public class TokenIndex {

	private int token_index = 0;

	public int NewIndex() {
		token_index++;
		return token_index;
	}

}

