package translation.tensor.serialize;

import translation.tensor.TensorForProject;

public class SaveTensorToFile {

	// , File dest, File debug_dest, File oracle_dest
	public static void SaveTensors(TensorForProject one_project_tensor, String kind) {
		one_project_tensor.SaveToFile(kind);// dest, debug_dest, oracle_dest
	}
	
}
