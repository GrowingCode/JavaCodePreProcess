package translation.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import main.MetaOfApp;
import statistic.id.IDManager;
import translation.TensorGenerator;
import translation.helper.TypeContentID;
import translation.helper.TypeContentIDFetcher;
import translation.roles.RoleAssigner;
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
			LinkedList<ASTNode> children_nodes = JDTSearchForChildrenOfASTNode.GetChildren(node);
			if (children_nodes != null && children_nodes.size() > 0) {
				curr_tensor.StorePrePostOrderNodeInfo(type_content_id.GetTypeContentID(), 0, -1);
			}
		}
	}

	@Override
	public void postVisit(ASTNode node) {
		if (begin_generation) {
			int post_order_index = node_post_order_index.size();
			node_post_order_index.put(node, post_order_index);
			TypeContentID type_content_id = TypeContentIDFetcher.FetchContentID(node, im);
			LinkedList<ASTNode> children_nodes = JDTSearchForChildrenOfASTNode.GetChildren(node);
			ArrayList<Integer> children_index = new ArrayList<Integer>();
			int c_start = 0;
			int c_end = -1;
			boolean has_children = children_nodes != null && children_nodes.size() > 0;
			if (has_children) {
				c_start = node_post_order_index.get(children_nodes.getFirst());
				c_end = node_post_order_index.get(children_nodes.getLast());
				Iterator<ASTNode> n_itr = children_nodes.iterator();
				while (n_itr.hasNext()) {
					ASTNode n = n_itr.next();
					children_index.add(node_post_order_index.get(n));
				}
			}
			int pre_index = node_pre_order_index.get(node);
			curr_tensor.StorePostOrderNodeInfo(type_content_id.GetTypeContentID(), pre_index, c_start, c_end, children_index);
			curr_tensor.StorePrePostOrderNodeInfo(type_content_id.GetTypeContentID(), has_children ? 2 : 1, post_order_index);
		}
		if (begin_generation && begin_generation_node.equals(node)) {
			// >= MetaOfApp.MinimumNumberOfStatementsInAST
			if (MetaOfApp.StatementNoLimit || (curr_tensor.getSize() >= MetaOfApp.MinimumNumberOfNodesInAST)) {
				StringTensor st = (StringTensor) tensor_list.getLast();
				curr_tensor.Validate();
				st.SetToString(curr_tensor.toString());
				st.SetToDebugString(curr_tensor.toDebugString());
				st.SetToOracleString(curr_tensor.toOracleString());
				st.SetSize(curr_tensor.getSize());
			} else {
				tensor_list.removeLast();
			}
			curr_tensor = null;
			node_pre_order_index.clear();
			node_post_order_index.clear();
		}
		super.postVisit(node);
	}
	
}
