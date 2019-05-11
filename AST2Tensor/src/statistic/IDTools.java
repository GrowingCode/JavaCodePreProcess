package statistic;

import statistic.ast.ChildrenNumCounter;
import statistic.id.APIRecorder;
import statistic.id.GrammarRecorder;
import statistic.id.TokenRecorder;
import translation.roles.RoleAssigner;

public class IDTools {
	
	public RoleAssigner role_assigner = null;
	public TokenRecorder tr = null;
	public GrammarRecorder gr = null;
	public APIRecorder ar = null;
	public ChildrenNumCounter cnc = null;
	
	public IDTools(RoleAssigner role_assigner, TokenRecorder tr, GrammarRecorder gr, APIRecorder ar, ChildrenNumCounter cnc) {
		this.role_assigner = role_assigner;
		this.tr = tr;
		this.gr = gr;
		this.ar = ar;
		this.cnc = cnc;
	}
	
}
