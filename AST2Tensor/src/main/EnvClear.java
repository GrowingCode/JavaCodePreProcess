package main;

import java.io.File;

import util.FileUtil;

public class EnvClear {
	
	public static void main(String[] args) {
		File bpe_mj = new File(Application.bpe_merges_json);
		if (bpe_mj.exists()) {
			bpe_mj.delete();
		}
//		File bpe_ttj = new File(Application.bpe_token_times_json);
//		if (bpe_ttj.exists()) {
//			bpe_ttj.delete();
//		}
		File skt_mj = new File(Application.sktpe_merges_json);
		if (skt_mj.exists()) {
			skt_mj.delete();
		}
		
		File f1 = new File(MetaOfApp.DataDirectory);
		if (f1.exists()) {
			FileUtil.DeleteFile(f1);
		}
		File f2 = new File(MetaOfApp.MetaDirectory);
		if (f2.exists()) {
			FileUtil.DeleteFile(f2);
		}
		System.out.println("Environment has been Cleared Successfully!");
	}
	
}
