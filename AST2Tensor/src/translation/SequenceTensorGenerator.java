package translation;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import translation.helper.TestDataDecodeType;
import translation.helper.TrainDataDecodeType;
import translation.helper.TypeContentID;
import translation.helper.TypeContentIDFetcher;
import translation.tensor.SequenceTensor;

public class SequenceTensorGenerator extends TensorGenerator {
	
	public SequenceTensorGenerator(RoleAssigner role_assigner, IJavaProject java_project, IDManager im, ICompilationUnit icu,
			CompilationUnit cu) {
		super(role_assigner, java_project, im, icu, cu);
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		if (node.getParent() == null) {
			int role = role_assigner.AssignRole(icu.getElementName());
			tensor_list.add(new SequenceTensor(role));
			if (role == 0 || role == 1) {
				decode_type_generator = new TrainDataDecodeType();
			} else {
				decode_type_generator = new TestDataDecodeType();
			}
		}
		SequenceTensor last_sequence = (SequenceTensor)tensor_list.peekLast();
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		boolean is_leaf = children.size() == 0;
		TypeContentID type_content_id = TypeContentIDFetcher.FetchTypeContentID(node, is_leaf, im);
		last_sequence.AppendOneToken(type_content_id, decode_type_generator.GenerateDecodeType(node));
		if (!is_leaf) {
			last_sequence.AppendOneToken(new TypeContentID(IDManager.InitialLeafASTType, IDManager.Default, im.GetTypeID(IDManager.InitialLeafASTType), im.GetContentID(IDManager.Default)), 0);
		}
	}
	
	@Override
	public void postVisit(ASTNode node) {
		SequenceTensor last_sequence = (SequenceTensor)tensor_list.peekLast();
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		boolean is_leaf = children.size() == 0;
		if (!is_leaf) {
			last_sequence.AppendOneToken(new TypeContentID(IDManager.TerminalLeafASTType, IDManager.Default, im.GetTypeID(IDManager.TerminalLeafASTType), im.GetContentID(IDManager.Default)), 0);
		}
		super.postVisit(node);
	}
	
}
