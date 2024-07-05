package tree;

import statistic.id.IDManager;
import translation.tensor.StringTensor;
import translation.tensor.TensorInfo;

public abstract class TreeVisitor {
	
	protected IDManager im = null;
	
	public TreeVisitor(IDManager im) {
		this.im = im;
	}
	
	public abstract boolean PreVisit(TreeNode node);
	public abstract void PostVisit(TreeNode node);
	public abstract StringTensor GetStringTensor();
	public abstract void ClearAndInitialize(TensorInfo ti);
	
}
