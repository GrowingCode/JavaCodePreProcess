package translation.tensor;

public class StringTensor extends Tensor {
	
	String str = null;
	String debug_str = null;
	String oracle_str = null;
	
	public StringTensor(TensorInfo ti) {
		super(ti);
	}
	
	public void SetToString(String str) {
		this.str = str;
	}
	
	public void SetToDebugString(String debug_str) {
		this.debug_str = debug_str;
	}
	
	public void SetToOracleString(String oracle_str) {
		this.oracle_str = oracle_str;
	}
	
	@Override
	public String toString() {
		return str;
	}

	@Override
	public String toDebugString() {
		return debug_str;
	}

	@Override
	public String toOracleString() {
		return oracle_str;
	}
	
}
