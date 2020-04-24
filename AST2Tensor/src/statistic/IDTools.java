package statistic;

import statistic.ast.ChildrenNumCounter;
import statistic.id.APIRecorder;
import statistic.id.BPEMergeRecorder;
import statistic.id.GrammarRecorder;
import statistic.id.TokenRecorder;

public class IDTools {
	
	public BPEMergeRecorder bpe_mr = null;
//	public RoleAssigner role_assigner = null;
	public TokenRecorder tr = null;
	public TokenRecorder sr = null;
//	public TokenRecorder str = null;
	public GrammarRecorder gr = null;
	public APIRecorder ar = null;
	public ChildrenNumCounter cnc = null;
	
//	TokenRecorder str, RoleAssigner role_assigner, 
	public IDTools(BPEMergeRecorder bpe_mr, TokenRecorder tr, TokenRecorder sr, GrammarRecorder gr, APIRecorder ar, ChildrenNumCounter cnc) {
		this.bpe_mr = bpe_mr;
//		this.role_assigner = role_assigner;
		this.tr = tr;
		this.sr = sr;
//		this.str = str;
		this.gr = gr;
		this.ar = ar;
		this.cnc = cnc;
	}
	
}
