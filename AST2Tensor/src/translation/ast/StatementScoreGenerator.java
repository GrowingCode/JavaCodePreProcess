package translation.ast;

import org.eclipse.core.runtime.Assert;

import eclipse.jdt.JDTASTHelper;
import statistic.id.IDManager;
import translation.tensor.StringTensor;
import translation.tensor.TensorInfo;
import tree.TreeNode;
import tree.TreeVisitor;

public class StatementScoreGenerator extends TreeVisitor {
	
//	int skt_all_num = 0;
//	int skt_unk_num = 0;
	int token_all_num = 0;
	int token_unk_num = 0;
	
	public StatementScoreGenerator(IDManager im) {
		super(im);
	}

	@Override
	public boolean PreVisit(TreeNode node) {
		boolean is_id_leaf = JDTASTHelper.IsIDLeafNode(node.GetClazz());
		if (is_id_leaf) {
			token_all_num++;
			int id = im.GetSkeletonTypeContentID(node.GetContent());
			int unk_id = im.GetSkeletonTypeContentID(IDManager.Unk);
			Assert.isTrue(unk_id == 1);
			if (id == unk_id) {
				token_unk_num++;
			}
		}
		return true;
	}
	
	public double GetScore() {
		Assert.isTrue(token_all_num > 0);
		double valid_rate = (token_all_num - token_unk_num) / (token_all_num * 1.0);
		return valid_rate;
	}

	@Override
	public void PostVisit(TreeNode node) {
	}

	@Override
	public StringTensor GetStringTensor() {
		return null;
	}

	@Override
	public void ClearAndInitialize(TensorInfo ti) {
	}
	
}
