package translation.ast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import main.MetaOfApp;
import statistic.id.IDManager;
import statistic.id.PreProcessContentHelper;
import translation.TensorGenerator;
import translation.helper.TypeContentID;
import translation.helper.TypeContentIDFetcher;
import translation.roles.RoleAssigner;
import translation.tensor.ASTTensor;
import translation.tensor.StatementInfo;
import translation.tensor.StringTensor;
import translation.tensor.TreeTensor;

public class TreeTensorGenerator extends TensorGenerator {

	public TreeTensorGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu, CompilationUnit cu) {
		super(role_assigner, im, icu, cu);
	}

	TreeTensor curr_tensor = null;

	Map<ASTNode, Integer> node_pre_order_index = new HashMap<ASTNode, Integer>();
	Map<ASTNode, Integer> node_post_order_index = new HashMap<ASTNode, Integer>();

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (begin_generation && begin_generation_node.equals(node)) {
			Assert.isTrue(curr_tensor == null);
//			Assert.isTrue(token_index_record == null);
			curr_tensor = new TreeTensor(im, -1);
		}
		if (begin_generation) {
//			boolean is_root = begin_generation_node.equals(node);
			node_pre_order_index.put(node, node_pre_order_index.size());
			TypeContentID type_content_id = TypeContentIDFetcher.FetchContentID(node, im);
			curr_tensor.StorePreOrderNodeEnInfo(type_content_id.GetTypeContentID());
		}
	}

	@Override
	public void postVisit(ASTNode node) {
		if (begin_generation) {
			node_post_order_index.put(node, node_post_order_index.size());
			TypeContentID type_content_id = TypeContentIDFetcher.FetchContentID(node, im);
			List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
			
			
			curr_tensor.StorePostOrderNodeInfo(type_content_id.GetTypeContentID(), node_pre_order_index.get(node), c_start, c_end, children);
		}
		if (begin_generation && begin_generation_node.equals(node)) {
			if (MetaOfApp.StatementNoLimit || (node_stmt.size() >= MetaOfApp.MinimumNumberOfStatementsInAST
					&& statement_node_count >= MetaOfApp.MinimumNumberOfNodesInAST)) {
				int size_of_statements = 0;
				Iterator<ASTNode> pot_itr = pre_order_node.iterator();
				while (pot_itr.hasNext()) {
					ASTNode an = pot_itr.next();
					StatementInfo si = node_stmt.get(an);
					Assert.isTrue(si != null);
					curr_tensor.Devour(si);
					int si_size = si.Size();
					size_of_statements += si_size;
				}
				StringTensor st = (StringTensor) tensor_list.getLast();
				st.SetToString(curr_tensor.toString());
				st.SetToDebugString(curr_tensor.toDebugString());
				st.SetToOracleString(curr_tensor.toOracleString());
				st.SetSize(curr_tensor.getSize());
			} else {
				tensor_list.removeLast();
			}
			curr_tensor = null;
		}
		super.postVisit(node);
	}

	private boolean IsStatement(ASTNode node) {
		if (node instanceof Statement && !(node instanceof Block)) {
			return true;
		}
		return false;
	}

	private boolean IsMethodDeclaration(ASTNode node) {
		if (node instanceof MethodDeclaration) {
			return true;
		}
		return false;
	}

	private void HandleOneNode(ASTNode node, boolean is_real, boolean is_root) {
//		System.err.println("node:" + node);
		if (begin_generation_node.equals(node)) {
			Assert.isTrue(IsMethodDeclaration(node));
		}
		if (IsStatement(node) || IsMethodDeclaration(node)) {
			in_handling_node.add(node);
			StatementInfo stmt_info = new StatementInfo(token_local_index, node.toString());
			in_handling_tensor.add(stmt_info);
			pre_order_node.add(node);
			node_stmt.put(node, stmt_info);
		}
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		{
			nodeCount++;
			TypeContentID type_content_id = TypeContentIDFetcher.FetchTypeID(node, im);
			StatementInfo stmt = in_handling_tensor.peek();
			stmt.StoreOneNode(im, type_content_id, null, -1, 0);
		}
		boolean is_leaf = children.size() == 0;
		if (is_leaf) {
			leafExtraCount++;
//			Assert.isTrue(node instanceof SimpleName, "wrong node class:" + node.getClass() + "#simple name:" + node);
			TypeContentID type_content_id = TypeContentIDFetcher.FetchContentID(node, im);
//			int var_index = -1;
			String var = null;
			int api_comb_id = -1;
			int api_relative_id = -1;
			if (node instanceof SimpleName) {
				SimpleName sn = (SimpleName) node;
//				var_index = HandleVariableIndex(sn.toString());
				var = sn.toString();
//				System.err.println("simple_name:" + sn);
				IBinding ib = sn.resolveBinding();
				if (ib != null) {
					if (ib instanceof IVariableBinding) {
//						IVariableBinding ivb = (IVariableBinding) ib;
//						String ivb_key = "var!" + ivb.getVariableId() + "#" + sn;
//						var_index = HandleVariableIndex(ivb_key)
					} else if (ib instanceof ITypeBinding) {
//						ITypeBinding itb = (ITypeBinding) ib;
//						String itb_key = "type!" + itb.getKey() + "#" + sn;
//						var_index = HandleVariableIndex(itb_key);
					} else if (ib instanceof IMethodBinding) {
						if (!(sn.getParent() instanceof MethodDeclaration)
								&& (sn.getParent() instanceof MethodInvocation)) {
							IMethodBinding imb = (IMethodBinding) ib;
							ITypeBinding dc = imb.getDeclaringClass();
							IMethodBinding[] mds = dc.getDeclaredMethods();
							LinkedList<String> mdnames = new LinkedList<String>();
							for (IMethodBinding md : mds) {
								String mdname = md.getName();
								mdname = PreProcessContentHelper.PreProcessTypeContent(mdname);
								if (!mdnames.contains(mdname)) {
									mdnames.add(mdname);
								}
							}
							String joined = String.join("#", mdnames);
							api_comb_id = im.GetAPICombID(joined);
							api_relative_id = mdnames.indexOf(type_content_id.GetTypeContent());
						}
					}
//					else {
//						new Exception("type of unmeet ib:" + ib.getClass()).printStackTrace();
//						System.exit(1);
//					}
				}
			}
			StatementInfo stmt = in_handling_tensor.peek();
			stmt.StoreOneNode(im, type_content_id, var, api_comb_id, api_relative_id);
		}
	}

}
