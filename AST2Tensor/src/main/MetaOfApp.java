package main;

public class MetaOfApp {
	
	public final static String DataDirectory = System.getProperty("user.home") + "/AST_Tensors";
	public final static boolean DetailDebugMode = false;
	public final static boolean StatementsDetailDebugMode = true;
	
//	public final static int TypeHuffTreeStandardChildrenNum = 100;
//	public final static int ContentHuffTreeStandardChildrenNum = 1000;
//	public final static int TypeContentHuffTreeStandardChildrenNum = 10000;
	
	public final static boolean ClassLevelTensorGeneration = false;
	
//	public final static int MaximumHandlingNodeNumInOneTree = 500;// 10
//	public final static double TreeShouldSplitNodeNumRate = 1.2;
//	public final static double TreeShouldCheckIntoNodeNumRate = 0.8;
//	public final static double MaximumInheritNodeNumRate = 0.4;
//	public final static double TreeAccumulatedShouldSplitNodeNumRate = 1.4;
//	public final static double BeATreeMinimumThresholdRate = 0.6;
//	public final static int BeATreeMinimumThresholdNodeNum = (int) Math.ceil((MaximumHandlingNodeNumInOneTree*1.0)*BeATreeMinimumThresholdRate);// 10
//	public final static double BeAContextMaximumThresholdRate = 1.2;
//	public final static int BeAContextMaximumThresholdNodeNum = (int) Math.floor((MaximumHandlingNodeNumInOneTree*1.0)*(BeAContextMaximumThresholdRate));
//	public final static int BeForcedAdoptedThresholdNodeNum = 25;
	
	public final static int MaximumStringLength = 100;
	
//	public final static boolean ReplaceLeastWithUnk = false;
	
	public final static boolean CharInCascadeForm = false;
	
	public static void SaveToDirectory(String dir) {
//		Gson gson = new Gson();
//		TreeMap<String, Integer> meta_of_ast2tensor = new TreeMap<String, Integer>();
//		meta_of_ast2tensor.put("MaximumHandlingNodeNumInOneTree", MaximumHandlingNodeNumInOneTree);
//		meta_of_ast2tensor.put("MaximumStringLength", MaximumStringLength);
//		FileUtil.WriteToFile(new File(dir + "/" + "Meta_of_ast2tensor.json"), gson.toJson(meta_of_ast2tensor));
	}
	
}
