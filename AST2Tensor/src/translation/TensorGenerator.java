package translation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.search.JDTSearchForChildrenOfASTNode;
import logger.DebugLogger;
import statistic.id.IDManager;
import translation.tensor.Tensor;
import util.SystemUtil;

public class TensorGenerator extends ASTVisitor {

	IJavaProject java_project = null;
	IDManager im = null;

	ICompilationUnit icu = null;
	CompilationUnit cu = null;

	Tensor t = new Tensor();
	
	public TensorGenerator(IJavaProject java_project, IDManager im, ICompilationUnit icu, CompilationUnit cu) {
		this.java_project = java_project;
		this.im = im;
		this.icu = icu;
		this.cu = cu;
	}

	private Map<ASTNode, Integer> node_idx = new HashMap<ASTNode, Integer>();
	private int current_max_idx = -1;

	private int GetNewIndex() {
		current_max_idx++;
		return current_max_idx;
	}

	private int GetASTNodeIndex(ASTNode node) {
		Integer idx = node_idx.get(node);
		if (idx == null) {
			current_max_idx++;
			idx = current_max_idx;
			node_idx.put(node, idx);
		}
		return idx;
	}

	Stack<Integer> expected_handled_child_num = new Stack<Integer>();
	Stack<Integer> already_handled_child_num = new Stack<Integer>();

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		expected_handled_child_num.push(children.size());
		already_handled_child_num.push(0);
	}

	@Override
	public void postVisit(ASTNode node) {
		if (expected_handled_child_num.peek() != already_handled_child_num.peek()) {
			DebugLogger.Error("Strange! expected num of children unmatched!");
			SystemUtil.Flush();
			System.exit(1);
		}
		// handle this node
		expected_handled_child_num.pop();
		already_handled_child_num.pop();
		int node_index = GetASTNodeIndex(node);
		List<ASTNode> children = JDTSearchForChildrenOfASTNode.GetChildren(node);
		// set node type and content
		int node_type = -1;
		int node_content = -1;
		if (children.size() > 0) {
			node_type = im.GetTypeID(IDManager.PrimordialNonLeafASTType);
			node_content = im.GetContentID(IDManager.PrimordialNonLeafASTType, node.getClass().getSimpleName());
		} else {
			String type = node.getParent().getClass().getSimpleName() + "#" + node.getClass().getSimpleName();
			node_type = im.GetTypeID(type);
			node_content = im.GetContentID(type, node.toString());
		}
		// handle node self and reform its children to binary tree.
		HandleChildren(node_index, children, node_type, node_content);
		already_handled_child_num.push(already_handled_child_num.pop() + 1);

		super.postVisit(node);
	}

	private void HandleChildren(int node_index, List<ASTNode> children, int node_type, int node_content) {
		int node_left_index = -1;
		int node_right_index = -1;
		if (children.size() > 0) {
			ASTNode child = children.get(0);
			node_left_index = GetASTNodeIndex(child);
			if (1 == children.size()) {
				node_right_index = GetNewIndex();
				HandleChildren(node_right_index, new LinkedList<ASTNode>(), im.GetTypeID(IDManager.TerminalLeafASTType), 0);
			} else {
				node_right_index = GetNewIndex();
				HandleChildren(node_right_index, children.subList(1, children.size()), 0, 0);
			}
			GetASTNodeIndex(child);
		}
		t.StoreOneASTNode(node_index, node_left_index, node_right_index, node_type, node_content);
	}

	public Tensor GetGeneratedTensor() {
		return t;
	}
	
	/*
	 * @Override public boolean visit(AnnotationTypeDeclaration node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(AnnotationTypeMemberDeclaration node) { //
	 * nothing return super.visit(node); }
	 * 
	 * @Override public boolean visit(EnumConstantDeclaration node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(EnumDeclaration node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(MethodDeclaration node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(TypeDeclaration node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(FieldDeclaration node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(Block node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(MarkerAnnotation node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(NormalAnnotation node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(SingleMemberAnnotation node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(PackageDeclaration node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(AnonymousClassDeclaration node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(SuperConstructorInvocation node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(MethodInvocation node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(IfStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ForStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(EnhancedForStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(DoStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ConstructorInvocation node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(ClassInstanceCreation node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(CatchClause node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ArrayInitializer node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(SuperMethodInvocation node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(SwitchStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(TryStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(VariableDeclarationExpression node) { //
	 * nothing return super.visit(node); }
	 * 
	 * @Override public boolean visit(VariableDeclarationStatement node) { //
	 * nothing return super.visit(node); }
	 * 
	 * @Override public boolean visit(WhileStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(SingleVariableDeclaration node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(CompilationUnit node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ReturnStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ParenthesizedExpression node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(ParameterizedType node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(MemberValuePair node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(InstanceofExpression node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(InfixExpression node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ConditionalExpression node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(CastExpression node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(Assignment node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(AssertStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ArrayType node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ArrayCreation node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ArrayAccess node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(SwitchCase node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(SynchronizedStatement node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(ThisExpression node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(TextElement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(TagElement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(SuperFieldAccess node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(StringLiteral node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(SimpleType node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(SimpleName node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(QualifiedName node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(PrimitiveType node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(NumberLiteral node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(NullLiteral node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(MethodRefParameter node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(MethodRef node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(MemberRef node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(Javadoc node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(Initializer node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(FieldAccess node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(BlockComment node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(EmptyStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ContinueStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(CharacterLiteral node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(BreakStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(BooleanLiteral node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(LineComment node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ThrowStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(TypeDeclarationStatement node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(TypeLiteral node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(TypeParameter node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(VariableDeclarationFragment node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(WildcardType node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ExpressionStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ImportDeclaration node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(LabeledStatement node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(PostfixExpression node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(PrefixExpression node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(Modifier node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(QualifiedType node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(CreationReference node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ExpressionMethodReference node) { // nothing
	 * return super.visit(node); }
	 * 
	 * @Override public boolean visit(LambdaExpression node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(SuperMethodReference node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(TypeMethodReference node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(Dimension node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(IntersectionType node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ModuleDeclaration node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(NameQualifiedType node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(UnionType node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ExportsDirective node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ModuleModifier node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(ProvidesDirective node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(RequiresDirective node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(UsesDirective node) { // nothing return
	 * super.visit(node); }
	 * 
	 * @Override public boolean visit(OpensDirective node) { // nothing return
	 * super.visit(node); }
	 */

}
