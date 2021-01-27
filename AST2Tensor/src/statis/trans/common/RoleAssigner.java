package statis.trans.common;

import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;

import main.MetaOfApp;

public class RoleAssigner {
	
//	ModifiableInteger ast_num = new ModifiableInteger(0);
//	ModifiableInteger sequence_num = new ModifiableInteger(0);
	
	public static RoleAssigner instance = null;
	
	ModifiableInteger role_num = new ModifiableInteger(0);
	
	TreeMap<String, Integer> path_roles = new TreeMap<String, Integer>();
	TreeMap<String, Integer> roles = new TreeMap<String, Integer>();
	
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
		String icu_path = icu.getPath().toOSString();
		if (path_roles.containsKey(f_path)) {
			int ir = path_roles.get(f_path);
			int raw_r = roles.get(icu_path);
			Assert.isTrue(ir == raw_r);
			return path_roles.get(f_path);
		}
		Integer raw_p_role = path_roles.get(f_path);
		Integer p_role = raw_p_role;
		if (p_role == null) {
//			return path_roles.get(f_path);
			p_role = AssignRole(f_path, role_num);
			path_roles.put(f_path, p_role);
		}
		
//		Assert.isTrue(!roles.containsKey(icu_path), "already contained path:" + icu_path);
		Integer role = p_role;
		if (raw_p_role == null) {
//			Assert.isTrue(raw_p_role == null);
			role = p_role;
			roles.put(icu_path, role);
		} else {
			Assert.isTrue((int)role == (int)p_role);
		}
		return role;
	}
	
	private int AssignRole(String file_abs_path, ModifiableInteger m_num) {
//		String file_abs_path = f.getAbsolutePath();
		if (MetaOfApp.TrainValidTestFoldSeparationSpecified) {
			if (file_abs_path.contains(SpecifiedTrainFilePrefix)) {
				return train_seen_k;
			}
			if (file_abs_path.contains(SpecifiedValidFilePrefix)) {
				return valid_k;
			}
			if (file_abs_path.contains(SpecifiedTestFilePrefix)) {
				return test_k;
			}
		}
		int num = m_num.getValue();
		num++;
		m_num.setValue(num);
//		System.err.println("num:" + num);
		if ((num % MetaOfApp.all) <= MetaOfApp.train_seen) {
			return train_seen_k;
		} else if ((num % MetaOfApp.all) <= MetaOfApp.train) {
			return train_k;
		} else if ((num % MetaOfApp.all) <= MetaOfApp.valid) {
			return valid_k;
		} else {
			return test_k;
		}
		
	}
	
	public void ClearRoles() {
		role_num.Reset();
		path_roles.clear();
		roles.clear();
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
	
	public void Reset() {
		this.value = -1;
	}
	
}
