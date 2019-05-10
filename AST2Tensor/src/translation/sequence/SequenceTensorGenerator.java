package translation.sequence;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import translation.TensorGenerator;
import translation.helper.TypeContentID;
import translation.helper.TypeContentIDFetcher;
import translation.roles.RoleAssigner;
import translation.tensor.SequenceTensor;
import translation.tensor.StringTensor;

public class SequenceTensorGenerator extends TensorGenerator {
	
	SequenceTensor curr_tensor = null;
	HashMap<String, Integer> leafNodeLastIndexMap = new HashMap<String, Integer>();
	int nodeCount = 0;

	public SequenceTensorGenerator(RoleAssigner role_assigner, IDManager im, ICompilationUnit icu,
			CompilationUnit cu) {
		super(role_assigner, im, icu, cu);
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (begin_generation && begin_generation_node.equals(node)) {
			curr_tensor = new SequenceTensor(icu.getElementName(), -1);
		}
		if (begin_generation) {
			HandleOneNode(node, true, begin_generation_node.equals(node));
		}
	}
	
	@Override
	public void postVisit(ASTNode node) {
//		if (begin_generation) {
//			List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
//			boolean is_leaf = children.size() == 0;
//			if (!is_leaf) {
//				// , IDManager.Default , im.GetContentID(IDManager.Default)
//				curr_sequence.AppendOneToken(new TypeContentID(IDManager.TerminalLeafASTType,
//						im.GetTypeContentID(IDManager.TerminalLeafASTType)), 0);
//			}
//		}
//		if (begin_generation) {
//			List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
//			boolean is_leaf = children.size() == 0;
//			TypeContentID type_content_id = TypeContentIDFetcher.FetchTypeContentID(node, is_leaf, im);
//			if (!is_leaf) {
//				curr_tensor.AppendOneToken(type_content_id, 0);// decode_type_generator.GenerateDecodeType(node)
//			}
//		}
		if (begin_generation && begin_generation_node.equals(node)) {
			StringTensor st = (StringTensor) tensor_list.getLast();
			st.SetToString(curr_tensor.toString());
			st.SetToDebugString(curr_tensor.toDebugString());
			st.SetToOracleString(curr_tensor.toOracleString());
			st.SetSize(curr_tensor.getSize());
			curr_tensor = null;
			leafNodeLastIndexMap.clear();
			nodeCount = 0;
		}
		super.postVisit(node);
	}
	
	private void HandleOneNode(ASTNode node, boolean is_real, boolean is_root) {
		nodeCount++;
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		boolean is_leaf = children.size() == 0;
		TypeContentID type_content_id = TypeContentIDFetcher.FetchTypeContentID(node, is_leaf, im);

		// two parameters for leaf similarity test
		int isExisted = 0;
		int lastIndex = -1;
		if (is_leaf) {
			String name = node.toString();
			// the last node
			if (leafNodeLastIndexMap.containsKey(name)) {
				lastIndex = leafNodeLastIndexMap.get(name) - nodeCount;
				isExisted = 1;
			}
			leafNodeLastIndexMap.put(name, nodeCount);
		}

		curr_tensor.AppendOneToken(im, type_content_id, isExisted, lastIndex, 1);// decode_type_generator.GenerateDecodeType(node)
	}
	
}
