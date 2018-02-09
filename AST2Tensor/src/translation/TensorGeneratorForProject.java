package translation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTParser;
import eclipse.search.EclipseSearchForICompilationUnits;
import logger.DebugLogger;
import main.Meta;
import statistic.id.IDManager;
import translation.tensor.SequenceTensor;
import translation.tensor.Tensor;
import translation.tensor.TensorForProject;
import translation.tensor.TreeTensor;

public class TensorGeneratorForProject {
	
	RoleAssigner role_assigner = null;
	IJavaProject java_project = null;
	IDManager im = null;
	
	public TensorGeneratorForProject(RoleAssigner role_assigner, IJavaProject java_project, IDManager im) {
		this.role_assigner = role_assigner;
		this.java_project = java_project;
		this.im = im;
	}

	public List<TensorForProject> GenerateForOneProject() {
		List<TensorForProject> result = new LinkedList<TensorForProject>();
		TensorForProject result_tree = new TensorForProject("tree");
		TensorForProject result_sequence = new TensorForProject("sequence");
		result.add(result_tree);
		result.add(result_sequence);
		List<ICompilationUnit> units = null;
		try {
			units = EclipseSearchForICompilationUnits.SearchForAllICompilationUnits(java_project);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		DebugLogger.Log("Tensor: ICompilationUnit_size:" + units.size());
		if (units != null) {
			for (ICompilationUnit icu : units) {
				if (Meta.DetailDebugMode) {
					System.out.println("Geneate tensor for ICompilationUnit:" + icu.getPath().toString());
				}
				CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
				// CreateJDTParserWithJavaProject(java_project).
				TensorGenerator tg_tree = new TreeTensorGenerator(role_assigner, java_project, im, icu, cu);
				TensorGenerator tg_sequence = new SequenceTensorGenerator(role_assigner, java_project, im, icu, cu);
				// role_assigner, java_project, im, 
				// TensorGenerator tg = new TensorGenerator(
				//		role_assigner, java_project, im, icu, cu);
				cu.accept(tg_tree);
				cu.accept(tg_sequence);
				List<Tensor> tree_tensors = tg_tree.GetGeneratedTensors();
				result_tree.AddTensors(tree_tensors);
				List<Tensor> sequence_tensors = tg_sequence.GetGeneratedTensors();
				if (tree_tensors.size() != sequence_tensors.size()) {
					System.err.println("Serious error! Tree tensors' num is not equal to Sequence tensors' num!");
					System.exit(1);
				}
				Iterator<Tensor> tree_itr = tree_tensors.iterator();
				Iterator<Tensor> seq_itr = sequence_tensors.iterator();
				while (tree_itr.hasNext()) {
					TreeTensor tree_tensor = (TreeTensor)tree_itr.next();
					SequenceTensor seq_tensor = (SequenceTensor)seq_itr.next();
					seq_tensor.SetTreeTensor(tree_tensor);
				}
				result_sequence.AddTensors(sequence_tensors);
			}
		}
		return result;
	}
	
	// RoleAssigner role_assigner, IJavaProject java_project, IDManager im, 
	// protected abstract TensorGenerator GenerateTensorGeneratorVisitor(ICompilationUnit icu, CompilationUnit cu);
	// return new SequenceTensorGenerator(role_assigner, java_project, im, icu, cu);
	// return new TreeTensorGenerator(role_assigner, java_project, im, icu, cu);
	
}
