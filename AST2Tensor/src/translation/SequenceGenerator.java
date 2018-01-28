package translation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import translation.helper.TypeContentID;
import translation.helper.TypeContentIDFetcher;
import translation.tensor.Sequence;

public class SequenceGenerator extends ASTVisitor {
	
	RoleAssigner role_assigner = null;
	IJavaProject java_project = null;
	IDManager im = null;

	ICompilationUnit icu = null;
	CompilationUnit cu = null;
	
	LinkedList<Sequence> seq_list = new LinkedList<Sequence>();
	
	DecodeTypeGenerator decode_type_generator = null;
	
	public SequenceGenerator(RoleAssigner role_assigner, IJavaProject java_project, IDManager im, ICompilationUnit icu,
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
			seq_list.add(new Sequence());
			int role = role_assigner.AssignRole();
			if (role == 0 || role == 1) {
				decode_type_generator = new TrainDataDecodeTypeGenerator();
			} else {
				decode_type_generator = new TestDataDecodeTypeGenerator();
			}
		}
		Sequence last_sequence = seq_list.peekLast();
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		TypeContentID type_content_id = TypeContentIDFetcher.FetchTypeContentID(node, children.size() == 0, im);
		last_sequence.AppendOneToken(type_content_id, decode_type_generator.GenerateDecodeType(node));
		super.preVisit(node);
	}
	
	public List<Sequence> GetGeneratedSequence() {
		return seq_list;
	}
	
}
