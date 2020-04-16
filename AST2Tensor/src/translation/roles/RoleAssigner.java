package translation.roles;

import java.util.HashMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;

public class RoleAssigner {
	
//	ModifiableInteger ast_num = new ModifiableInteger(0);
//	ModifiableInteger sequence_num = new ModifiableInteger(0);
	
	public static RoleAssigner instance = null;
	
	ModifiableInteger role_num = new ModifiableInteger(0);
	
	HashMap<String, Integer> path_roles = new HashMap<String, Integer>();
	HashMap<String, Integer> roles = new HashMap<String, Integer>();
	
	public final static int all = 5;
	public final static int train_seen = 2;
	public final static int train = 2;
	public final static int valid = 3;
	public final static int test = 4;
	
	public final static int train_seen_k = 0;
	public final static int train_k = 1;
	public final static int valid_k = 2;
	public final static int test_k = 3;
	
	public final static String SpecifiedTrainFilePrefix = "YYX_S_T_R_A_N_G_E_Specified_Train_File__P_R_E_F_I_X_";
	public final static String SpecifiedValidFilePrefix = "YYX_S_T_R_A_N_G_E_Specified_Valid_File__P_R_E_F_I_X_";
	public final static String SpecifiedTestFilePrefix = "YYX_S_T_R_A_N_G_E_Specified_Test_File__P_R_E_F_I_X_";
	
	private RoleAssigner() {
	}
	
	public static synchronized RoleAssigner GetInstance() {
		if (instance == null) {
			instance = new RoleAssigner();
		}
		return instance;
	}
	
//	public int AssignASTRole(File f) {
//		return AssignRole(f, ast_num);
//	}
	
//	public int AssignSequenceRole(File f) {
//		return AssignRole(f, sequence_num);
//	}
	
	public int GetRole(ICompilationUnit icu) {
		String icu_path = icu.getPath().toOSString();
		Integer rl = roles.get(icu_path);
		Assert.isTrue(rl != null);
//		Assert.isTrue(roles.containsKey(f_path));
		return rl;
	}
	
	public int AssignRole(String f_path, ICompilationUnit icu) {
//		String f_path = f.getAbsolutePath();
//		Assert.isTrue(!path_roles.containsKey(f_path), "already contained path:" + f_path);
		Integer raw_p_role = path_roles.get(f_path);
		Integer p_role = raw_p_role;
		if (p_role == null) {
//			return path_roles.get(f_path);
			p_role = AssignRole(f_path, role_num);
			path_roles.put(f_path, p_role);
		}
		
		String icu_path = icu.getPath().toOSString();
		Integer raw_role = roles.get(icu_path);
		Integer role = raw_role;
		if (role == null) {
			Assert.isTrue(raw_p_role == null);
			role = p_role;
			roles.put(icu_path, role);
		} else {
			Assert.isTrue((int)raw_role == (int)raw_p_role);
		}
//		Assert.isTrue(!roles.containsKey(icu_path));
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
