package translation.tensor;

public class TensorInfo {
	
	String path = null;
	String desc = null;
	
	public TensorInfo(String path, String desc) {
		this.path = path;
		this.desc = desc;
	}
	
	public String GetPath() {
		return path;
	}
	
	public String GetDesc() {
		return desc;
	}
	
}
