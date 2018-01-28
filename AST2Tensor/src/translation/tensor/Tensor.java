package translation.tensor;

public abstract class Tensor {

	int role = -1;
	
	public Tensor(int role) {
		this.role = role;
	}
	
	public int GetRole() {
		return role;
	}
	
	@Override
	public abstract String toString();
	
	public abstract String toDebugString();
	
	public abstract String toOracleString();
	
}
