package main;

import java.io.File;

import util.FileUtil;

public class EnvClear {
	
	public static void main(String[] args) {
		File bpe_mj = new File(Application.bpe_merges_json);
		File bpe_ttj = new File(Application.bpe_token_times_json);
		
		if (bpe_mj.exists()) {
			bpe_mj.delete();
		}
		if (bpe_ttj.exists()) {
			bpe_ttj.delete();
		}
		
		File f = new File(MetaOfApp.DataDirectory);
		if (f.exists()) {
			FileUtil.DeleteFile(f);
		}
		System.out.println("Environment has been Cleared Successfully!");
	}
	
}