package translation.helper;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

public class TestDataDecodeType implements DecodeType {

	@Override
	public int GenerateDecodeType(ASTNode node) {
		if (node instanceof Statement) {
			return 1;
		}
		return 0;
	}

}
