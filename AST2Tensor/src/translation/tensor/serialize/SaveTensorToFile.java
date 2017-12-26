package translation.tensor.serialize;

import translation.tensor.TensorForProject;

public class SaveTensorToFile {

	// , File dest, File debug_dest, File oracle_dest
	public static void SaveTensors(int num_of_tensors, TensorForProject one_project_tensor) {
		one_project_tensor.SaveToFile(num_of_tensors);// dest, debug_dest, oracle_dest
	}
	
}
