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
import main.MetaOfApp;
import tree.ExprSpecTreeNode;
import tree.TreeNode;
import util.MapUtil;
import util.PrintUtil;

public class TokenKindUtil {

	/**
	 * kind: -1, default value 1, approximate variable
	 */
	public final static int DefaultTokenKind = 0b0;
	public final static int SimpleTypeSimpleName = 0b01;
	public final static int QualifiedSimpleName = 0b010;
	public final static int MethodNameSimpleName = 0b0100;
	public final static int SimpleNameApproximateNotVariable = 0b01000;
	public final static int SimpleNameApproximateVariable = 0b010000;
	public final static int SimpleName = 0b0100000;
	public final static int NonLeafAtLeastTwoChildren = 0b01000000;
	public final static int NonLeafOnlyOneChild = 0b010000000;
	
	private final static String class_string_default = "#CLS_DFT#";
	private final static int class_string_trace_count = 2;
	private final static String non_exist = "#CLS_NON_EXIST#";
	private final static String non_leaf_at_least_two_children = "#NON_LEAF_AT_LEAST_TWO_CHILDREN#";
	private final static String non_leaf_only_one_child = "#NON_LEAF_ONLY_ONE_CHILD#";
	
	public final static Map<ConditionIndex, ConditionKindComputer> token_kind_map = new TreeMap<ConditionIndex, ConditionKindComputer>() {
		private static final long serialVersionUID = -6787015540770019187L;
		{
			put(new ConditionIndex("org.eclipse.jdt.core.dom.SimpleType org.eclipse.jdt.core.dom.SimpleName"), new ConditionKindComputer() {
				@Override
				public int ConditionToKind(ConditionDetail cond2) {
					return SimpleName | SimpleTypeSimpleName | SimpleNameApproximateNotVariable;
				}
			});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.ContinueStatement org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.ExpressionMethodReference org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.TypeParameter org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | SimpleTypeSimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.MarkerAnnotation org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.NormalAnnotation org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.MemberValuePair org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.QualifiedType org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | QualifiedSimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.QualifiedName org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | QualifiedSimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.MethodDeclaration org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.LabeledStatement org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.BreakStatement org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.ExpressionMethodReference org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							return SimpleName | SimpleNameApproximateNotVariable;
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.SwitchCase org.eclipse.jdt.core.dom.SimpleName"), new ConditionKindComputer() {
				@Override
				public int ConditionToKind(ConditionDetail cond2) {
					return SimpleName | SimpleNameApproximateNotVariable;
				}
			});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.MethodInvocation org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							Assert.isTrue(cond2 instanceof PositionRelatedConditionDetail);
							PositionRelatedConditionDetail prcd = (PositionRelatedConditionDetail) cond2;
							if (prcd.cond2 == 1) {
								return SimpleName | MethodNameSimpleName | SimpleNameApproximateNotVariable;
							} else {
								return SimpleName | SimpleNameApproximateVariable;
							}
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.SuperMethodInvocation org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							Assert.isTrue(cond2 instanceof PositionRelatedConditionDetail);
							PositionRelatedConditionDetail prcd = (PositionRelatedConditionDetail) cond2;
							if (prcd.cond2 == 0) {
								return SimpleName | SimpleTypeSimpleName | SimpleNameApproximateNotVariable;
							} else {
								if (prcd.cond2 == 1) {
									return SimpleName | MethodNameSimpleName | SimpleNameApproximateNotVariable;
								} else {
									return SimpleName | SimpleNameApproximateVariable;
								}
							}
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.SuperConstructorInvocation org.eclipse.jdt.core.dom.SimpleName"),
					new ConditionKindComputer() {
						@Override
						public int ConditionToKind(ConditionDetail cond2) {
							Assert.isTrue(cond2 instanceof PositionRelatedConditionDetail);
							PositionRelatedConditionDetail prcd = (PositionRelatedConditionDetail) cond2;
							if (prcd.cond2 == 0) {
								return SimpleName | SimpleTypeSimpleName | SimpleNameApproximateNotVariable;
							} else {
								return SimpleName | SimpleNameApproximateVariable;
							}
						}
					});
			put(new ConditionIndex("org.eclipse.jdt.core.dom.SimpleName"), new ConditionKindComputer() {
				@Override
				public int ConditionToKind(ConditionDetail cond2) {
					return SimpleName | SimpleNameApproximateVariable;
				}
			});
			put(new ConditionIndex(non_leaf_at_least_two_children), new ConditionKindComputer() {
				@Override
				public int ConditionToKind(ConditionDetail cond2) {
					return NonLeafAtLeastTwoChildren;
				}
			});
			put(new ConditionIndex(non_leaf_only_one_child), new ConditionKindComputer() {
				@Override
				public int ConditionToKind(ConditionDetail cond2) {
					return NonLeafOnlyOneChild;
				}
			});
		}
	};
	
	private static Condition GetNodeClassRepresentation(Object tn) {
		Assert.isTrue(tn instanceof ASTNode || tn instanceof TreeNode);
		Class<?> clz = GetClazz(tn);
		if (clz.equals(SimpleName.class)) {
			StringBuilder sb = new StringBuilder();
			int cond2 = -1;
			Object cu_tn = tn;
			for (int i = 0; i < class_string_trace_count; i++) {
				String cls_cnt = class_string_default;
				if (cu_tn != null) {
					cls_cnt = GetClazz(cu_tn).getName();
					Object par_tn = GetParent(cu_tn);
					if (par_tn != null) {
						ArrayList<Object> sibs = GetChildren(par_tn);
						int index = sibs.indexOf(cu_tn);
						Assert.isTrue(index > -1);
						if (IsExprSpecPattern(par_tn)) {
							if (!NodeHasExpression(par_tn)) {
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
			return new Condition(new SimpleNameConditionIndex(sb.toString().trim()), new PositionRelatedConditionDetail(cond2));
		} else {
//			if (!clz.equals(QualifiedName.class) && !clz.equals(QualifiedType.class) && !clz.equals(Block.class)) {
				int children_size = GetChildren(tn).size();
				if (children_size >= 2) {
					return new Condition(new ConditionIndex(non_leaf_at_least_two_children), null);
				} else {
					if (children_size > 0) {
						return new Condition(new ConditionIndex(non_leaf_only_one_child), null);
					}
				}
//			}
			return new Condition(new ConditionIndex(non_exist), null);
		}
	}
	
	private static Class<?> GetClazz(Object tn) {
		Assert.isTrue(tn instanceof ASTNode || tn instanceof TreeNode);
		if (tn instanceof ASTNode) {
			return ((ASTNode) tn).getClass();
		}
		if (tn instanceof TreeNode) {
			return ((TreeNode) tn).GetClazz();
		}
		Assert.isTrue(false);
		return null;
	}
	
	private static Object GetParent(Object tn) {
		Assert.isTrue(tn instanceof ASTNode || tn instanceof TreeNode);
		if (tn instanceof ASTNode) {
			ASTNode par_tn = ((ASTNode) tn).getParent();
			return par_tn;
		}
		if (tn instanceof TreeNode) {
			TreeNode par_tn = ((TreeNode) tn).GetParent();
			return par_tn;
		}
		Assert.isTrue(false);
		return null;
	}
	
	private static ArrayList<Object> GetChildren(Object par_tn) {
		Assert.isTrue(par_tn instanceof ASTNode || par_tn instanceof TreeNode);
		ArrayList<Object> result = new ArrayList<Object>();
		if (par_tn instanceof ASTNode) {
			ArrayList<ASTNode> sibs = JDTSearchForChildrenOfASTNode.GetChildren((ASTNode) par_tn);
			result.addAll(sibs);
		}
		if (par_tn instanceof TreeNode) {
			ArrayList<TreeNode> sibs = ((TreeNode) par_tn).GetChildren();
			result.addAll(sibs);
		}
		return result;
	}
	
	private static boolean IsExprSpecPattern(Object par_tn) {
		Assert.isTrue(par_tn instanceof ASTNode || par_tn instanceof TreeNode);
		if (par_tn instanceof TreeNode) {
			if (par_tn instanceof ExprSpecTreeNode) {
				return true;
			} else {
				return false;
			}
		}
		if (par_tn instanceof ASTNode) {
			if (JDTASTHelper.IsExprSpecPattern((ASTNode) par_tn)) {
				return true;
			} else {
				return false;
			}
		}
		Assert.isTrue(false);
		return false;
	}
	
	private static boolean NodeHasExpression(Object par_tn) {
		Assert.isTrue(par_tn instanceof ASTNode || par_tn instanceof TreeNode);
		if (par_tn instanceof TreeNode) {
			return (((ExprSpecTreeNode) par_tn).HasExpression());
		}
		if (par_tn instanceof ASTNode) {
			return JDTASTHelper.GetExprSpec((ASTNode) par_tn) != null;
		}
		Assert.isTrue(false);
		return false;
	}

	public static int GetTokenKind(TreeNode tn) {
		Condition cls_pet = GetNodeClassRepresentation(tn);
//		Class<?> cls = tn.GetClazz();
//		if (cls.equals(SimpleName.class)) {
//			System.err.println("is_var:" + (tn.GetBinding() != null) + "#cls_pet:" + cls_pet);
//		}
		return ContitionToKind(cls_pet);
	}

	private static Map<String, Integer> is_var_cls_pet_count = new TreeMap<String, Integer>();
	private static Map<String, Integer> is_not_var_cls_pet_count = new TreeMap<String, Integer>();
	static {
		if (MetaOfApp.PrintTokenKindDebugInfo) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					{
						TreeSet<String> iv_set = new TreeSet<String>(is_var_cls_pet_count.keySet());
						TreeSet<String> inv_set = new TreeSet<String>(is_not_var_cls_pet_count.keySet());
						iv_set.removeAll(inv_set);
						PrintUtil.PrintMap(is_var_cls_pet_count, iv_set, "absolute is_var_cls_pet_count", 100000000);
					}
					{
						TreeSet<String> iv_set = new TreeSet<String>(is_var_cls_pet_count.keySet());
						TreeSet<String> inv_set = new TreeSet<String>(is_not_var_cls_pet_count.keySet());
						inv_set.removeAll(iv_set);
						PrintUtil.PrintMap(is_not_var_cls_pet_count, inv_set, "absolute is_not_var_cls_pet_count",
								100000000);
					}
					{
						TreeSet<String> iv_set = new TreeSet<String>(is_var_cls_pet_count.keySet());
						TreeSet<String> inv_set = new TreeSet<String>(is_not_var_cls_pet_count.keySet());
						iv_set.retainAll(inv_set);
						PrintUtil.PrintMap(is_var_cls_pet_count, iv_set, "join is_var_cls_pet_count", 100000000);
						PrintUtil.PrintMap(is_not_var_cls_pet_count, iv_set, "join is_not_var_cls_pet_count",
								100000000);
					}
				}
			});
		}
	}

	public static int GetTokenKind(ASTNode tn) {
		Condition cls_pet = GetNodeClassRepresentation(tn);
		Class<?> cls = tn.getClass();
		if (cls.equals(SimpleName.class)) {
			IBinding bind = BindingResolveUtil.ResolveVariableBinding(tn);
//			System.err.println("is_var:" + (bind != null) + "#cls_pet:" + cls_pet);
			if (bind != null) {
				MapUtil.CountOneKey(is_var_cls_pet_count, cls_pet.toString(), 1);
//				ASTNode tn_par = tn.getParent();
//				if (tn_par instanceof MethodInvocation) {
//					MethodInvocation mi = (MethodInvocation) tn_par;
//					if (JDTSearchForChildrenOfASTNode.GetChildren(tn_par).indexOf(tn) == 1) {
//						System.err.println("==#== strange content :" + tn_par + "#mi.getExpression():" + mi.getExpression());
//					}
//				}
			} else {
				MapUtil.CountOneKey(is_not_var_cls_pet_count, cls_pet.toString(), 1);
			}
		}
		return ContitionToKind(cls_pet);
	}

	public static ArrayList<String> GenApproximateVarFromTokenKind(ArrayList<String> node_type_content_str,
			ArrayList<Integer> token_kind) {
		ArrayList<String> te_var_str = new ArrayList<String>();
		int index = -1;
		for (Integer tk : token_kind) {
			index++;
			if ((tk & MetaOfApp.ApproximateVarMode) > 0) {
				te_var_str.add(node_type_content_str.get(index));
			} else {
				te_var_str.add(null);
			}
		}
		return te_var_str;
	}

	private static int ContitionToKind(Condition cond) {
		ConditionKindComputer kind_computer = token_kind_map.get(cond.cond1);
		if (kind_computer == null) {
			kind_computer = token_kind_map.get(cond.cond1.GetSecondaryIndex());
		}
		if (kind_computer == null) {
			return DefaultTokenKind;
		} else {
			return kind_computer.ConditionToKind(cond.cond2);
		}
	}

}

abstract class ConditionKindComputer {

	public abstract int ConditionToKind(ConditionDetail cond2);

}

class Condition implements Comparable<Condition> {

	ConditionIndex cond1 = null;
	ConditionDetail cond2 = null;

	public Condition(ConditionIndex cond1, ConditionDetail cond2) {
		this.cond1 = cond1;
		this.cond2 = cond2;
	}

	@Override
	public int compareTo(Condition o) {
		return toString().compareTo(o.toString());
	}

	@Override
	public String toString() {
		return cond1.toString() + "#" + cond2.toString();
	}

}

class ConditionIndex implements Comparable<ConditionIndex> {
	
	String cond1 = null;
	
	public ConditionIndex(String cond1) {
		this.cond1 = cond1;
	}
	
	@Override
	public int compareTo(ConditionIndex o) {
		return toString().compareTo(o.toString());
	}
	
	@Override
	public String toString() {
		return cond1;
	}
	
	public ConditionIndex GetSecondaryIndex() {
		return new ConditionIndex(cond1);
	}
	
}

class SimpleNameConditionIndex extends ConditionIndex {
	
	public SimpleNameConditionIndex(String cond1) {
		super(cond1);
	}
	
	@Override
	public ConditionIndex GetSecondaryIndex() {
		String[] ss = cond1.split(" ");
		return new ConditionIndex(ss[ss.length - 1]);
	}
	
}

abstract class ConditionDetail {
}

class PositionRelatedConditionDetail extends ConditionDetail {

	int cond2 = -1;
	
	public PositionRelatedConditionDetail(int cond2) {
		this.cond2 = cond2;
	}
	
}









