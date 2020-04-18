package translation.tensor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class SequenceTensor extends Tensor {

	ArrayList<String> node_type_content_str = new ArrayList<String>();
	ArrayList<Integer> node_type_content_en = new ArrayList<Integer>();
	
	public void AppendOneToken(String token, int token_en) {
		node_type_content_str.add(token);
		node_type_content_en.add(token_en);
	}
	
	@Override
	public int getSize() {
		return node_type_content_en.size();
	}

	@Override
	public String toString() {
		return StringUtils.join(node_type_content_en.toArray(), " ");
	}

	@Override
	public String toDebugString() {
		return StringUtils.join(node_type_content_en.toArray(), " ");
	}

	@Override
	public String toOracleString() {
		return StringUtils.join(node_type_content_str.toArray(), " ");
	}

}
