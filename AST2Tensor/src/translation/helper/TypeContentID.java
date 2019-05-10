package translation.helper;

public class TypeContentID {
	
//	String type = null;
//	String content = null;
//	int type_id = -1;
//	int content_id = -1;
	
	String type_content = null;
	int type_content_id = -1;
	
	public TypeContentID(String type_content, int type_content_id) {// String type, String content, int type_id, int content_id
//		this.type = type;
//		this.content = content;
//		this.type_id = type_id;
//		this.content_id = content_id;
		this.type_content = type_content;
		this.type_content_id = type_content_id;
	}
	
	public String GetTypeContent() {
		return type_content;
	}
	
	public int GetTypeContentID() {
		return type_content_id;
	}
	
//	public String GetType() {
//		return type;
//	}
//	
//	public String GetContent() {
//		return content;
//	}
//	
//	public int GetTypeID() {
//		return type_id;
//	}
//	
//	public int GetContentID() {
//		return content_id;
//	}
	
}
