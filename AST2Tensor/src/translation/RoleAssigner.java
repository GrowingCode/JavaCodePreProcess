package translation;

public class RoleAssigner {
	
	int num = 0;
	
	public final static int all = 10;
	public final static int train = 7;
	public final static int valid = 8;
	public final static int test = 9;
	
	public final static String SpecifiedValidFilePrefix = "YYX_S_T_R_A_N_G_E_Specified_Valid_File__P_R_E_F_I_X_";
	public final static String SpecifiedTestFilePrefix = "YYX_S_T_R_A_N_G_E_Specified_Test_File__P_R_E_F_I_X_";
	
	public RoleAssigner() {
	}
	
	public int AssignRole(String file_name) {
		if (file_name.startsWith(SpecifiedValidFilePrefix)) {
			return 1;
		}
		if (file_name.startsWith(SpecifiedTestFilePrefix)) {
			return 2;
		}
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
