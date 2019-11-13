package util.visitor;

public class ElementInfo implements Comparable<ElementInfo> {

	int index = -1;
	String content = null;

	public ElementInfo(int index, String content) {
		this.index = index;
		this.content = content;
	}

	@Override
	public int compareTo(ElementInfo o) {
		return new Integer(index).compareTo(new Integer(o.index));
	}

	@Override
	public String toString() {
		return "index:" + index + "#content:" + content;
	}

}
