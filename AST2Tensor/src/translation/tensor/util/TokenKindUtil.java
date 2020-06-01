package translation.tensor.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;

import eclipse.bind.BindingResolveUtil;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import tree.TreeNode;

public class TokenKindUtil {
	
	public final static Map<String, Integer> token_kind_map = new HashMap<String, Integer>()
	{
		private static final long serialVersionUID = -6787015540770019187L;
		{
			// TODO update here for detailed info
			put(SimpleName.class.toString(), 1);
			put(SimpleType.class.toString() + " " + SimpleName.class.toString(), 2);
		}
	};
	private final static String class_string_default = "#CLS_DFT#";
	private final static int class_string_trace_count = 3;
	
	private static String GetNodeClassRepresentation(TreeNode tn) {
		StringBuilder sb = new StringBuilder();
		TreeNode cu_tn = tn;
		for (int i=0; i<class_string_trace_count; i++) {
			String cls_cnt = class_string_default;
			if (cu_tn != null) {
				TreeNode par_tn = cu_tn.GetParent();
				if (par_tn != null) {
					ArrayList<TreeNode> sibs = par_tn.GetChildren();
					int index = sibs.indexOf(cu_tn);
					Assert.isTrue(index > -1);
					cls_cnt = cu_tn.getClass().toString() + "#" + index;
					cu_tn = par_tn;
				} else {
					cls_cnt = cu_tn.getClass().toString() + "#" + -1;
				}
			}
			sb.insert(0, cls_cnt).insert(0, " ");
		}
		return sb.toString().trim();
	}
	
	private static String GetNodeClassRepresentation(ASTNode tn) {
		StringBuilder sb = new StringBuilder();
		ASTNode cu_tn = tn;
		for (int i=0; i<class_string_trace_count; i++) {
			String cls_cnt = class_string_default;
			if (cu_tn != null) {
				ASTNode par_tn = cu_tn.getParent();
				if (par_tn != null) {
					ArrayList<ASTNode> sibs = JDTSearchForChildrenOfASTNode.GetChildren(par_tn);
					int index = sibs.indexOf(cu_tn);
					Assert.isTrue(index > -1);
					cls_cnt = cu_tn.getClass().toString() + "#" + index;
					cu_tn = par_tn;
				} else {
					cls_cnt = cu_tn.getClass().toString() + "#" + -1;
				}
			}
			sb.insert(0, cls_cnt).insert(0, " ");
		}
		return sb.toString().trim();
	}
	
	public static int GetTokenKind(TreeNode tn) {
		String cls_pet = GetNodeClassRepresentation(tn);
		Class<?> cls = tn.GetClazz();
		if (cls.equals(SimpleName.class)) {
			System.err.println("is_var:" + (tn.GetBinding() != null) + "#cls_pet:" + cls_pet);
		}
		Integer kind = token_kind_map.get(cls_pet);
		if (kind == null) {
			return 0;
		}
		return kind;
	}
	
	public static int GetTokenKind(ASTNode tn) {
		String cls_pet = GetNodeClassRepresentation(tn);
		Class<?> cls = tn.getClass();
		if (cls.equals(SimpleName.class)) {
			IBinding bind = BindingResolveUtil.ResolveVariableBinding(tn);
			System.err.println("is_var:" + (bind != null) + "#cls_pet:" + cls_pet);
		}
		Integer kind = token_kind_map.get(cls_pet);
		if (kind == null) {
			return 0;
		}
		return kind;
	}
	
}
