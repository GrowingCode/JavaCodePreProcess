package eclipse.project.process;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTLexicalParser;
import eclipse.jdt.JDTParser;

public class PreProcessCompilationUnitHelper {
	
	public static String PreProcessDeleter(ICompilationUnit icu) { // , IJavaProject java_project
		CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
		cu.recordModifications();
		@SuppressWarnings("unchecked")
		List<Comment> comments = cu.getCommentList();
		if (comments != null) {
			for (Comment comment : comments) {
				comment.accept(new CommentRemover());
			}
		}
		return cu.toString();
	}
	
	public static String PreProcessDeleter(File f) {
		CompilationUnit cu = JDTLexicalParser.ParseJavaFile(f);
		cu.recordModifications();
		@SuppressWarnings("unchecked")
		List<Comment> comments = cu.getCommentList();
		if (comments != null) {
			for (Comment comment : comments) {
				comment.accept(new CommentRemover());
			}
		}
		return cu.toString();
	}
	
}
