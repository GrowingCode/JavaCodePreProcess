package eclipse;

public class ResourceMeta {
	
	public static final String ProjectDependencyDirectory = "YYX_d_e_p_d_d_i_r";
	
	public static final String DependencyLog = "dependency_log.txt";
	
	public static String GetAbsolutePathOfProjectDependencyDirectory() {
		return System.getProperty("user.home") + "/" + ProjectDependencyDirectory;
	}
}
