package statistic;

import statis.trans.common.SkeletonForestRecorder;
import statistic.ast.ChildrenNumCounter;
import statistic.id.APIRecorder;
import statistic.id.BPEMergeRecorder;
import statistic.id.GrammarRecorder;
import statistic.id.TokenRecorder;

public class IDTools {
	
	public BPEMergeRecorder bpe_mr = null;
	public SkeletonForestRecorder stf_r = null;
	public TokenRecorder tr = null;
	public TokenRecorder sr = null;
	public TokenRecorder str = null;
	public GrammarRecorder gr = null;
	public APIRecorder ar = null;
	public ChildrenNumCounter cnc = null;
	
	public IDTools(BPEMergeRecorder bpe_mr, SkeletonForestRecorder stf_r, TokenRecorder tr, TokenRecorder sr, TokenRecorder str, GrammarRecorder gr, APIRecorder ar, ChildrenNumCounter cnc) {
		this.bpe_mr = bpe_mr;
		this.stf_r = stf_r;
		this.tr = tr;
		this.sr = sr;
		this.str = str;
		this.gr = gr;
		this.ar = ar;
		this.cnc = cnc;
	}
	
}
