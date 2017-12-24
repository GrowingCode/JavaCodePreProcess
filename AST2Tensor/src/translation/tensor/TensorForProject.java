package translation.tensor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TensorForProject {
	
	List<Tensor> tensors = new LinkedList<Tensor>();
	
	public TensorForProject() {
	}
	
	public void AddTensor(Tensor e) {
		tensors.add(e);
	}
	
	public void SaveToFile(File dest, File debug_dest, File oracle_dest) {
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
			Iterator<Tensor> titr = tensors.iterator();
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
	
}
