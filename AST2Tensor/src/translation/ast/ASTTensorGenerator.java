package translation.ast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import translation.TensorGenerator;
import translation.helper.TypeContentID;
import translation.helper.TypeContentIDFetcher;
import translation.roles.RoleAssigner;
import translation.tensor.ASTTensor;
import translation.tensor.StatementInfo;
import translation.tensor.StringTensor;

public class ASTTensorGenerator extends TensorGenerator {

	public ASTTensorGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu, CompilationUnit cu) {
		super(role_assigner, im, icu, cu);
	}
	
	public static int min_statement_size = Integer.MAX_VALUE;
	public static String min_size_statement = null;
	
	public static int max_statement_size = Integer.MIN_VALUE;
	public static String max_size_statement = null;

	int token_local_index = 0;
	Map<String, Integer> token_index_record = null;
	ASTTensor curr_tensor = null;
	
	Stack<ASTNode> in_handling_node = new Stack<ASTNode>();
	Stack<StatementInfo> in_handling_tensor = new Stack<StatementInfo>();
	
	LinkedList<ASTNode> pre_order_node = new LinkedList<ASTNode>();
	Map<ASTNode, StatementInfo> node_stmt = new HashMap<ASTNode, StatementInfo>();
	
//	HashMap<ASTNode, Integer> node_index_map = new HashMap<ASTNode, Integer>();
//	HashMap<String, Integer> leafNodeLastIndexMap = new HashMap<>();
	int nodeCount = 0;

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (begin_generation && begin_generation_node.equals(node)) {
			Assert.isTrue(curr_tensor == null);
			Assert.isTrue(token_index_record == null);
			Assert.isTrue(token_local_index == 0);
			Assert.isTrue(in_handling_node.size() == 0);
			Assert.isTrue(in_handling_tensor.size() == 0);
			Assert.isTrue(pre_order_node.size() == 0);
			Assert.isTrue(node_stmt.size() == 0);
			curr_tensor = new ASTTensor(icu.getElementName(), -1);
			token_index_record = new TreeMap<String, Integer>();
			token_local_index = 0;
		}
		if (begin_generation) {
			HandleOneNode(node, true, begin_generation_node.equals(node));
		}
	}

	@Override
	public void postVisit(ASTNode node) {
		if (begin_generation) {
			if (IsStatement(node) || begin_generation_node.equals(node)) {
				ASTNode handle_node = in_handling_node.pop();
				Assert.isTrue(handle_node.equals(node));
				StatementInfo last_stmt = in_handling_tensor.pop();
				Assert.isTrue(last_stmt == node_stmt.get(node));
			}
		}
		if (begin_generation && begin_generation_node.equals(node)) {
			Assert.isTrue(in_handling_node.size() == 0);
			Assert.isTrue(in_handling_tensor.size() == 0);
			int size_of_statements = 0;
			Iterator<ASTNode> pot_itr = pre_order_node.iterator();
			while (pot_itr.hasNext()) {
				ASTNode an = pot_itr.next();
				StatementInfo si = node_stmt.get(an);
				Assert.isTrue(si != null);
				curr_tensor.Devour(si);
				int si_size = si.Size();
				size_of_statements += si_size;
				if (min_statement_size > si_size) {
					min_statement_size = si_size;
					min_size_statement = si.GetStatement() + "\n" + "==== type_content ====" + "\n" + si.GetTypeContentOfStatement();
				}
				if (max_statement_size < si_size) {
					max_statement_size = si_size;
					max_size_statement = si.GetStatement() + "\n" + "==== type_content ====" + "\n" + si.GetTypeContentOfStatement();
				}
			}
			Assert.isTrue(nodeCount == size_of_statements);
			pre_order_node.clear();
			node_stmt.clear();
			curr_tensor.Validate(nodeCount);
			StringTensor st = (StringTensor) tensor_list.getLast();
			st.SetToString(curr_tensor.toString());
			st.SetToDebugString(curr_tensor.toDebugString());
			st.SetToOracleString(curr_tensor.toOracleString());
			st.SetSize(curr_tensor.getSize());
			curr_tensor = null;
//			PrintUtil.PrintMap(token_index_record);
			token_index_record.clear();
			token_index_record = null;
			token_local_index = 0;
//			node_index_map.clear();
			nodeCount = 0;
		}
		super.postVisit(node);
	}

	private boolean IsStatement(ASTNode node) {
		if (node instanceof Statement && !(node instanceof Block)) {
			return true;
		}
		return false;
	}
	
	private void HandleOneNode(ASTNode node, boolean is_real, boolean is_root) {
		nodeCount++;
		if (IsStatement(node) || begin_generation_node.equals(node)) {
			in_handling_node.add(node);
			StatementInfo stmt_info = new StatementInfo(token_local_index, node.toString());
			in_handling_tensor.add(stmt_info);
			pre_order_node.add(node);
			node_stmt.put(node, stmt_info);
		}
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		boolean is_leaf = children.size() == 0;
		{
			TypeContentID type_content_id = TypeContentIDFetcher.FetchTypeID(node, im);
			StatementInfo stmt = in_handling_tensor.peek();
			stmt.StoreOneNode(im, type_content_id, -1, -1);
		}
		if (is_leaf) {
			Assert.isTrue(node instanceof SimpleName, "wrong node class:" + node.getClass());
			TypeContentID type_content_id = TypeContentIDFetcher.FetchContentID(node, im);
			int token_index = -1;
			int is_var = -1;
			SimpleName sn = (SimpleName) node;
			IBinding ib = sn.resolveBinding();
			if (ib != null) {
				if (ib instanceof IVariableBinding) {
					IVariableBinding ivb = (IVariableBinding) ib;
					String ivb_key = "var!" + ivb.getVariableId() + "#" + sn;
					Integer index_record = token_index_record.get(ivb_key);
					if (index_record == null) {
						token_local_index++;
						index_record = token_local_index;
						token_index_record.put(ivb_key, index_record);
					}
					token_index = index_record;
					is_var = 1;
				}
//				if (ib instanceof ITypeBinding) {
//					ITypeBinding itb = (ITypeBinding) ib;
//					String itb_key = "type!" + itb.getKey() + "#" + sn;
//					Integer index_record = token_index_record.get(itb_key);
//					if (index_record == null) {
//						token_local_index++;
//						index_record = token_local_index;
//						token_index_record.put(itb_key, index_record);
//					}
//					token_index = index_record;
//					is_var = 0;
//				}
			}
			StatementInfo stmt = in_handling_tensor.peek();
			stmt.StoreOneNode(im, type_content_id, token_index, is_var);
		}
	}

}