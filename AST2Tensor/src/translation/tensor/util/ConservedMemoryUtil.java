package translation.tensor.util;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;

public class ConservedMemoryUtil {

	public static ArrayList<Integer> GenerateConservedMemory(ArrayList<Integer> token_var,
			ArrayList<Integer> token_var_relative, int conserved_context_length) {
//		ArrayList<Integer> result = new ArrayList<Integer>();
//		int len = token_var.size();
//		Assert.isTrue(len == token_var_relative.size());
//		for (int idx = 0; idx < len; idx++) {
//			int ccl = ConservedMemory(idx, token_var, conserved_context_length);
//			result.add(ccl);
//		}
		ArrayList<Integer> r_result = GenerateConservedMemoryEfficiency(token_var, token_var_relative,
				conserved_context_length);
//		int i_len = r_result.size();
//		Assert.isTrue(i_len == result.size());
//		PrintUtil.PrintList(result, "common result");
//		PrintUtil.PrintList(r_result, "efficient result");
//		for (int i = 0; i < i_len; i++) {
//			Assert.isTrue(result.get(i).equals(r_result.get(i)));
//		}
		return r_result;
	}

	private static ArrayList<Integer> GenerateConservedMemoryEfficiency(ArrayList<Integer> token_var,
			ArrayList<Integer> token_var_relative, int conserved_context_length) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int len = token_var.size();
		Assert.isTrue(len == token_var_relative.size());
//		Assert.isTrue(len > conserved_context_length, "len:" + len);
		int ccl = 0;
		int one_turn_len = Math.min(len, conserved_context_length);
		for (int idx = 0; idx < one_turn_len; idx++) {
			if (token_var.get(idx) > 0) {
				ccl++;
			}
			result.add(ccl);
		}
		for (int idx = conserved_context_length; idx < len; idx++) {
			if (token_var.get(idx) > 0) {
				ccl++;
			}
			if (token_var.get(idx - conserved_context_length) > 0) {
				ccl--;
			}
			result.add(ccl);
		}
		return result;
	}

//	private static Integer ConservedMemory(int ei, ArrayList<Integer> token_var, int conserved_context_length) {
//		int ccl = 0;
//		int si = Math.max(-1, ei - conserved_context_length);
//		for (int i = ei; i > si; i--) {
//			if (token_var.get(i) > 0) {
//				ccl++;
//			}
//		}
//		return ccl;
//	}

}
