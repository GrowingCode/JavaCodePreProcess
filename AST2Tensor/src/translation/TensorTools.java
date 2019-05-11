package translation;

import statistic.id.IDManager;
import translation.roles.RoleAssigner;

public class TensorTools {
	
	public RoleAssigner role_assigner = null;
	public IDManager im = null;
	
	public TensorTools(RoleAssigner role_assigner, IDManager im) {
		this.role_assigner = role_assigner;
		this.im = im;
	}
	
}
