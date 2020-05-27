package translation.tensor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import eclipse.project.ProjectInfo;
import main.MetaOfApp;
import statis.trans.common.RoleAssigner;

public class TensorForProject {
	
	String kind = null;
	List<Tensor> tensors = new LinkedList<Tensor>();
	
	public TensorForProject(String kind) {
		this.kind = kind;
	}
	
	public void AddTensors(List<Tensor> e) {
		tensors.addAll(e);
	}
	
	private void SaveToFile(List<Tensor> tensors_to, String type, ProjectInfo info) {
		File dest = new File(MetaOfApp.DataDirectory + "/" + type + "_data.txt");
		File debug_dest = new File(MetaOfApp.DataDirectory + "/debug_" + type + "_data.txt");
		File oracle_dest = new File(MetaOfApp.DataDirectory + "/oracle_" + type + "_data.txt");
		BufferedWriter bw = null;
		BufferedWriter debug_bw = null;
		BufferedWriter oracle_bw = null;
		try {
			FileWriter fw = new FileWriter(dest.getAbsoluteFile(), true);
			FileWriter debug_fw = new FileWriter(debug_dest.getAbsoluteFile(), true);
			FileWriter oracle_fw = new FileWriter(oracle_dest.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			debug_bw = new BufferedWriter(debug_fw);
			oracle_bw = new BufferedWriter(oracle_fw);
			oracle_bw.write(MetaOfApp.ProjectDeclarationSignaturePrefix + info.getName());
			oracle_bw.newLine();
			Iterator<Tensor> titr = tensors_to.iterator();
			while (titr.hasNext()) {
				Tensor t = titr.next();
				bw.write(t.toString());
				bw.newLine();
				debug_bw.write(t.toDebugString());
				debug_bw.newLine();
				oracle_bw.write(t.toOracleString());
				oracle_bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (debug_bw != null) {
				try {
					debug_bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (oracle_bw != null) {
				try {
					oracle_bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void SaveToFile(ProjectInfo info) {// int total_of_tensors
//		System.err.println("tensor_size:" + tensors.size());
		LinkedList<Tensor> train_tensors = new LinkedList<Tensor>();
		LinkedList<Tensor> test_tensors = new LinkedList<Tensor>();
		LinkedList<Tensor> valid_tensors = new LinkedList<Tensor>();
		Iterator<Tensor> titr = tensors.iterator();
		while (titr.hasNext()) {
			Tensor t = titr.next();
//			System.err.println("role:" + t.GetRole());
			if (t.GetRole() <= RoleAssigner.train_k) {
				train_tensors.add(t);
			} else if (t.GetRole() == RoleAssigner.valid_k) {
				valid_tensors.add(t);
			} else {
				test_tensors.add(t);
			}
		}
		SaveToFile(tensors, kind + "_all", info);
//		System.err.println("train_tensor_size:" + train_tensors.size());
		SaveToFile(train_tensors, kind + "_train", info);
//		System.err.println("test_tensor_size:" + test_tensors.size());
		SaveToFile(test_tensors, kind + "_test", info);
//		System.err.println("valid_tensor_size:" + valid_tensors.size());
		SaveToFile(valid_tensors, kind + "_valid", info);
		
		if (MetaOfApp.PrintTensorInfoForEachExampleInTestSet && Arrays.asList(MetaOfApp.PrintTensorInfoKind).contains(kind)) {
			System.err.println("print each tensor info in test set in project:" + info.getName());
			int index = 0;
			for (Tensor t : test_tensors) {
				index++;
				System.err.println("index:" + index + "#" + t.GetTensorInfo());
			}
		}
	}

//	public int GetNumOfTensors() {
//		return tensors.size();
//	}
	
}
