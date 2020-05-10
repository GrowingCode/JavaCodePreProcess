package util.visitor;

public class ElementInfo implements Comparable<ElementInfo> {

	int index = -1;
	String content = null;
	int is_var = 0;

	public ElementInfo(int index, String content, int is_var) {
		this.index = index;
		this.content = content;
		this.is_var = is_var;
	}

	@Override
	public int compareTo(ElementInfo o) {
		return Integer.valueOf(index).compareTo(Integer.valueOf(o.index));
	}

	@Override
	public String toString() {
		return "index:" + index + "#content:" + content + "#is_var:" + is_var;
	}

}
