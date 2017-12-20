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
	
	public void SaveToFile(File dest) {
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter(dest.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			Iterator<Tensor> titr = tensors.iterator();
			while (titr.hasNext()) {
				Tensor t = titr.next();
				bw.write(t.toString());
				bw.newLine();
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
		}
	}
	
}
