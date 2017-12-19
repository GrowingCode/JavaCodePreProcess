package statistic.id.serialize;

import java.io.File;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

import net.sf.json.JSONObject;
import statistic.id.IDManager;
import util.FileUtil;

public class SaveIDMapToFile {

	public static void SaveIDMaps(IDManager im) {
		Assert.isTrue(im.IsRefined());
		TreeMap<String, Integer> ati = im.GetAstTypeIDMap();
		TreeMap<Integer, TreeMap<String, Integer>> atci = im.GetAstTypeContentIDMap();
		JSONObject type_id_json = JSONObject.fromObject(ati);
		FileUtil.WriteToFile(new File("type_id.json"), type_id_json.toString());
		Set<String> akeys = ati.keySet();
		for (String ak : akeys) {
			Integer tcid = ati.get(ak);
			TreeMap<String, Integer> tci = atci.get(tcid);
			JSONObject type_content_id_json = JSONObject.fromObject(tci);
			FileUtil.WriteToFile(new File(ak + "_content_id.json"), type_content_id_json.toString());
		}
	}

}
