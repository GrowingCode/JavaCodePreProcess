package bpe.skt.util;

import main.MetaOfApp;

public class SktMergeApplyCondition {
	
	public static boolean MeetTrainTestJoinCondition(int exist_in_train, int exist_in_test) {
		return exist_in_train > 0 && exist_in_test > 0 && exist_in_train * MetaOfApp.TrainTestJoinCondition < exist_in_test;
	}
	
}
