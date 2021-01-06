package main.util;

import java.util.List;

import statis.trans.project.STProject;
import statistic.IDTools;
import translation.TensorTools;

public class AppRunUtil {
	
	// int max_handle_projs, RoleAssigner role_assigner
	// File root_dir
	public static void HandleEachProjectFramework(List<STProject> projs, HandleOneProject run, IDTools id_tool,
			TensorTools tensor_tool) {
//		System.err.println(root_dir.getAbsolutePath());
//		File[] files = root_dir.listFiles();
//		int count_projs = 0;
//		int all_size = 0;
//		for (File f : files) {
		for (STProject proj : projs) {
//			if (max_handle_projs >= 0 && count_projs >= max_handle_projs) {
//				break;
//			}
//			all_size += 
			run.Handle(proj, id_tool, tensor_tool);
//			if (f.isDirectory()) {
////				count_projs++;
////				f.getAbsolutePath(), all_size
//				all_size += run.Handle(proj, id_tool, tensor_tool);
//			} else {
//				Assert.isTrue(false);
//				File unzip_out_dir = new File(TemporaryUnzipWorkingSpace);
//				if (unzip_out_dir.exists()) {
//					FileUtil.DeleteFile(unzip_out_dir);
//				}
//				unzip_out_dir.mkdirs();
//				if (f.getName().endsWith(".zip")) {
//					try {
//						ZIPUtil.Unzip(f, unzip_out_dir);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
////					unzip_out_dir.getAbsolutePath(), all_size
//					all_size += run.Handle(proj, id_tool, tensor_tool);
//				}
////				if (unzip_out_dir.exists()) {
////					FileUtil.DeleteFile(unzip_out_dir);
////				}
//			}
		}
	}
	
}
