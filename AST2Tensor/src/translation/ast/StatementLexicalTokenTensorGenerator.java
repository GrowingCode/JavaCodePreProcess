package translation.ast;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import statis.trans.common.BasicGenerator;
import statis.trans.common.YTokenizer;
import statistic.id.IDManager;
import translation.helper.TypeContentID;
import translation.roles.RoleAssigner;
import translation.tensor.StatementInfo;
import translation.tensor.StatementTensor;
import translation.tensor.StringTensor;

public class StatementLexicalTokenTensorGenerator extends BasicGenerator {

	public StatementLexicalTokenTensorGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu,
			CompilationUnit cu) {
		super(role_assigner, im, icu, cu);
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
		StatementTensor curr_tensor = new StatementTensor();
		String content = node.toString();
		StatementInfo stmt_info = new StatementInfo(content);
		ArrayList<String> tks = YTokenizer.GetTokens(content);
		for (String tk : tks) {
			int type_content_id = im.GetTypeContentID(tk);
			TypeContentID t_c = new TypeContentID(tk, type_content_id);
			stmt_info.StoreOneNode(t_c, tk, null, null, -1, -1);
		}
		curr_tensor.Devour(stmt_info);
		curr_tensor.HandleAllDevoured(im);
		StringTensor st = new StringTensor();
		st.SetToString(curr_tensor.toString());
		st.SetToDebugString(curr_tensor.toDebugString());
		st.SetToOracleString(curr_tensor.toOracleString());
		st.SetSize(curr_tensor.getSize());
		st.SetRole(role_assigner.GetRole(icu.getPath().toOSString()));
		tensor_list.add(st);
	}
	
}
