package translation.tensor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import main.Meta;

public class TensorForProject {
	
	List<Tensor> tensors = new LinkedList<Tensor>();
	
	public TensorForProject() {
	}
	
	public void AddTensors(List<Tensor> e) {
		tensors.addAll(e);
	}
	
	private void SaveToFile(List<Tensor> tensors_to, String type) {
		File dest = new File(Meta.DataDirectory + "/" + type + "_data.txt");
		File debug_dest = new File(Meta.DataDirectory + "/debug_" + type + "_data.txt");
		File oracle_dest = new File(Meta.DataDirectory + "/oracle_" + type + "_data.txt");
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
	
	public void SaveToFile() {// int total_of_tensors
		LinkedList<Tensor> train_tensors = new LinkedList<Tensor>();
		LinkedList<Tensor> test_tensors = new LinkedList<Tensor>();
		LinkedList<Tensor> valid_tensors = new LinkedList<Tensor>();
		Iterator<Tensor> titr = tensors.iterator();
		while (titr.hasNext()) {
			Tensor t = titr.next();
			if (t.GetRole() == 0) {
				train_tensors.add(t);
			} else if (t.GetRole() == 1) {
				valid_tensors.add(t);
			} else {
				test_tensors.add(t);
			}
		}
		SaveToFile(tensors, "all");
		SaveToFile(train_tensors, "train");
		SaveToFile(test_tensors, "test");
		SaveToFile(valid_tensors, "valid");
	}

	public int GetNumOfTensors() {
		return tensors.size();
	}
	
}
