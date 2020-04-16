package dataset;

import java.io.File;

import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTLexicalParser;

public class JavaFileScorer {
	
	public static double ScoreFile(File f) {
//		if (f.length() / 1024 < 12) {
//			return -1;
//		}
		CompilationUnit cu = JDTLexicalParser.ParseJavaFile(f);
		ASTScoreGenerator score_generator = new ASTScoreGenerator();
		cu.accept(score_generator);
		return score_generator.GetScore();
	}
	
}
