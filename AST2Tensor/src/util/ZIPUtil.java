package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZIPUtil {

	public static void Unzip(File f, File out_dir) throws ZipException, IOException {
		// File file = new File("/home/liangruihua/ziptest/test.zip");
		// get a ZipFile instance
		ZipFile zipFile = new ZipFile(f);
		// create a ZipInputStream instance
		ZipInputStream zis = new ZipInputStream(new FileInputStream(f));
		// create a ZipEntry instance , lay the every file from
		// decompress file temporarily
		ZipEntry entry = null;
		// a circle to get every file
		while ((entry = zis.getNextEntry()) != null) {
		//	System.out.println("decompress file :" + entry.getName());
			// define the path to set the file
			File outFile = new File(out_dir.getAbsolutePath() + "/" + entry.getName());
			// if the file's parent directory wasn't exits ,than
			// create the directory
			if (!outFile.getParentFile().exists()) {
				outFile.getParentFile().mkdirs();
			}
			// if the file not exits ,than create the file
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			// create an input stream
			BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
			// create an output stream
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
			byte[] b = new byte[1024];
			while (true) {
				int len = bis.read(b);
				if (len == -1)
					break;
				bos.write(b, 0, len);
			}
			// close stream
			bis.close();
			bos.close();
		}
		zis.close();
		zipFile.close();
	}
}