package statistic;

import statistic.ast.ChildrenNumCounter;
import statistic.id.APIRecorder;
import statistic.id.BPEMergeRecorder;
import statistic.id.TokenRecorder;
import translation.roles.RoleAssigner;

public class IDTools {
	
	public BPEMergeRecorder bpe_mr = null;
	public RoleAssigner role_assigner = null;
	public TokenRecorder tr = null;
	public TokenRecorder sr = null;
//	public TokenRecorder str = null;
//	public GrammarRecorder gr = null;
	public APIRecorder ar = null;
	public ChildrenNumCounter cnc = null;
	
//	TokenRecorder str, GrammarRecorder gr, 
	public IDTools(BPEMergeRecorder bpe_mr, RoleAssigner role_assigner, TokenRecorder tr, TokenRecorder sr, APIRecorder ar, ChildrenNumCounter cnc) {
		this.bpe_mr = bpe_mr;
		this.role_assigner = role_assigner;
		this.tr = tr;
		this.sr = sr;
//		this.str = str;
//		this.gr = gr;
		this.ar = ar;
		this.cnc = cnc;
	}
	
}
