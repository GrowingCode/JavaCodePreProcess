package translation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import eclipse.jdt.JDTParser;
import eclipse.search.EclipseSearchForICompilationUnits;
import logger.DebugLogger;
import main.MetaOfApp;
import statis.trans.common.YTreeGenerator;
import translation.ast.StatementLexicalTokenTensorGenerator;
import translation.ast.StatementSkeletonTensorGenerator;
import translation.ast.TreeTensorGenerator;
import translation.sequence.SequenceTensorGenerator;
import translation.tensor.StringTensor;
import translation.tensor.Tensor;
import translation.tensor.TensorForProject;

public class TensorGeneratorForProject {

	IJavaProject java_project = null;
	TensorTools tensor_tool = null;

	public TensorGeneratorForProject(IJavaProject java_project, TensorTools tensor_tool) {
		this.java_project = java_project;
		this.tensor_tool = tensor_tool;
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
			int total_method_count = 0;
			int unsuitable_method_count = 0;
//			PriorityQueue<SizePath> pq = new PriorityQueue<SizePath>();
			for (ICompilationUnit icu : units) {
//				if (Meta.DetailDebugMode) {
//					System.out.println("Geneate tensor for ICompilationUnit:" + icu.getPath().toString());
//				}
//				CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
//				// CreateJDTParserWithJavaProject(java_project).
//				TensorGenerator tg_tree = new TreeTensorGenerator(role_assigner, java_project, im, icu, cu);
//				TensorGenerator tg_sequence = new SequenceTensorGenerator(role_assigner, java_project, im, icu, cu);
//				// role_assigner, java_project, im, 
//				// TensorGenerator tg = new TensorGenerator(
//				//		role_assigner, java_project, im, icu, cu);
//				cu.accept(tg_tree);
//				cu.accept(tg_sequence);
//				List<Tensor> tree_tensors = tg_tree.GetGeneratedTensors();
//				result_tree.AddTensors(tree_tensors);
//				List<Tensor> sequence_tensors = tg_sequence.GetGeneratedTensors();
//				if (tree_tensors.size() != sequence_tensors.size()) {
//					System.err.println("Serious error! Tree tensors' num is not equal to Sequence tensors' num!");
//					System.exit(1);
//				}
//				Iterator<Tensor> tree_itr = tree_tensors.iterator();
//				Iterator<Tensor> seq_itr = sequence_tensors.iterator();
//				while (tree_itr.hasNext()) {
//					TreeTensor tree_tensor = (TreeTensor)tree_itr.next();
//					SequenceTensor seq_tensor = (SequenceTensor)seq_itr.next();
//					seq_tensor.SetTreeTensor(tree_tensor);
//				}
//				result_sequence.AddTensors(sequence_tensors);

				if (MetaOfApp.DetailDebugMode) {
					System.out.println("Geneate tensor for ICompilationUnit:" + icu.getPath().toString());
				}
				CompilationUnit cu = JDTParser.ParseICompilationUnit(icu);
				
				if (MetaOfApp.UseLexicalToken) {
					StatementLexicalTokenTensorGenerator tg_stmt_lex_visitor = new StatementLexicalTokenTensorGenerator(
							tensor_tool.role_assigner, tensor_tool.im, icu, cu);
					cu.accept(tg_stmt_lex_visitor);
					total_method_count += tg_stmt_lex_visitor.total_method_count;
					unsuitable_method_count += tg_stmt_lex_visitor.unsuitable_method_count;
					List<Tensor> stmt_lex_tensors = tg_stmt_lex_visitor.GetGeneratedTensors();
					Iterator<Tensor> s_itr = stmt_lex_tensors.iterator();
					List<Tensor> stmt_result_tensors = new LinkedList<Tensor>();
					while (s_itr.hasNext()) {
						StringTensor s = (StringTensor) s_itr.next();
						StringTensor r = new StringTensor();
						r.SetRole(s.GetRole());
						r.SetSize(s.getSize());
						r.SetToString(s.toString());
						r.SetToDebugString(s.toDebugString());
						r.SetToOracleString(s.toOracleString());
						stmt_result_tensors.add(r);
					}
					result_tree.AddTensors(stmt_result_tensors);
					result_sequence.AddTensors(stmt_result_tensors);
				} else {
					TreeTensorGenerator tg_tree_visitor = new TreeTensorGenerator(tensor_tool.im);
					SequenceTensorGenerator tg_stmt_visitor = new SequenceTensorGenerator(tensor_tool.im);
//					StatementTensorGenerator tg_stmt_tree_visitor = new StatementTensorGenerator(tensor_tool.im);
					YTreeGenerator tg_stmt = new YTreeGenerator(tensor_tool.role_assigner, tensor_tool.im, icu, cu,
							tg_stmt_visitor);
					YTreeGenerator tg_tree = new YTreeGenerator(tensor_tool.role_assigner, tensor_tool.im, icu, cu,
							tg_tree_visitor);
//					YTreeGenerator tg_stmt_sequence = new YTreeGenerator(tensor_tool.role_assigner, tensor_tool.im, icu, cu, tg_stmt_sequence_visitor);
					StatementSkeletonTensorGenerator tg_stmt_skt = new StatementSkeletonTensorGenerator(
							tensor_tool.role_assigner, tensor_tool.im, icu, cu);
					cu.accept(tg_stmt);
					cu.accept(tg_tree);
//					cu.accept(tg_stmt_sequence);
					cu.accept(tg_stmt_skt);
					total_method_count += tg_stmt.total_method_count;
					unsuitable_method_count += tg_stmt.unsuitable_method_count;
					
					List<Tensor> stmt_tensors = tg_stmt.GetGeneratedTensors();
					List<Tensor> tree_tensors = tg_tree.GetGeneratedTensors();
					List<Tensor> stmt_skt_tensors = tg_stmt_skt.GetGeneratedTensors();
					Assert.isTrue(stmt_tensors.size() == tree_tensors.size());
					Assert.isTrue(tree_tensors.size() == stmt_skt_tensors.size());
					List<Tensor> tree_result_tensors = new LinkedList<Tensor>();
					Iterator<Tensor> s_itr = stmt_tensors.iterator();
					Iterator<Tensor> t_itr = tree_tensors.iterator();
					Iterator<Tensor> sst_itr = stmt_skt_tensors.iterator();
					while (s_itr.hasNext()) {
						StringTensor s = (StringTensor) s_itr.next();
						StringTensor t = (StringTensor) t_itr.next();
						StringTensor sst = (StringTensor) sst_itr.next();
						Assert.isTrue(s.GetRole() == t.GetRole());
						Assert.isTrue(t.GetRole() == sst.GetRole());
						StringTensor r = new StringTensor();
						r.SetRole(s.GetRole());
						Assert.isTrue(s.getSize() + 1 == t.getSize(),
								"s.getSize():" + s.getSize() + ";t.getSize():" + t.getSize());
//						Assert.isTrue(s.getSize() == sst.getSize(),
//								"s.getSize():" + s.getSize() + ";sst.getSize():" + sst.getSize());
						r.SetSize(s.getSize());
						r.SetToString(s.toString() + "$" + t.toString() + "$" + sst.toString());
						r.SetToDebugString(s.toDebugString() + "\n\n\n\n$\n\n\n\n" + t.toDebugString() + "\n\n\n\n$\n\n\n\n"
								+ sst.toDebugString());
						r.SetToOracleString(s.toOracleString() + "\n\n\n\n$\n\n\n\n" + t.toOracleString()
								+ "\n\n\n\n$\n\n\n\n" + sst.toOracleString());
						tree_result_tensors.add(r);
					}
					result_tree.AddTensors(tree_result_tensors);
//					List<Tensor> sequence_tensors = tg_stmt_sequence.GetGeneratedTensors();
//					for (Tensor s_t : sequence_tensors) {
//						pq.add(new SizePath(s_t.getSize()));// , s_t.GetOriginFile()
//					}
					result_sequence.AddTensors(stmt_tensors);
					Assert.isTrue(tree_tensors.size() == stmt_tensors.size());
				}
			}
			System.out.println(
					"total_method_count:" + total_method_count + "#unsuitable_method_count:" + unsuitable_method_count);
		}
		return result;
	}

	// RoleAssigner role_assigner, IJavaProject java_project, IDManager im,
	// protected abstract TensorGenerator
	// GenerateTensorGeneratorVisitor(ICompilationUnit icu, CompilationUnit cu);
	// return new SequenceTensorGenerator(role_assigner, java_project, im, icu, cu);
	// return new TreeTensorGenerator(role_assigner, java_project, im, icu, cu);

}

class SizePath implements Comparable<SizePath> {

	Integer size = -1;
//	String path = null;

	public SizePath(int size) {
		// , String path
		this.size = size;
//		this.path = path;
	}

	@Override
	public int compareTo(SizePath o) {
		return -size.compareTo(o.size);
	}

}
