package dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;

import statis.trans.common.RoleAssigner;
import util.FileIterator;
import util.FileUtil;
import util.RandomUtil;
import util.YEntry;
import util.ZIPUtil;

public class DataSetGenerator {

	private static final String TempFolder = System.getProperty("user.home") + "/TempFolder";
	private static final int GenerationTime = 1;
	private static final double RetainRate = 0.85;
	
//	private static final boolean FilterByRate = true;

	public DataSetGenerator() {
	}

	private Set<String> ReadIgnoreZip() {
		String path = this.getClass().getClassLoader().getResource("dataset/IgnoreZip").getPath();
//		System.out.println("path:" + path);
//		File f = new File(path);
//		System.out.println("path:" + f.getAbsolutePath());
		Set<String> list = new TreeSet<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	private List<File> FilterIgnoredZip(String zip_directory) {
		List<File> zips = new LinkedList<File>();
		Set<String> iz = ReadIgnoreZip();
		File zd = new File(zip_directory);
		if (zd.isDirectory()) {
			File[] all_files = zd.listFiles();
			for (File f : all_files) {
				if (f.getName().endsWith(".zip")) {
					if (!iz.contains(f.getName())) {
						if ((f.length() * 1.0) / 1024.0 / 1024.0 > 0) {
							zips.add(f);
						}
					}
				}
			}
		}
		return zips;
	}

	public void GenerateDataSet(String zip_directory, String dataset_directory, int number_of_select
//			, double expected_data_set_size
			) {
		List<File> zips = FilterIgnoredZip(zip_directory);
		System.out.println("zip size:" + zips.size());
		Collection<File> random_select_zips = RandomUtil.RandomFromCollection(zips, number_of_select);
		System.out.println("TempFolder:" + TempFolder);
		File tf = new File(TempFolder);
		if (tf.exists()) {
			FileUtil.DeleteFile(tf);
		}
		tf.mkdirs();
		Assert.isTrue(tf.isDirectory());
		String project_names = "";
		for (File f : random_select_zips) {
			String f_name = f.getName();
			System.out.println("selected file:" + f_name);
			int pos = f_name.lastIndexOf(".zip");
			String f_real_name = f_name.substring(0, pos);
			project_names += f_real_name + "#";
			File out_dir = new File(TempFolder + "/" + f_real_name);
			if (!out_dir.exists()) {
				out_dir.mkdirs();
			}
			Assert.isTrue(out_dir.isDirectory());
			try {
				ZIPUtil.Unzip(f, out_dir);
			} catch (Exception e) {
				System.err.println("Encountering error file:" + f.getName());
				e.printStackTrace();
				FileUtil.DeleteFile(new File(TempFolder));
				System.exit(1);
			}
		}
		project_names += "dataset";
		ArrayList<YEntry<Double, File>> java_files = new ArrayList<YEntry<Double, File>>();
		FileIterator fi = new FileIterator(tf.getAbsolutePath(), ".+\\.java$");
		Iterator<File> f_itr = fi.EachFileIterator();
		while (f_itr.hasNext()) {
			File f = f_itr.next();
			double score = JavaFileScorer.ScoreFile(f);
			if (score > 0.0) {
				java_files.add(new YEntry<Double, File>(score, f));
			}
		}
		Collections.sort(java_files, new Comparator<Map.Entry<Double, File>>() {
			public int compare(Entry<Double, File> o1, Entry<Double, File> o2) {
				return -o1.getValue().compareTo(o2.getValue());
			}
		});
		
		double real_expected_data_set_size = 0;
//		if (expected_data_set_size <= 0) {
		int s = java_files.size();
		
		// debug
//		for (int i=0;i<s;i++) {
//			YEntry<Double, File> java_file = java_files.get(i);
//			System.out.println("score:" + java_file.getKey());
//		}
		
		int r_s = (int)Math.ceil(s * RetainRate);
//		System.out.println("r_s:" + r_s); 
		for (int i=0;i<r_s;i++) {
			YEntry<Double, File> java_file = java_files.get(i);
			real_expected_data_set_size += (java_file.getValue().length() * 1.0) / 1024.0;
		}
//		} else {
//			real_expected_data_set_size += expected_data_set_size * 1024.0;
//		}
		
		double total_kb = real_expected_data_set_size;
		double train_kb_left = total_kb * 0.60;
		double valid_kb_left = total_kb * 0.15;
		double test_kb_left = total_kb * 0.25;
		Set<File> train_files = new HashSet<File>();
		Set<File> valid_files = new HashSet<File>();
		Set<File> test_files = new HashSet<File>();
		double total_socre = 0.0;
		double total_files = 0.0;
		for (int i=0;i<r_s;i++) {
//		for (YEntry<Double, File> java_file : java_files) {
			YEntry<Double, File> java_file = java_files.get(i);
			Double score = java_file.getKey();
			Assert.isTrue(score != null);
			total_socre += score;
			total_files++;
			File f = java_file.getValue();
			double f_size = f.length() * 1.0 / 1024.0;
			double refine_train_kb_left = (train_kb_left < 0 ? 0 : train_kb_left);
			double refine_valid_kb_left = (valid_kb_left < 0 ? 0 : valid_kb_left);
			double refine_test_kb_left = (test_kb_left < 0 ? 0 : test_kb_left);
			double current_total = refine_train_kb_left + refine_valid_kb_left + refine_test_kb_left;
			double mark = Math.random() * current_total;
			if (mark < refine_train_kb_left) {
				Assert.isTrue(train_kb_left >= 0);
				train_files.add(f);
				train_kb_left -= f_size;
			} else if (mark < refine_train_kb_left + refine_valid_kb_left) {
				Assert.isTrue(valid_kb_left >= 0);
				valid_files.add(f);
				valid_kb_left -= f_size;
			} else if (mark < refine_train_kb_left + refine_valid_kb_left + refine_test_kb_left) {
				Assert.isTrue(test_kb_left >= 0);
				test_files.add(f);
				test_kb_left -= f_size;
			} else {
				System.out.println("Strange!");
			}
			if (current_total <= 0) {
				break;
			}
		}
		BigDecimal bg_average_score = new BigDecimal(total_socre / total_files).setScale(2, RoundingMode.HALF_UP);
		double average_score = bg_average_score.doubleValue();
//		System.out.println("average_score:" + average_score);
		String average_score_str = String.format("%06d", (int)(average_score));
		String with_score_project_names = average_score_str + "#" + project_names;
		String dataset_folder = dataset_directory + "/" + with_score_project_names;
		File df = new File(dataset_folder);
		if (df.exists()) {
			FileUtil.DeleteFile(df);
		}
		df.mkdirs();
		String train_folder = dataset_folder + "/" + RoleAssigner.SpecifiedTrainFilePrefix + "Train";
		double train_size_in_MB = CopyToDirectory(train_files, train_folder, "training set");
		
		String valid_folder = dataset_folder + "/" + RoleAssigner.SpecifiedValidFilePrefix + "Valid";
		double valid_size_in_MB = CopyToDirectory(valid_files, valid_folder, "validing set");
		
		String test_folder = dataset_folder + "/" + RoleAssigner.SpecifiedTestFilePrefix + "Test";
		double test_size_in_MB = CopyToDirectory(test_files, test_folder, "testing set");
		
		BigDecimal bg_total = new BigDecimal(train_size_in_MB + valid_size_in_MB + test_size_in_MB).setScale(4, RoundingMode.HALF_UP);
		double total_in_MB = bg_total.doubleValue();
//		System.out.println("total_in_MB:" + total_in_MB);
		BigDecimal bg_train = new BigDecimal(train_size_in_MB/total_in_MB).setScale(4, RoundingMode.HALF_UP);
		double train_rate = bg_train.doubleValue() * 100;
		
		BigDecimal bg_valid = new BigDecimal(valid_size_in_MB/total_in_MB).setScale(4, RoundingMode.HALF_UP);
		double valid_rate = bg_valid.doubleValue() * 100;
		
		BigDecimal bg_test = new BigDecimal(test_size_in_MB/total_in_MB).setScale(4, RoundingMode.HALF_UP);
		double test_rate = bg_test.doubleValue() * 100;
		
		System.out.println(with_score_project_names + "#total size:" + total_in_MB + "MB#train_rate:" + train_rate + "%" + "#valid_rate:" + valid_rate + "%" + "#test_rate:" + test_rate + "%");
		
		FileUtil.DeleteFile(new File(TempFolder));
	}

	private double CopyToDirectory(Set<File> files, String folder, String exrta_info) {
//		System.out.println("Writing files to " + exrta_info);
		File folder_file = new File(folder);
		if (folder_file.exists()) {
			FileUtil.DeleteFile(folder_file);
		}
		folder_file.mkdirs();
		double total_size_in_KB = 0; // KB
		Iterator<File> fitr = files.iterator();
		while (fitr.hasNext()) {
			File f = fitr.next();
			total_size_in_KB += f.length() * 1.0 / 1024.0;
//			File f2 = new File(folder + "/" + f.getName());
//			if (f2.exists()) {
			File f2 = new File(folder + "/" + System.nanoTime() + "_" + f.getName());
//			}
			Assert.isTrue(!f2.exists());
			try {
				f2.createNewFile();
			} catch (IOException e) {
				System.err.println("File:" + f.getAbsolutePath() + " copy error!");
				e.printStackTrace();
				System.exit(1);
			}
//			System.out.println("f:" + f.getAbsolutePath());
			FileUtil.CopyFile(f, f2);
		}
		double total_size_in_MB = (total_size_in_KB/1024.0);
//		System.out.println("Total size: " + total_size_in_MB + "MB");
		return total_size_in_MB;
	}

	public static void main(String[] args) {
		for (int i=0;i<GenerationTime;i++) {
			DataSetGenerator dsg = new DataSetGenerator();
			dsg.GenerateDataSet(args[0], args[1], Integer.parseInt(args[2]));//, Double.parseDouble(args[3])
		}
	}

}
