package translation.roles;

import java.util.HashMap;

public class RoleAssigner {
	
//	ModifiableInteger ast_num = new ModifiableInteger(0);
//	ModifiableInteger sequence_num = new ModifiableInteger(0);
	
	ModifiableInteger role_num = new ModifiableInteger(0);
	
	HashMap<String, Integer> roles = new HashMap<String, Integer>();
	
	public final static int all = 20;
	public final static int train_seen = 12;
	public final static int train = 12;
	public final static int valid = 12;
	public final static int test = 19;
	
	public final static int train_seen_k = 0;
	public final static int train_k = 1;
	public final static int valid_k = 2;
	public final static int test_k = 3;
	
	public final static String SpecifiedTrainFilePrefix = "YYX_S_T_R_A_N_G_E_Specified_Train_File__P_R_E_F_I_X_";
	public final static String SpecifiedValidFilePrefix = "YYX_S_T_R_A_N_G_E_Specified_Valid_File__P_R_E_F_I_X_";
	public final static String SpecifiedTestFilePrefix = "YYX_S_T_R_A_N_G_E_Specified_Test_File__P_R_E_F_I_X_";
	
	public RoleAssigner() {
	}
	
//	public int AssignASTRole(File f) {
//		return AssignRole(f, ast_num);
//	}
	
//	public int AssignSequenceRole(File f) {
//		return AssignRole(f, sequence_num);
//	}
	
	public int AssignRole(String f_path) {
//		String f_path = f.getAbsolutePath();
		if (roles.containsKey(f_path)) {
			return roles.get(f_path);
		}
		int role = AssignRole(f_path, role_num);
		roles.put(f_path, role);
		return role;
	}
	
	private int AssignRole(String file_abs_path, ModifiableInteger m_num) {
//		String file_abs_path = f.getAbsolutePath();
		if (file_abs_path.contains(SpecifiedTrainFilePrefix)) {
			return train_seen_k;
		}
		if (file_abs_path.contains(SpecifiedValidFilePrefix)) {
			return valid_k;
		}
		if (file_abs_path.contains(SpecifiedTestFilePrefix)) {
			return test_k;
		}
		int num = m_num.getValue();
		num++;
		m_num.setValue(num);
//		System.err.println("num:" + num);
		if (num % all <= train_seen) {
			return train_seen_k;
		} else if (num % all <= train) {
			return train_k;
		} else if (num % all <= valid) {
			return valid_k;
		} else {
			return test_k;
		}
	}
	
}

class ModifiableInteger {
	
	private int value = -1;
	
	public ModifiableInteger(int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
