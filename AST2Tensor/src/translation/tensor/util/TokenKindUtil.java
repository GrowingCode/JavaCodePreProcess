package translation.tensor.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import eclipse.bind.BindingResolveUtil;
import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import tree.ExprSpecTreeNode;
import tree.TreeNode;
import util.MapUtil;
import util.PrintUtil;

public class TokenKindUtil {

	/**
	 * kind: 
	 *   0, default value
	 *   1, approximate variable
	 */
	public final static Map<String, ConditionKindComputer> token_kind_map = new TreeMap<String, ConditionKindComputer>() {
		private static final long serialVersionUID = -6787015540770019187L;
		{
			put("org.eclipse.jdt.core.dom.SimpleType org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.ContinueStatement org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.ExpressionMethodReference org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.TypeParameter org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.MarkerAnnotation org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.NormalAnnotation org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.MemberValuePair org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.QualifiedName org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.MethodDeclaration org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.LabeledStatement org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.BreakStatement org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.ExpressionMethodReference org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.SwitchCase org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 0;
				}
			});
			put("org.eclipse.jdt.core.dom.MethodInvocation org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					if (cond2 == 1) {
						return 0;
					} else {
						return 1;
					}
				}
			});
			put("org.eclipse.jdt.core.dom.SuperMethodInvocation org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					if (cond2 == 0) {
						return 0;
					} else {
						return 1;
					}
				}
			});
			put("org.eclipse.jdt.core.dom.SimpleName", new ConditionKindComputer() {				
				@Override
				public int ConditionToKind(int cond2) {
					return 1;
				}
			});
		}
	};
	private final static String class_string_default = "#CLS_DFT#";
	private final static int class_string_trace_count = 2;

	private static Condition GetNodeClassRepresentation(TreeNode tn) {
		StringBuilder sb = new StringBuilder();
		int cond2 = -1;
		TreeNode cu_tn = tn;
		for (int i = 0; i < class_string_trace_count; i++) {
			String cls_cnt = class_string_default;
			if (cu_tn != null) {
				cls_cnt = cu_tn.GetClazz().getName();
				TreeNode par_tn = cu_tn.GetParent();
				if (par_tn != null) {
					ArrayList<TreeNode> sibs = par_tn.GetChildren();
					int index = sibs.indexOf(cu_tn);
					Assert.isTrue(index > -1);
					if (par_tn instanceof ExprSpecTreeNode) {
						if (!((ExprSpecTreeNode) par_tn).HasExpression()) {
							index++;
						}
					}
					if (i == 0) {
						cond2 = index;
					}
					cu_tn = par_tn;
				}
			}
			sb.insert(0, cls_cnt).insert(0, " ");
		}
		return new Condition(sb.toString().trim(), cond2);
	}

	private static Condition GetNodeClassRepresentation(ASTNode tn) {
		StringBuilder sb = new StringBuilder();
		int cond2 = -1;
		ASTNode cu_tn = tn;
		for (int i = 0; i < class_string_trace_count; i++) {
			String cls_cnt = class_string_default;
			if (cu_tn != null) {
				cls_cnt = cu_tn.getClass().getName();
				ASTNode par_tn = cu_tn.getParent();
				if (par_tn != null) {
					ArrayList<ASTNode> sibs = JDTSearchForChildrenOfASTNode.GetChildren(par_tn);
					int index = sibs.indexOf(cu_tn);
					Assert.isTrue(index > -1);
					if (JDTASTHelper.IsExprSpecPattern(par_tn)) {
						if (JDTASTHelper.GetExprSpec(par_tn) == null) {
							index++;
						}
					}
					if (i == 0) {
						cond2 = index;
					}
					cu_tn = par_tn;
				}
			}
			sb.insert(0, cls_cnt).insert(0, " ");
		}
		return new Condition(sb.toString().trim(), cond2);
	}

	public static int GetTokenKind(TreeNode tn) {
		Condition cls_pet = GetNodeClassRepresentation(tn);
//		Class<?> cls = tn.GetClazz();
//		if (cls.equals(SimpleName.class)) {
//			System.err.println("is_var:" + (tn.GetBinding() != null) + "#cls_pet:" + cls_pet);
//		}
		ConditionKindComputer kind_computer = token_kind_map.get(cls_pet.cond1);
		if (kind_computer == null) {
			return 0;
		}
		int kind = kind_computer.ConditionToKind(cls_pet.cond2);
		return kind;
	}

	private static Map<Condition, Integer> is_var_cls_pet_count = new TreeMap<Condition, Integer>();
	private static Map<Condition, Integer> is_not_var_cls_pet_count = new TreeMap<Condition, Integer>();
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				{
					TreeSet<Condition> iv_set = new TreeSet<Condition>(is_var_cls_pet_count.keySet());
					TreeSet<Condition> inv_set = new TreeSet<Condition>(is_not_var_cls_pet_count.keySet());
					iv_set.removeAll(inv_set);
					PrintUtil.PrintMap(is_var_cls_pet_count, iv_set, "absolute is_var_cls_pet_count", 100000000);
				}
				{
					TreeSet<Condition> iv_set = new TreeSet<Condition>(is_var_cls_pet_count.keySet());
					TreeSet<Condition> inv_set = new TreeSet<Condition>(is_not_var_cls_pet_count.keySet());
					inv_set.removeAll(iv_set);
					PrintUtil.PrintMap(is_not_var_cls_pet_count, inv_set, "absolute is_not_var_cls_pet_count", 100000000);
				}
				{
					TreeSet<Condition> iv_set = new TreeSet<Condition>(is_var_cls_pet_count.keySet());
					TreeSet<Condition> inv_set = new TreeSet<Condition>(is_not_var_cls_pet_count.keySet());
					iv_set.retainAll(inv_set);
					PrintUtil.PrintMap(is_var_cls_pet_count, iv_set, "join is_var_cls_pet_count", 100000000);
					PrintUtil.PrintMap(is_not_var_cls_pet_count, iv_set, "join is_not_var_cls_pet_count", 100000000);
				}
			}
		});
	}

	public static int GetTokenKind(ASTNode tn) {
		Condition cls_pet = GetNodeClassRepresentation(tn);
		Class<?> cls = tn.getClass();
		if (cls.equals(SimpleName.class)) {
			IBinding bind = BindingResolveUtil.ResolveVariableBinding(tn);
//			System.err.println("is_var:" + (bind != null) + "#cls_pet:" + cls_pet);
			if (bind != null) {
				MapUtil.CountOneKey(is_var_cls_pet_count, cls_pet, 1);
//				ASTNode tn_par = tn.getParent();
//				if (tn_par instanceof MethodInvocation) {
//					MethodInvocation mi = (MethodInvocation) tn_par;
//					if (JDTSearchForChildrenOfASTNode.GetChildren(tn_par).indexOf(tn) == 1) {
//						System.err.println("==#== strange content :" + tn_par + "#mi.getExpression():" + mi.getExpression());
//					}
//				}
			} else {
				MapUtil.CountOneKey(is_not_var_cls_pet_count, cls_pet, 1);
			}
		}
		ConditionKindComputer kind_computer = token_kind_map.get(cls_pet.cond1);
		if (kind_computer == null) {
			return 0;
		}
		int kind = kind_computer.ConditionToKind(cls_pet.cond2);
		return kind;
	}

}

abstract class ConditionKindComputer {
	
	public abstract int ConditionToKind(int cond2);
	
}

class Condition implements Comparable<Condition> {
	
	String cond1 = null;
	int cond2 = -1;
	
	public Condition(String cond1, int cond2) {
		this.cond1 = cond1;
		this.cond2 = cond2;
	}

	@Override
	public int compareTo(Condition o) {
		return toString().compareTo(o.toString());
	}
	
	@Override
	public String toString() {
		return cond1 + "#" + cond2;
	}
	
}









