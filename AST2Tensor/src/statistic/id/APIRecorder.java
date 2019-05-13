package statistic.id;

import java.util.TreeSet;

public class APIRecorder {
	
	TreeSet<String> api_combs = new TreeSet<String>();
//	TreeSet<String> not_hit_train = new TreeSet<String>();
	
	public APIRecorder() {
	}
	
	public void RecordAPIComb(String api_comb) {
		api_combs.add(api_comb);
//		not_hit_train.remove(type_content);
	}
	
//	public void APINotHitInTrainSet(String type_content) {
//		if (!hit_train.contains(type_content)) {
//			not_hit_train.add(type_content);
//		}
//	}
	
}
