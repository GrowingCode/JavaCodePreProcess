package statistic;

import statistic.ast.ChildrenNumCounter;
import statistic.id.APIRecorder;
import statistic.id.BPEMergeRecorder;
import statistic.id.GrammarRecorder;
import statistic.id.TokenRecorder;

public class IDTools {
	
	public BPEMergeRecorder bpe_mr = null;
//	public SkeletonForestRecorder stf_r = null;
	public TokenRecorder tr = null;
//	public TokenRecorder sr = null;
//	public TokenRecorder one_struct_r = null;
//	public TokenRecorder pe_struct_r = null;
//	public TokenRecorder e_struct_r = null;
//	public TokenRecorder s_tr = null;
	public GrammarRecorder gr = null;
	public APIRecorder ar = null;
	public ChildrenNumCounter cnc = null;
//	public SktPEMergeRecorder sktpe_mr = null;
//	public ParentSktHintRecorder hint_recorder = null;
	
	// TokenRecorder sr, TokenRecorder one_struct_r, TokenRecorder pe_struct_r, TokenRecorder e_struct_r, TokenRecorder s_tr, , SktPEMergeRecorder sktpe_mr, ParentSktHintRecorder hint_recorder
	public IDTools(BPEMergeRecorder bpe_mr, TokenRecorder tr, GrammarRecorder gr, APIRecorder ar, ChildrenNumCounter cnc) {
		this.bpe_mr = bpe_mr;
		this.tr = tr;
//		this.sr = sr;
//		this.one_struct_r = one_struct_r;
//		this.pe_struct_r = pe_struct_r;
//		this.e_struct_r = e_struct_r;
//		this.s_tr = s_tr;
		this.gr = gr;
		this.ar = ar;
		this.cnc = cnc;
//		this.sktpe_mr = sktpe_mr;
//		this.hint_recorder = hint_recorder;
	}
	
}
