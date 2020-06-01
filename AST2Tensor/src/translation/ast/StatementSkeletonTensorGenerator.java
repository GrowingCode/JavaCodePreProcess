package translation.ast;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import eclipse.jdt.JDTASTHelper;
import statis.trans.common.BasicGenerator;
import statis.trans.common.RoleAssigner;
import statistic.id.IDManager;
import statistic.id.PreProcessContentHelper;
import translation.tensor.StatementSkeletonTensor;
import translation.tensor.StringTensor;
import translation.tensor.TensorInfo;
import util.visitor.TokenHandleSkeletonVisitor;

public class StatementSkeletonTensorGenerator extends BasicGenerator {
	
	ArrayList<ASTNode> stmt_roots = new ArrayList<ASTNode>();
	
	public StatementSkeletonTensorGenerator(IDManager im, ICompilationUnit icu,
			CompilationUnit cu) {
		super(im, icu, cu);
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
		Assert.isTrue(node instanceof MethodDeclaration);
		TensorInfo tinfo = new TensorInfo(icu.getPath().toOSString(), ((MethodDeclaration)node).getName().toString());
		StatementSkeletonTensor curr_tensor = new StatementSkeletonTensor(tinfo);
		for (ASTNode stmt_root : stmt_roots) {
			TokenHandleSkeletonVisitor sv = new TokenHandleSkeletonVisitor(icu);
			stmt_root.accept(sv);
			ArrayList<String> lls = sv.GetResult();
			ArrayList<Integer> kinds = sv.GetTokenKind();
			ArrayList<Integer> is_var_lls = sv.GetIsVarResult();
			ArrayList<Integer> ids = new ArrayList<Integer>();
			int sk_id = im.GetSkeletonID(lls.get(0));
			ids.add(sk_id);
			for (int i=1;i<lls.size();i++) {
				String pp_tk = PreProcessContentHelper.PreProcessTypeContent(lls.get(i));
				int tk_id = im.GetTypeContentID(pp_tk);
				ids.add(tk_id);
			}
			curr_tensor.StoreStatementSkeletonInfo(JDTASTHelper.GetSimplifiedSignatureForMethodDeclaration(stmt_roots.get(0)), lls, ids, kinds, is_var_lls);
		}
		try {
			curr_tensor.HandleAllInfo();
		} catch (Exception e) {
			try {
				IResource resource = icu.getUnderlyingResource();
				Assert.isTrue(resource.getType() == IResource.FILE);
				IFile ifile = (IFile) resource;
				String path = ifile.getRawLocation().toString();
				System.err.println("Wrong file:" + path);
			} catch (JavaModelException e1) {
				e1.printStackTrace();
			}
			throw e;
		}
		StringTensor st = new StringTensor(tinfo);
		st.SetToString(curr_tensor.toString());
		st.SetToDebugString(curr_tensor.toDebugString());
		st.SetToOracleString(curr_tensor.toOracleString());
		st.SetSize(curr_tensor.getSize());
		st.SetRole(RoleAssigner.GetInstance().GetRole(icu));
		tensor_list.add(st);
	}
	
	@Override
	protected void WholePostClear(ASTNode node) {
		stmt_roots.clear();
	}
	
}
