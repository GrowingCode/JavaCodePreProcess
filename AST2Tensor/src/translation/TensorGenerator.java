package translation;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

import statistic.id.IDManager;
import translation.tensor.Tensor;

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
	
	@Override
	public boolean preVisit2(ASTNode node) {
		// TODO
		
		return super.preVisit2(node);
	}
	
	/*@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnumConstantDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnumDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(Block node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(PackageDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(IfStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ForStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnhancedForStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(DoStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ConstructorInvocation node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ClassInstanceCreation node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(CatchClause node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ArrayInitializer node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SwitchStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TryStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(VariableDeclarationExpression node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(VariableDeclarationStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(WhileStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleVariableDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(CompilationUnit node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ReturnStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ParenthesizedExpression node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ParameterizedType node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MemberValuePair node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(InstanceofExpression node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(InfixExpression node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ConditionalExpression node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(CastExpression node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(Assignment node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(AssertStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ArrayType node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ArrayCreation node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ArrayAccess node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SwitchCase node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SynchronizedStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ThisExpression node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TextElement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TagElement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperFieldAccess node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(StringLiteral node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SimpleType node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SimpleName node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(QualifiedName node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(PrimitiveType node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NumberLiteral node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NullLiteral node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodRefParameter node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodRef node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MemberRef node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(Javadoc node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(Initializer node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldAccess node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(BlockComment node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EmptyStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ContinueStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(CharacterLiteral node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(BreakStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(BooleanLiteral node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(LineComment node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ThrowStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeDeclarationStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeLiteral node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeParameter node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(WildcardType node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ExpressionStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ImportDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(LabeledStatement node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(PostfixExpression node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(PrefixExpression node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(Modifier node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(QualifiedType node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(CreationReference node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ExpressionMethodReference node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(LambdaExpression node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperMethodReference node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeMethodReference node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(Dimension node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(IntersectionType node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ModuleDeclaration node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NameQualifiedType node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(UnionType node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ExportsDirective node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ModuleModifier node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ProvidesDirective node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(RequiresDirective node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(UsesDirective node) {
		// nothing
		return super.visit(node);
	}
	
	@Override
	public boolean visit(OpensDirective node) {
		// nothing
		return super.visit(node);
	}*/

	public Tensor GetGeneratedTensor() {
		return t;
	}
	
}
