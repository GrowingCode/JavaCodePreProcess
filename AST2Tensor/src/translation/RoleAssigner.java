package translation;

public class RoleAssigner {
	
	int num = 0;
	
	public final static int all = 10;
	public final static int train = 7;
	public final static int valid = 8;
	public final static int test = 9;
	
	public RoleAssigner() {
	}
	
	public int AssignRole() {
		num++;
		if (num % all <= train) {
			return 0;
		} else if (num % all <= valid) {
			return 1;
		} else {
			return 2;
		}
	}
	
}
