package translation.tensor.serialize;

import java.io.File;

import translation.tensor.TensorForProject;

public class SaveTensorToFile {

	public static void SaveTensors(TensorForProject one_project_tensor, File dest) {
		one_project_tensor.SaveToFile(dest);
	}
	
}
