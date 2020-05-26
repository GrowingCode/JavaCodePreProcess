package bpe.skt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTParser;
import eclipse.search.EclipseSearchForICompilationUnits;
import logger.DebugLogger;
import statistic.IDTools;
import translation.TensorTools;
import tree.Forest;

public class SktPEGeneratorForProject {
	
	IJavaProject java_project = null;
	IDTools tool = null;
	TensorTools tensor_tool = null;
	
	public SktPEGeneratorForProject(IJavaProject java_project, IDTools tool, TensorTools tensor_tool) {
		this.java_project = java_project;
		this.tool = tool;
		this.tensor_tool = tensor_tool;
	}
	
	public int GenerateForOneProject() {
		List<ICompilationUnit> units = null;
		try {
			units = EclipseSearchForICompilationUnits.SearchForAllICompilationUnits(java_project);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		DebugLogger.Log("ID: ICompilationUnit_size of project:" + units.size());
		int length = 0;
		if (units != null) {
			for (ICompilationUnit icu : units) {
				CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
				length += cu.getLength();
				// CreateJDTParserWithJavaProject(java_project).
				Assert.isTrue(icu != null);
				SktForestGenerator tg = new SktForestGenerator(null, icu, cu, tool);
				cu.accept(tg);
				ArrayList<Forest> funcs = tg.GetFunctions();
				tool.stf_r.AddForests(funcs);
			}
		}
		return length;
	}
	
}
