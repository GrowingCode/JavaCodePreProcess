package main;

import java.io.File;
import java.util.TreeMap;

import com.google.gson.Gson;

import translation.tensor.util.TokenKindUtil;
import util.FileUtil;

public class MetaOfApp {

	public final static String DataDirectory = System.getProperty("user.home") + "/AST_Tensors";
//	public final static boolean DetailDebugMode = false;
//	public final static boolean DetailFollowStatementDebugMode = false;
//	public final static boolean StatementsDetailDebugMode = true;
	
	public final static String MethodDeclarationSignaturePrefix = "$YStatementSig$:";
	public final static String ProjectDeclarationSignaturePrefix = "$YProjectSig$:";
	
	public static boolean PrintTensorInfoForEachExampleInTestSet = false;
	public final static String[] PrintTensorInfoKind = new String[] {"stmt"};
	
//	public final static int TypeHuffTreeStandardChildrenNum = 100;
//	public final static int ContentHuffTreeStandardChildrenNum = 1000;
//	public final static int TypeContentHuffTreeStandardChildrenNum = 10000;

	public static boolean ClassLevelTensorGeneration = false;

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

//	public static int AddZeroIfNoVariable = 0;

//	public final static boolean ReplaceLeastWithUnk = false;

//	public final static boolean CharInCascadeForm = false;

	public final static int NumberOfMerges = 2000;
	public final static int NumberOfSkeletonMerges = 1000;

//	public final static int MinimumNumberOfStatementsInAST = 10;
	public final static int MinimumNumberOfNodesInAST = 50;
	public final static int MaximumNumberOfNodesInAST = 5000;
	
//	public final static int NoChar = 0;
//	public final static int TokenChar = 1;
//	public final static int SubWordChar = 1;
//	public static int CharForm = NoChar;
	
	public final static int SkeletonIDBase = 5000000;
	
	public final static int ConservedContextLength = 25;
	
//	public static boolean InBPEForm = true;
	public static boolean UseLexicalToken = false;
	public static boolean VariableNoLimit = false;
	public static boolean UseApproximateVariable = true;
	public static boolean FurtherUseStrictVariable = false;
	public static boolean MethodNoLimit = false;
	public static boolean JavaFileNoLimit = true;

	public static boolean LeafTypeContentSeparate = false;
	
	public static boolean TrainValidTestFoldSeparationSpecified = true;
	
	public static boolean OutOfScopeReplacedByUnk = false;
	public final static int NumberOfSkeletonUnk = 2;
	public final static int MinimumNumberOfSkeletonVocabulary = 20;
	public final static int NumberOfUnk = 5;
	public final static int MinimumNumberOfVocabulary = 50;
	public final static int MaxParentTypeRemoveTimes = 1;
	
	public static boolean GenerateSkeletonToken = true;
	public static boolean GeneratePairEncodedSkeletonToken = false;
	
	public static boolean PrintTokenKindDebugInfo = false;
	
	public final static int ApproximateVarMode = TokenKindUtil.SimpleNameApproximateVariable;

	public static void SaveToDirectory() {
		Gson gson = new Gson();
		TreeMap<String, Integer> meta_of_ast2tensor = new TreeMap<String, Integer>();
//		meta_of_ast2tensor.put("AddZeroIfNoVariable", AddZeroIfNoVariable);
//		meta_of_ast2tensor.put("MaximumHandlingNodeNumInOneTree", MaximumHandlingNodeNumInOneTree);
//		meta_of_ast2tensor.put("MaximumStringLength", MaximumStringLength);
		meta_of_ast2tensor.put("NumberOfMerges", NumberOfMerges);
		meta_of_ast2tensor.put("MinimumNumberOfNodesInAST", MinimumNumberOfNodesInAST);
		meta_of_ast2tensor.put("MaximumNumberOfNodesInAST", MaximumNumberOfNodesInAST);
//		meta_of_ast2tensor.put("InBPEForm", InBPEForm ? 1 : 0);
		
		meta_of_ast2tensor.put("UseLexicalToken", UseLexicalToken ? 1 : 0);
//		meta_of_ast2tensor.put("TakeUnseenAsUnk", TakeUnseenAsUnk ? 1 : 0);
		meta_of_ast2tensor.put("VariableNoLimit", VariableNoLimit ? 1 : 0);
		meta_of_ast2tensor.put("MethodNoLimit", MethodNoLimit ? 1 : 0);
		meta_of_ast2tensor.put("JavaFileNoLimit", JavaFileNoLimit ? 1 : 0);
		String dir = System.getProperty("user.home") + "/ASTMeta";
		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
		}
		FileUtil.WriteToFile(new File(dir + "/" + "meta_of_ast2tensor.json"), gson.toJson(meta_of_ast2tensor));
	}

}
