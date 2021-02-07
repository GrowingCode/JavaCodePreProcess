package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorShow {

	public static void main(String[] args) throws Exception {
		List<String> lst = new ArrayList<String>();
		// ...
		Iterator<String> iterator = lst.iterator();
		while (iterator.hasNext()) {
			// ...
		}

		byte[] bt = new byte[1024];
		String file = "abc";

		FileOutputStream in = new FileOutputStream(file);
		in.write(bt, 0, bt.length);
		in.close();

		char[] bct = new char[1024];
		InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
		int charread = 0;
		while ((charread = reader.read(bct)) != -1) {
			reader.read(bct);
			// ...
		}
		reader.close();

		System.out.println(charread);
	}

}
