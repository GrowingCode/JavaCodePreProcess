package main;

import java.io.File;

import logger.DebugLogger;
import util.FileUtil;

public class Meta {
	
	public final static String DataDirectory = System.getProperty("user.home") + "/AST_Tensors";
	static {
		DebugLogger.Log("===== Data Directory:" + System.getProperty("user.home") + "/AST_Tensors" + "; created!!! =====");
		File dd = new File(DataDirectory);
		if (dd.exists()) {
			FileUtil.DeleteFile(dd);
		}
		dd.mkdir();
	}
}
