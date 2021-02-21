package main;

import java.util.TreeMap;

import translation.tensor.util.TokenKindUtil;
import util.JsonPrintUtil;

public class MetaOfApp {

	public final static String DataDirectory = System.getProperty("user.home") + "/AST_Tensors";
	public final static String MetaDirectory = System.getProperty("user.home") + "/AST_Metas";
//	public final static boolean DetailDebugMode = false;
//	public final static boolean DetailFollowStatementDebugMode = false;
//	public final static boolean StatementsDetailDebugMode = true;
	
	public final static String MethodDeclarationSignaturePrefix = "$YStatementSig$:";
	public final static String ProjectDeclarationSignaturePrefix = "$YProjectSig$:";
	
	public static boolean PrintTensorInfoForEachExampleInTestSet = false;
	public final static String[] PrintTensorInfoKind = new String[] {"stmt"};
	
	public final static int all = 5;
	public final static int train_seen = 2;
	public final static int train = 2;
	public final static int valid = 2;
	public final static int test = 4;
	
//	public final static int TypeHuffTreeStandardChildrenNum = 100;
//	public final static int ContentHuffTreeStandardChildrenNum = 1000;
//	public final static int TypeContentHuffTreeStandardChildrenNum = 10000;

	public static boolean ClassLevelTensorGeneration = false;
	
	public static boolean OnlyDebugTreeMerge = false;
	
	public static boolean GenFilterTrainTestJoinTreeMerge = true;
	public static boolean ApplyTrainTestJoinTreeMerge = true;
	public static double TrainTestJoinCondition = 0.35;

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

	public final static int MaximumStringLength = 40;

	public final static int MaximumFollowingStatements = 5;

	public static boolean NotMergeIDLeaf = true;
	
//	public static int AddZeroIfNoVariable = 0;

//	public final static boolean ReplaceLeastWithUnk = false;

//	public final static boolean CharInCascadeForm = false;
	
	public final static int MinimumThresholdOfMerge = 250;
	public final static int MinimumThresholdOfSkeletonMerge = 250;
	public final static int MaximumNumberOfApplyingSkeletonMerge = 2000;
//	public final static int NumberOfMerges = 2000;
//	public final static int NumberOfSkeletonMerges = 1000;

//	public final static int MinimumNumberOfStatementsInAST = 10;
	public final static int MinimumNumberOfNodesInAST = 50;
	public final static int MaximumNumberOfNodesInAST = 5000;
	
//	public final static int NoChar = 0;
//	public final static int TokenChar = 1;
//	public final static int SubWordChar = 1;
//	public static int CharForm = NoChar;
	
	public final static int SkeletonIDBase = 0;
	
	public final static int ConservedContextLength = 50;
	
//	public static boolean InBPEForm = true;
	public static boolean UseLexicalToken = false;
	public static boolean VariableNoLimit = false;
	public static boolean UseApproximateVariable = true;
	public static boolean FurtherUseStrictVariable = false;
	public static boolean MethodNoLimit = false;
	public static boolean JavaFileNoLimit = true;

	public static boolean LeafTypeContentSeparate = false;
	
	public static boolean TrainValidTestFoldSeparationSpecified = true;
	
//	public final static boolean FilterFairly = true;
	public final static double FilterMinimumScore = 0.0;
	public final static double FilterRate = 0.2;
	
	public final static int MaximumTokenCapacity = 10000;
	public final static int MaximumSkeletonTokenCapacity = 5000;
	
	public final static int MinimumNotUnkAppearTime = 2;
	public final static int MinimumSkeletonNotUnkAppearTime = 2;
	public final static int MinimumPESkeletonNotUnkAppearTime = 2;
	public final static int MinimumEachSkeletonNotUnkAppearTime = 2;
	
	public static boolean OutOfScopeReplacedByUnk = true;
	public final static int NumberOfSkeletonUnk = 0;
	public final static int MinimumNumberOfSkeletonVocabulary = 10;
	public final static int NumberOfUnk = 0;
	public final static int MinimumNumberOfVocabulary = 10;
	public final static int MaxParentTypeRemoveTimes = 1;
	
	public static boolean GenerateSkeletonToken = true;
//	public static boolean GeneratePairEncodedSkeletonToken = false;
	
	public final static Class<?>[] ignore_ast_type = new Class<?>[] {};// StringLiteral.class
	public final static String[] ignore_ast_dft_value = new String[] {};// "\"@Str\""
	
	public static boolean PrintTokenKindDebugInfo = false;
	
	public final static int ApproximateVarMode = TokenKindUtil.SimpleNameApproximateVariable;

	public static void SaveToDirectory() {
//		Gson gson = new Gson();
		TreeMap<String, Integer> meta_of_ast2tensor = new TreeMap<String, Integer>();
//		meta_of_ast2tensor.put("AddZeroIfNoVariable", AddZeroIfNoVariable);
//		meta_of_ast2tensor.put("MaximumHandlingNodeNumInOneTree", MaximumHandlingNodeNumInOneTree);
//		meta_of_ast2tensor.put("MaximumStringLength", MaximumStringLength);
//		meta_of_ast2tensor.put("NumberOfMerges", NumberOfMerges);
		meta_of_ast2tensor.put("MinimumNumberOfNodesInAST", MinimumNumberOfNodesInAST);
		meta_of_ast2tensor.put("MaximumNumberOfNodesInAST", MaximumNumberOfNodesInAST);
//		meta_of_ast2tensor.put("InBPEForm", InBPEForm ? 1 : 0);
		meta_of_ast2tensor.put("GenerateSkeletonToken", GenerateSkeletonToken ? 1 : 0);
		
		meta_of_ast2tensor.put("UseLexicalToken", UseLexicalToken ? 1 : 0);
//		meta_of_ast2tensor.put("TakeUnseenAsUnk", TakeUnseenAsUnk ? 1 : 0);
		meta_of_ast2tensor.put("VariableNoLimit", VariableNoLimit ? 1 : 0);
		meta_of_ast2tensor.put("MethodNoLimit", MethodNoLimit ? 1 : 0);
		meta_of_ast2tensor.put("JavaFileNoLimit", JavaFileNoLimit ? 1 : 0);
//		String dir = System.getProperty("user.home") + "/AST_Metas";
//		File f = new File(MetaDirectory);
//		if (!f.exists()) {
//			f.mkdirs();
//		}
//		FileUtil.WriteToFile(new File(MetaDirectory + "/" + "meta_of_ast2tensor.json"), gson.toJson(meta_of_ast2tensor));
		JsonPrintUtil.PrintMapToJsonFile(meta_of_ast2tensor, MetaDirectory, "meta_of_ast2tensor.json");
	}

}
