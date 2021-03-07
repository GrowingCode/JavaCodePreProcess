package bpe.skt.debug;

public class TreeMergeTest {
	
//	public static void main(String[] args) {
//		TreeNode root = new TreeNode(InfixExpression.class, false, null, "#h+#h", null);
//		
//		TreeNode l1_1 = new TreeNode(InfixExpression.class, false, null, "#h+#h", null);
//		TreeNode l1_2 = new TreeNode(InfixExpression.class, false, null, "#h+#h", null);
//		TreeNode l2_1 = new TreeNode(InfixExpression.class, false, null, "#v+#v", null);
//		TreeNode l2_2 = new TreeNode(InfixExpression.class, false, null, "#v+#v", null);
//		TreeNode l2_3 = new TreeNode(InfixExpression.class, false, null, "#v+#v", null);
//		TreeNode l2_4 = new TreeNode(InfixExpression.class, false, null, "#v+#v", null);
//		
//		TreeNode c_1 = new TreeNode(SimpleName.class, true, null, "abc1", null);
//		TreeNode c_2 = new TreeNode(SimpleName.class, true, null, "abc1", null);
//		TreeNode c_3 = new TreeNode(SimpleName.class, true, null, "abc1", null);
//		TreeNode c_4 = new TreeNode(SimpleName.class, true, null, "abc1", null);
//		TreeNode c_5 = new TreeNode(SimpleName.class, true, null, "abc1", null);
//		TreeNode c_6 = new TreeNode(SimpleName.class, true, null, "abc1", null);
//		TreeNode c_7 = new TreeNode(SimpleName.class, true, null, "abc1", null);
//		TreeNode c_8 = new TreeNode(SimpleName.class, true, null, "abc1", null);
//		
//		root.AppendToChildren(l1_1);
//		root.AppendToChildren(l1_2);
//		l1_1.AppendToChildren(l2_1);
//		l1_1.AppendToChildren(l2_2);
//		l1_2.AppendToChildren(l2_3);
//		l1_2.AppendToChildren(l2_4);
//		
//		l2_1.AppendToChildren(c_1);
//		l2_1.AppendToChildren(c_2);
//		l2_2.AppendToChildren(c_3);
//		l2_2.AppendToChildren(c_4);
//		l2_3.AppendToChildren(c_5);
//		l2_3.AppendToChildren(c_6);
//		l2_4.AppendToChildren(c_7);
//		l2_4.AppendToChildren(c_8);
//		
//		TreeNodeTwoMerge tntm1 = new TreeNodeTwoMerge("#h+#h", 0, "#h+#h", "#h+#h+#h");
//		TreeNodeTwoMerge tntm2 = new TreeNodeTwoMerge("abc1", 0, "#v+#v", "abc1+#v");
//		TreeNodeTwoMerge tntm3 = new TreeNodeTwoMerge("abc1", 0, "abc1+#v", "abc1+abc1");
//		
//		Tree t = new Tree(root);
//		t.PreProcessTree();
//		t.ApplyMerge(tntm1);
//		t.ApplyMerge(tntm2);
//		t.ApplyMerge(tntm3);
//		
//		t.FlattenTree(false);
//		TreeFlatten tf = t.GetTreeFlattenResult();
//		PrintUtil.PrintList(tf.skt_pe_struct, "$$");
//		
//		TreeMap<String, ArrayList<PairContainer<TreeNode, TreeNode>>> pcnps = t.GetParentChildNodePairs();
//		Set<String> pcn_set = pcnps.keySet();
//		Iterator<String> pcn_itr = pcn_set.iterator();
//		while (pcn_itr.hasNext()) {
//			String pcn = pcn_itr.next();
//			ArrayList<PairContainer<TreeNode, TreeNode>> ps = pcnps.get(pcn);
//			Iterator<PairContainer<TreeNode, TreeNode>> pc_itr = ps.iterator();
//			while (pc_itr.hasNext()) {
//				PairContainer<TreeNode, TreeNode> pc = pc_itr.next();
//				int sib_idx = pc.k.GetChildren().indexOf(pc.v);
//				Assert.isTrue(sib_idx > -1, "pcn:" + pcn + "#ps size:" + ps.size() + "#pc.k.GetChildren().size():" + pc.k.GetChildren().size() + "#pc.k.GetContent():" + pc.k.GetContent());
//				System.out.println(pcn + "#sib_idx:" + sib_idx);
//			}
//		}
//	}
	
}
