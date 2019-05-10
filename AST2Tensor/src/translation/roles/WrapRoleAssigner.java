package translation.roles;

import java.io.File;

public abstract class WrapRoleAssigner {
	
	RoleAssigner ra = null;
	
	public WrapRoleAssigner(RoleAssigner ra) {
		this.ra = ra;
	}
	
	public abstract int AssignRole(File f);
	
}
