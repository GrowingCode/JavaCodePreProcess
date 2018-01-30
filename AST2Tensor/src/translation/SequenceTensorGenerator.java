package translation;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import translation.helper.DecodeType;
import translation.helper.TestDataDecodeType;
import translation.helper.TrainDataDecodeType;
import translation.helper.TypeContentID;
import translation.helper.TypeContentIDFetcher;
import translation.tensor.SequenceTensor;

public class SequenceTensorGenerator extends TensorGenerator {
	
	RoleAssigner role_assigner = null;
	IJavaProject java_project = null;
	IDManager im = null;

	ICompilationUnit icu = null;
	CompilationUnit cu = null;
	
	DecodeType decode_type_generator = null;
	
	public SequenceTensorGenerator(RoleAssigner role_assigner, IJavaProject java_project, IDManager im, ICompilationUnit icu,
			CompilationUnit cu) {
		this.role_assigner = role_assigner;
		this.java_project = java_project;
		this.im = im;
		this.icu = icu;
		this.cu = cu;
	}
	
	@Override
	public void preVisit(ASTNode node) {
		if (node.getParent() == null) {
			int role = role_assigner.AssignRole();
			tensor_list.add(new SequenceTensor(role));
			if (role == 0 || role == 1) {
				decode_type_generator = new TrainDataDecodeType();
			} else {
				decode_type_generator = new TestDataDecodeType();
			}
		}
		SequenceTensor last_sequence = (SequenceTensor)tensor_list.peekLast();
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		// TODO add virtual start here and end in postVisit
		TypeContentID type_content_id = TypeContentIDFetcher.FetchTypeContentID(node, children.size() == 0, im);
		last_sequence.AppendOneToken(type_content_id, decode_type_generator.GenerateDecodeType(node));
		super.preVisit(node);
	}
	
}
