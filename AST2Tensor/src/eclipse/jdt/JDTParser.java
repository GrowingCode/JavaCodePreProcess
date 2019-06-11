package eclipse.jdt;

import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class JDTParser {
	
	public static CompilationUnit ParseICompilationUnit(ICompilationUnit icu)
	{
		ASTParser parser= ASTParser.newParser(AST.JLS11);
		parser.setResolveBindings(true);
		parser.setSource(icu);
		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);
		parser.setIgnoreMethodBodies(false);
//		if (getCurrentInputKind() == ASTInputKindAction.USE_FOCAL) {
//			parser.setFocalPosition(offset);
//		}
		CompilationUnit compilationUnit= (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
	public static CompilationUnit ParseOneClass(IType f)
	{
		return ParseOneClass(f.getClassFile());
	}
	
	public static CompilationUnit ParseOneClass(IClassFile f)
	{
		ASTParser parser= ASTParser.newParser(AST.JLS11);
		parser.setResolveBindings(true);
		parser.setSource(f);
		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);
		parser.setIgnoreMethodBodies(false);
//		if (getCurrentInputKind() == ASTInputKindAction.USE_FOCAL) {
//			parser.setFocalPosition(offset);
//		}
		CompilationUnit compilationUnit= (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
//	public static CompilationUnit ParseJavaContent(String package_name, String unit_name, IDocument doc)
//	{
//		IJavaProject java_project = AnalysisEnvironment.GetDefaultAnalysisEnironment();
//		String file_unit_name = unit_name + ".java";
//		String proj_name = java_project.getElementName();
//		FakeResourceCreationHelper.CreateAndImportFakeJavaFile(proj_name, package_name, file_unit_name, doc);
//		IType it = null;
//		String full_qualified_name = package_name + (package_name.equals("") ? "" : ".") + unit_name;
//		try {
//			it = java_project.findType(full_qualified_name);
//		} catch (JavaModelException e) {
//			e.printStackTrace();
//		}
//		CompilationUnit compilationUnit = null;
//		if (it != null && it.getCompilationUnit() != null) {
//			// parser.setUnitName("/" + proj_name + "/src/" + package_name.replace('.', '/') + (package_name.equals("") ? "" : "/") + file_unit_name);
//			// parser.setSource(doc.get().toCharArray());
//			compilationUnit = ParseICompilationUnit(it.getCompilationUnit());
//		} else {
//			System.err.println("Warning: " + full_qualified_name + " can not be founded!");
//		}
//		return compilationUnit;
//	}
	
}
