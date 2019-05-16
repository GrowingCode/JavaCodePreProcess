package translation.sequence;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import statistic.id.PreProcessContentHelper;
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
		{
			TypeContentID type_content_id = TypeContentIDFetcher.FetchTypeID(node, im);
			curr_tensor.AppendOneToken(im, type_content_id, -1, 0, -1, 1);
		}
		if (is_leaf) {
			Assert.isTrue(node instanceof SimpleName, "wrong node class:" + node.getClass());
			TypeContentID type_content_id = TypeContentIDFetcher.FetchContentID(node, im);
			// two parameters for leaf similarity test
			int isExisted = 0;
			int lastIndex = -1;
			String name = JDTASTHelper.GetContentRepresentationForASTNode(node);
			// the last node
			if (leafNodeLastIndexMap.containsKey(name)) {
				lastIndex = leafNodeLastIndexMap.get(name) - nodeCount;
				isExisted = 1;
			}
			leafNodeLastIndexMap.put(name, nodeCount);
			int api_comb_id = -1;
			SimpleName sn = (SimpleName) node;
			IBinding ib = sn.resolveBinding();
			if (ib != null && ib instanceof IMethodBinding) {
				if (!(sn.getParent() instanceof MethodDeclaration)) {
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
				}
			}
			curr_tensor.AppendOneToken(im, type_content_id, api_comb_id, isExisted, lastIndex, 1);
		}
	}
	
}
