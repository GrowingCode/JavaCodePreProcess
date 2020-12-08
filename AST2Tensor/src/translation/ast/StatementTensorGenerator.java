package translation.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.runtime.Assert;

import statistic.id.IDManager;
import translation.helper.TypeContentID;
import translation.tensor.StatementInfo;
import translation.tensor.StatementTensor;
import translation.tensor.StringTensor;
import translation.tensor.TensorInfo;
import tree.TreeNode;
import tree.TreeVisitor;

public class StatementTensorGenerator extends TreeVisitor {
	
	public StatementTensorGenerator(IDManager im) {
		super(im);
	}
	
	Stack<TreeNode> in_handling_node = new Stack<TreeNode>();
	Stack<StatementInfo> in_handling_tensor = new Stack<StatementInfo>();

	LinkedList<TreeNode> pre_order_node = new LinkedList<TreeNode>();
	Map<TreeNode, StatementInfo> node_stmt = new HashMap<TreeNode, StatementInfo>();
	
	TensorInfo ti = null;
	
	@Override
	public boolean PreVisit(TreeNode node) {
		if (StatementUtil.IsStatement(node.GetClazz()) || StatementUtil.IsMethodDeclaration(node.GetClazz())) {
			in_handling_node.add(node);
			StatementInfo stmt_info = new StatementInfo(node.GetTreeWholeContent());
			in_handling_tensor.add(stmt_info);
			pre_order_node.add(node);
			node_stmt.put(node, stmt_info);
		}
		String var = null;
		int type_content_id = im.GetTypeContentID(node.GetContent());
		TypeContentID tid = new TypeContentID(node.GetContent(), type_content_id);
		ArrayList<TreeNode> children = node.GetChildren();
		boolean is_leaf = children.size() == 0;
		if (!is_leaf) {
//			StatementInfo stmt = in_handling_tensor.peek();
//			stmt.StoreOneNode(tid, null, -1, -1);
		} else {
//			int api_comb_id = -1;
//			int api_relative_id = -1;
			if (node.GetBinding() != null) {
//				SimpleName sn = (SimpleName) node;
//				var_index = HandleVariableIndex(sn.toString());
//				var = sn.toString();
				var = node.GetContent();
//				System.err.println("simple_name:" + sn);
//				IBinding ib = sn.resolveBinding();
//				if (ib != null) {
//					if (ib instanceof IVariableBinding) {
////						IVariableBinding ivb = (IVariableBinding) ib;
////						String ivb_key = "var!" + ivb.getVariableId() + "#" + sn;
////						var_index = HandleVariableIndex(ivb_key)
//					} else if (ib instanceof ITypeBinding) {
////						ITypeBinding itb = (ITypeBinding) ib;
////						String itb_key = "type!" + itb.getKey() + "#" + sn;
////						var_index = HandleVariableIndex(itb_key);
//					} else if (ib instanceof IMethodBinding) {
//						if (!(sn.getParent() instanceof MethodDeclaration)
//								&& (sn.getParent() instanceof MethodInvocation)) {
//							IMethodBinding imb = (IMethodBinding) ib;
//							ITypeBinding dc = imb.getDeclaringClass();
//							IMethodBinding[] mds = dc.getDeclaredMethods();
//							LinkedList<String> mdnames = new LinkedList<String>();
//							for (IMethodBinding md : mds) {
//								String mdname = md.getName();
//								mdname = PreProcessContentHelper.PreProcessTypeContent(mdname);
//								if (!mdnames.contains(mdname)) {
//									mdnames.add(mdname);
//								}
//							}
//							String joined = String.join("#", mdnames);
//							api_comb_id = im.GetAPICombID(joined);
//							api_relative_id = mdnames.indexOf(type_content_id.GetTypeContent());
//						}
//					}
//				}
			}
//			StatementInfo stmt = in_handling_tensor.peek();
//			stmt.StoreOneNode(tid, var, api_comb_id, api_relative_id);
		}
		StatementInfo stmt = in_handling_tensor.peek();
		stmt.StoreOneNode(tid, var, node, node.GetParent(), -1, -1);
		return true;
	}

	@Override
	public void PostVisit(TreeNode node) {
		if (StatementUtil.IsStatement(node.GetClazz()) || StatementUtil.IsMethodDeclaration(node.GetClazz())) {
			TreeNode handle_node = in_handling_node.pop();
			Assert.isTrue(handle_node.equals(node));
			StatementInfo last_stmt = in_handling_tensor.pop();
			Assert.isTrue(last_stmt == node_stmt.get(node));
		}
	}

	@Override
	public StringTensor GetStringTensor() {
		StatementTensor curr_tensor = new StatementTensor(ti);
		Iterator<TreeNode> pot_itr = pre_order_node.iterator();
		while (pot_itr.hasNext()) {
			TreeNode an = pot_itr.next();
			StatementInfo si = node_stmt.get(an);
			Assert.isTrue(si != null);
			curr_tensor.Devour(si);
		}
		curr_tensor.HandleAllDevoured(im);
		StringTensor st = new StringTensor(ti);
		st.SetToString(curr_tensor.toString());
		st.SetToDebugString(curr_tensor.toDebugString());
		st.SetToOracleString(curr_tensor.toOracleString());
		st.SetSize(curr_tensor.GetSize());
		return st;
	}

	@Override
	public void ClearAndInitialize(TensorInfo ti) {
		this.ti = ti;
		in_handling_node.clear();
		in_handling_tensor.clear();
		pre_order_node.clear();
		node_stmt.clear();
	}
	
}
