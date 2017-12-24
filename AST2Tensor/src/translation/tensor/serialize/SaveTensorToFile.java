package translation.tensor.serialize;

import java.io.File;

import translation.tensor.TensorForProject;

public class SaveTensorToFile {

	public static void SaveTensors(TensorForProject one_project_tensor, File dest, File debug_dest, File oracle_dest) {
		one_project_tensor.SaveToFile(dest, debug_dest, oracle_dest);
	}
	
}
