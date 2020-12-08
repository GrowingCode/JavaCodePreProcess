package translation.ast;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import statis.trans.common.BasicGenerator;
import statis.trans.common.RoleAssigner;
import statis.trans.common.YTokenizer;
import statistic.id.IDManager;
import translation.helper.TypeContentID;
import translation.tensor.StatementInfo;
import translation.tensor.StatementTensor;
import translation.tensor.StringTensor;
import translation.tensor.TensorInfo;

public class StatementLexicalTokenTensorGenerator extends BasicGenerator {
	
	public int min_num_node_in_one_ast = Integer.MAX_VALUE;
	public int max_num_node_in_one_ast = Integer.MIN_VALUE;
	
//	RoleAssigner role_assigner, 
	public StatementLexicalTokenTensorGenerator(IDManager im, ICompilationUnit icu,
			CompilationUnit cu) {
//		role_assigner, 
		super(im, icu, cu);
	}

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (begin_generation && begin_generation_node.equals(node)) {
			// do nothing.
		}
	}
	
	@Override
	protected void WholePostHandle(ASTNode node) {
		Assert.isTrue(node instanceof MethodDeclaration);
		TensorInfo tinfo = new TensorInfo(icu.getPath().toOSString(), ((MethodDeclaration)node).getName().toString());
		StatementTensor curr_tensor = new StatementTensor(tinfo);
		String content = node.toString();
		StatementInfo stmt_info = new StatementInfo(content);
		ArrayList<String> tks = YTokenizer.GetTokens(content);
		for (String tk : tks) {
			int type_content_id = im.GetTypeContentID(tk);
			TypeContentID t_c = new TypeContentID(tk, type_content_id);
			stmt_info.StoreOneNode(t_c, tk, null, null, -1, -1);
		}
		min_num_node_in_one_ast = min_num_node_in_one_ast > stmt_info.Size() ? stmt_info.Size() : min_num_node_in_one_ast;
		max_num_node_in_one_ast = max_num_node_in_one_ast < stmt_info.Size() ? stmt_info.Size() : max_num_node_in_one_ast;
		curr_tensor.Devour(stmt_info);
		curr_tensor.HandleAllDevoured(im);
		StringTensor st = new StringTensor(tinfo);
		st.SetToString(curr_tensor.toString());
		st.SetToDebugString(curr_tensor.toDebugString());
		st.SetToOracleString(curr_tensor.toOracleString());
		st.SetSize(curr_tensor.GetSize());
		st.SetRole(RoleAssigner.GetInstance().GetRole(icu));
		tensor_list.add(st);
	}
	
}
