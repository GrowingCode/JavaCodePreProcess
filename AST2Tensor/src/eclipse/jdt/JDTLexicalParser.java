package eclipse.jdt;

import java.io.File;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import util.FileUtil;

public class JDTLexicalParser {
	
//	private static JDTLexicalParser unique_parser = new JDTLexicalParser();
//	private ASTParser parser = null;
	
	public static CompilationUnit ParseJavaFile(File f)
	{
		ASTParser parser = ASTParser.newParser(AST.JLS10);
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_10);
		parser.setCompilerOptions(options);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(FileUtil.ReadFromFile(f).toCharArray());
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
//	public static JDTLexicalParser GetUniqueParser() {
//		return unique_parser;
//	}
	
}
