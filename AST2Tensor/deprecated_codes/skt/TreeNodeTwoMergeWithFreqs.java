package bpe.skt;

public class TreeNodeTwoMergeWithFreqs extends TreeNodeTwoMerge {
	
	int freqs = 0;
	
	public TreeNodeTwoMergeWithFreqs(TreeNodeTwoMerge tntm, int ifreqs) {
		super(tntm.node, tntm.node_index, tntm.parent, tntm.merged);
		this.freqs = ifreqs;
	}
	
	public int GetFreqs() {
		return freqs;
	}
	
}
