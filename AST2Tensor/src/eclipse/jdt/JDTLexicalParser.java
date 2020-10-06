package eclipse.jdt;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

import eclipse.project.process.PreProcessCompilationUnitHelper;
import util.FileUtil;

public class JDTLexicalParser {
	
	public static CompilationUnit ParseJavaFile(File f)
	{
		ASTParser parser = ASTParser.newParser(JDTHelper.GetCurrentJavaVersion());// AST.JLS13
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_10);
		parser.setCompilerOptions(options);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(FileUtil.ReadFromFile(f).toCharArray());
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
//	public static String PreProcess(File f) {
//		String changed_class = PreProcessCompilationUnitHelper.PreProcessDeleter(f);
//		CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(null);
//		TextEdit textEdit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, changed_class, 0,
//				changed_class.length(), 0, null);
//		IDocument doc = new Document(changed_class);
//		try {
//			textEdit.apply(doc);
//			changed_class = doc.get();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return changed_class;
//	}
	
//	public static CompilationUnit PreProcessAndParseJavaFile(File f)
//	{
//		ASTParser parser = ASTParser.newParser(AST.JLS13);
//		Map<String, String> options = JavaCore.getOptions();
//		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_10);
//		parser.setCompilerOptions(options);
//		parser.setKind(ASTParser.K_COMPILATION_UNIT);
//		parser.setSource(PreProcess(f).toCharArray());
////		parser.setSource(PreProcessCompilationUnitHelper.PreProcessDeleter(f).toCharArray());
//		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
//		return compilationUnit;
//	}
	
	public static CompilationUnit PreProcessAndParseJavaFileWithRecovery(File f) {
		String dir = System.getProperty("user.home") + "/YYXEmptyFileDirectory";
		File f_e_dir = new File(dir);
		if (!f_e_dir.exists()) {
			f_e_dir.mkdirs();
		}
		ASTParser parser = ASTParser.newParser(JDTHelper.GetCurrentJavaVersion());// AST.JLS13
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_10);
		parser.setCompilerOptions(options);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
//		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);
//		parser.setIgnoreMethodBodies(false);
		parser.setResolveBindings(true);
//		System.out.println(f.getName());
		parser.setUnitName(f.getName());
//		System.out.println(file_content);
//		String[] sources = { "test_data/test/test_project1" };
		String[] sources = { f_e_dir.getAbsolutePath() };// f.getParent()
//		String java_home = System.getenv("JAVA_HOME");
//		if (java_home == null) {
//			System.err.println("Wrong! JAVA_HOME is not set yet! Please set up the JAVA_HOME and rerun the program again!");
//			System.exit(1);
//		}
//		String[] classpaths = { java_home + "/jre/lib/rt.jar" };
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader) cl).getURLs();
		String[] classpaths = new String[urls.length];
		for (int i = 0; i < urls.length; i++) {
			classpaths[i] = urls[i].getFile();
        	// System.out.println(urls[i].getFile());
		}
		parser.setEnvironment(classpaths, sources, new String[] { "UTF-8" }, true);
		String file_content = PreProcessCompilationUnitHelper.PreProcessDeleter(f);
//		String file_content = FileUtil.ReadFromFile(f);
		parser.setSource(file_content.toCharArray());
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}

	public static void main(String[] args) {
		ASTVisitor av = new ASTVisitor() {
			
			@Override
			public boolean visit(MethodDeclaration node) {
				System.out.println("===== split line =====");
				return super.visit(node);
			}
			
			@Override
			public boolean visit(SimpleName node) {
				IBinding bind = node.resolveBinding();
				if (bind != null) {
					if (bind instanceof IVariableBinding) {
						IVariableBinding nbind = (IVariableBinding)bind;
						System.out.println("var_node:" + node + "#bind_element:" + nbind.getJavaElement() + "#dec_var:" + nbind.getVariableDeclaration() + "#var_id:" + nbind.getVariableId() + "#bind_name:" + nbind.getName() + "#bind_type:" + nbind.getClass() + "#nbind_key:" + nbind.getKey() + "#nbind:" + nbind);
					}
					if (bind instanceof ITypeBinding) {
						ITypeBinding nbind = (ITypeBinding)bind;
						System.out.println("type_node:" + node + "#bind_element:" + nbind.getJavaElement() + "#qualified_name:" + nbind.getQualifiedName() + "#binary_name:" + nbind.getBinaryName() + "#bind_name:" + nbind.getName() + "#bind_type:" + nbind.getClass() + "#nbind_key:" + nbind.getKey());// + "#nbind:" + nbind
					}
//					System.out.println("node:" + node + "#bind_type:" + nbind.getClass() + "#nbind:" + nbind);
				}
				return super.visit(node);
			}
			
		};
		CompilationUnit cu = JDTLexicalParser.PreProcessAndParseJavaFileWithRecovery(
				new File("test_data/test/test_project_difficult/HeiHei.java"));
		// "test_data/test/test_project1/com/google/thirdparty/publicsuffix/TrieParser.java"
		cu.accept(av);
	}
	
}
