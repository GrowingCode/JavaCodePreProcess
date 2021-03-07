package util;

public class StringEquivalentUtil {
	
	public static String SktIDManagerEquivalent(String skt_info) {
		String res = null;
		switch (skt_info) {
		case "skt_one":
			res = "";
			break;
		case "skt_pe":
			res = "PE";
			break;
		case "skt_e":
			res = "Each";
			break;
		default:
			break;
		}
		return res;
	}
	
}
