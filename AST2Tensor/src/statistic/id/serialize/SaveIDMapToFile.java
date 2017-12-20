package statistic.id.serialize;

import statistic.id.IDManager;

public class SaveIDMapToFile {

	public static void SaveIDMaps(IDManager im, String dir) {
		im.SaveToDirectory(dir);
	}

}
