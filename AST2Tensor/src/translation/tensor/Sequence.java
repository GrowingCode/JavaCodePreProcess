package translation.tensor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import translation.helper.TypeContentID;

public class Sequence {
	
	ArrayList<Integer> type_id = new ArrayList<Integer>();
	ArrayList<Integer> content_id = new ArrayList<Integer>();
	ArrayList<Integer> decode_type_id = new ArrayList<Integer>();
	
	ArrayList<String> type = new ArrayList<String>();
	ArrayList<String> content = new ArrayList<String>();
	
	public Sequence() {
	}
	
	public void AppendOneToken(TypeContentID type_content_id, int decode_type) {
		type_id.add(type_content_id.GetTypeID());
		content_id.add(type_content_id.GetContentID());
		decode_type_id.add(decode_type);
		type.add(type_content_id.GetType());
		content.add(type_content_id.GetContent());
	}
	
	@Override
	public String toString() {
		String result = StringUtils.join(type.toArray(), " ") + " " + StringUtils.join(content.toArray(), " ");
		return result.trim();
	}
	
}
