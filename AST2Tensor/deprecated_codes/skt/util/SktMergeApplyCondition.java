package bpe.skt.util;

import main.MetaOfApp;

public class SktMergeApplyCondition {
	
	public static boolean MeetTrainTestJoinCondition(int exist_in_train, int exist_in_test) {
		boolean basic_cond = exist_in_train > 0 && exist_in_test > 0 && exist_in_train + exist_in_test >= MetaOfApp.MinimumThresholdOfAppyingSkeletonMerge;
		boolean one = exist_in_train * MetaOfApp.TrainTestJoinCondition < exist_in_test;
		boolean two = exist_in_test * MetaOfApp.TrainTestJoinCondition < exist_in_train;
		return basic_cond && one && two;
	}
	
}
