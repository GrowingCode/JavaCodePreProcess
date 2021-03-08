package statistic.id;

public class ParentSktHintRecorder {
	
	public TokenRecorder e_r = null;
	public TokenRecorder pe_r = null;
	public TokenRecorder one_r = null;
	
	public ParentSktHintRecorder(int capacity) {
		e_r = new TokenRecorder(capacity);
		pe_r = new TokenRecorder(capacity);
		one_r = new TokenRecorder(capacity);
	}
	
}
