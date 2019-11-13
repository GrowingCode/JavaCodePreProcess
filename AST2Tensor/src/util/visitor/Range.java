package util.visitor;

public class Range {

	ElementInfo ei = null;
	int buf_start = 0;
	int buf_end = -1;
	OperationKind kind = null;

	public Range(ElementInfo ei, int buf_start, int buf_end, OperationKind kind) {
		this.ei = ei;
		this.buf_start = buf_start;
		this.buf_end = buf_end;
		this.kind = kind;
	}

}
