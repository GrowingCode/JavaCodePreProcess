package translation.ast;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import statis.trans.common.BasicGenerator;
import statistic.id.IDManager;
import statistic.id.PreProcessContentHelper;
import translation.roles.RoleAssigner;
import translation.tensor.StatementSkeletonTensor;
import translation.tensor.StringTensor;

public class StatementSkeletonTensorGenerator extends BasicGenerator {
	
	StatementSkeletonTensor curr_tensor = new StatementSkeletonTensor();
	ArrayList<ASTNode> stmt_roots = new ArrayList<ASTNode>();
	
	public StatementSkeletonTensorGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu,
			CompilationUnit cu) {
		super(role_assigner, im, icu, cu);
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (begin_generation) {
			if (StatementUtil.IsStatement(node.getClass()) || StatementUtil.IsMethodDeclaration(node.getClass())) {
				stmt_roots.add(node);
			}
		}
	}

	@Override
	protected void WholePostHandle(ASTNode node) {
		for (ASTNode stmt_root : stmt_roots) {
			ArrayList<String> lls = StatementUtil.ProcessSkeleton(icu, stmt_root);
			ArrayList<Integer> ids = new ArrayList<Integer>();
			int sk_id = im.GetSkeletonID(lls.get(0));
			ids.add(sk_id);
			for (int i=1;i<lls.size();i++) {
				String pp_tk = PreProcessContentHelper.PreProcessTypeContent(lls.get(i));
				int tk_id = im.GetTypeContentID(pp_tk);
//				int tk_id = im.GetTypeContentID(lls.get(i));
				ids.add(tk_id);
			}
			curr_tensor.StoreStatementSkeletonInfo(ids);
		}
		
		StringTensor st = new StringTensor();
		st.SetToString(curr_tensor.toString());
		st.SetToDebugString(curr_tensor.toDebugString());
		st.SetToOracleString(curr_tensor.toOracleString());
		st.SetSize(curr_tensor.getSize());
		if (st != null) {
			st.SetRole(role_assigner.GetRole(icu.getPath().toOSString()));
			tensor_list.add(st);
		}
	}
	
	@Override
	protected void WholePostClear(ASTNode node) {
		curr_tensor = new StatementSkeletonTensor();
		stmt_roots.clear();
	}
	
}
