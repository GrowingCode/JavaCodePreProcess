package main;

import java.io.File;

import util.FileUtil;

public class EnvClear {
	
	public static void ClearEnv(boolean remove_bpe_merges, boolean remove_sktpe_merges) {
		if (remove_bpe_merges) {
			File bpe_mj = new File(AppMainRoot.bpe_merges_json);
			if (bpe_mj.exists()) {
				bpe_mj.delete();
			}
		}
//		File bpe_ttj = new File(Application.bpe_token_times_json);
//		if (bpe_ttj.exists()) {
//			bpe_ttj.delete();
//		}
		if (remove_sktpe_merges) {
			File skt_mj = new File(AppMainRoot.sktpe_merges_json);
			if (skt_mj.exists()) {
				skt_mj.delete();
			}
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
	
	public static void main(String[] args) {
		ClearEnv(true, true);
	}
	
}
