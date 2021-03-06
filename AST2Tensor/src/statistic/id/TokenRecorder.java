package statistic.id;

import statistic.id.util.DCapacityMap;

public class TokenRecorder {

	// private static IDManager unique = new IDManager();
	//
	// public static IDManager Instance() {
	// return unique;
	// }
	
//	private TreeMap<String, Boolean> type_is_leaf = new TreeMap<String, Boolean>();

//	private TreeMap<String, Boolean> type_content_is_leaf = new TreeMap<String, Boolean>();

//	private TreeMap<String, Integer> type_count = new TreeMap<String, Integer>();
//	private TreeMap<String, TreeMap<String, Integer>> type_content_count = new TreeMap<String, TreeMap<String, Integer>>();

//	private TreeMap<String, Integer> type_content_count = new TreeMap<String, Integer>();

//	private TreeMap<String, Boolean> ast_type_is_leaf = new TreeMap<String, Boolean>();

//	TreeMap<String, Integer> hit_train = new TreeMap<String, Integer>();
//	TreeMap<String, Integer> not_hit_train = new TreeMap<String, Integer>();
	
	DCapacityMap<String, Integer> hit_train = null;
	DCapacityMap<String, Integer> not_hit_train = null;
	
//	TreeMap<String, String> hit_train_parent_type_content = new TreeMap<String, String>();
//	TreeMap<String, String> not_hit_train_parent_type = new TreeMap<String, String>();

	public TokenRecorder(int capacity) {
		hit_train = new DCapacityMap<String, Integer>(capacity);
		not_hit_train = new DCapacityMap<String, Integer>(capacity);
		for (String r_w : IDManager.reserved_words) {
//			"null", 
			TokenHitInTrainSet(r_w, Integer.MAX_VALUE/2);
		}
	}

//	private void RegistTypeIsLeaf(String type, boolean is_leaf) {
//		if (ast_type_is_leaf.containsKey(type)) {
//			Assert.isTrue(is_leaf == ast_type_is_leaf.get(type).booleanValue(), "Wrong type:" + type);
//		} else {
//			ast_type_is_leaf.put(type, is_leaf);
//		}
//	}

//	String parent_type_content, 
	public void TokenHitInTrainSet(String type_content, Integer count) {
//		type_content = PreProcessContentHelper.PreProcessTypeContent(type_content);
//		hit_train.add(type_content);
//		System.out.println("hit_train type_content:" + type_content);
		Integer h_count = hit_train.GetValueBasedOnKey(type_content);
		if (h_count == null) {
			h_count = 0;
		}
		h_count += count;
		
//		Integer val = ;
		if (not_hit_train.ContainsKey(type_content)) {
			Integer nh_count = not_hit_train.RemoveKey(type_content);
			h_count += nh_count;
		}
//		Assert.isTrue(val != null);
		
//		if (val != null) {
//			h_count += val;
//		}
		hit_train.PutKeyValue(type_content, h_count);
//		hit_train.put(type_content, h_count);
//		hit_train_parent_type_content.put(type_content, parent_type_content);
	}

	public void TokenNotHitInTrainSet(String type_content, Integer count) {
//		System.out.println("not_hit_train type_content:" + type_content);
		Integer h_count = hit_train.GetValueBasedOnKey(type_content);
		if (h_count == null) {
			Integer nh_count = not_hit_train.GetValueBasedOnKey(type_content);
			if (nh_count == null) {
				nh_count = 0;
			}
			nh_count += count;
			not_hit_train.PutKeyValue(type_content, nh_count);
		} else {
			h_count += count;
			hit_train.PutKeyValue(type_content, h_count);
		}
	}

//	public static boolean LeafIsFixed(ASTNode node) {
//		boolean is_fixed_leaf = node instanceof Modifier || node instanceof PrimitiveType || node instanceof BooleanLiteral || node instanceof NullLiteral || node instanceof Dimension || node instanceof ThisExpression || node instanceof Block || node instanceof BreakStatement || node instanceof SwitchCase || node instanceof WildcardType || node instanceof ContinueStatement || node instanceof ReturnStatement || node instanceof SuperConstructorInvocation || node instanceof ArrayInitializer || node instanceof EmptyStatement || node instanceof ConstructorInvocation || node instanceof AnonymousClassDeclaration;
//		boolean is_unfixed_leaf = node instanceof SimpleName || node instanceof NumberLiteral || node instanceof CharacterLiteral || node instanceof StringLiteral;
//		Assert.isTrue(is_unfixed_leaf || is_fixed_leaf, "Strange node class:" + node.getClass().getSimpleName() + "#node:" + node);
//		return is_fixed_leaf;
//	}

//	public void EncounterANode(String type_content, boolean is_leaf) {
////		int pos = type_content.indexOf('#');
////		String type = type_content.substring(0, pos);
////		RegistTypeIsLeaf(type);//, is_leaf
//		if (type_content_is_leaf.containsKey(type_content)) {
//			Assert.isTrue(is_leaf == type_content_is_leaf.get(type_content));
//		} else {
//			type_content_is_leaf.put(type_content, is_leaf);
//		}
//		{
//			Integer tc = type_content_count.get(type_content);
//			if (tc == null) {
//				tc = 0;
//			}
//			tc++;
//			type_content_count.put(type_content, tc);
//		}
////		{
////			TreeMap<String, Integer> tcc = type_content_count.get(type_content);
////			if (tcc == null) {
////				tcc = new TreeMap<String, Integer>();
////				type_content_count.put(type, tcc);
////			}
////			content = PreProcessContentHelper.PreProcessContent(content);
////			Integer cc = tcc.get(content);
////			if (cc == null) {
////				cc = 0;
////			}
////			cc++;
////			tcc.put(content, cc);
////		}
//	}

//	// warning: this version can only be invoked once
//	public Set<String> RefineHitTrain(int removes) {
//		List<Entry<String, Integer>> ht = MapUtil.SortMapByValue(hit_train);
////		System.out.println("ht.get(0).getValue():" + ht.get(0).getValue());
//		Set<String> remove_keys = new TreeSet<String>();
////		Map<String, Integer> parent_type_remove_num = new TreeMap<String, Integer>();
//		int a_rm = 0;
////		System.out.println("ht.size():" + ht.size());
//		for (int i=0;i<ht.size();i++) {
//			Entry<String, Integer> e = ht.get(i);
//			String key = e.getKey();
//			Integer value = e.getValue();
//			System.out.println("==== Removed key:" + key + "#count:" + value + " ====");
////			String parent_type = hit_train_parent_type_content.get(key);
////			System.out.println("parent_type:" + parent_type);
////			if (parent_type.endsWith("L")) {
////				Integer rm = parent_type_remove_num.get(parent_type);
////				if (rm == null) {
////					rm = 0;
////				}
////				rm++;
////				parent_type_remove_num.put(parent_type, rm);
////				if (rm <= MetaOfApp.MaxParentTypeRemoveTimes) {
//					remove_keys.add(key);
//					a_rm++;
////				}
////			}
//			if (a_rm == removes) {
//				break;
//			}
//		}
//		Set<String> ks = hit_train.keySet();
////		PrintUtil.PrintSet(remove_keys, "Unk TypeContent");
//		ks.removeAll(remove_keys);
//		return ks;
////		TreeMap<String, TreeMap<String, Integer>> all_pre_classified_type_content_count = GenerateInternalTypeContentCascadedMap();
////		type_content_count.clear();
////		// now, all data in type_content_count has already been put into
////		// pre_classified_type_content_count
////		// trim and put data in pre_classified_type_content_count back to
////		// type_content_count
////		Set<String> t_keys = all_pre_classified_type_content_count.keySet();
////		Iterator<String> t_itr = t_keys.iterator();
////		while (t_itr.hasNext()) {
////			String t_key = t_itr.next();
////			TreeMap<String, Integer> pre_classified_content_count = all_pre_classified_type_content_count.get(t_key);
////			List<Entry<String, Integer>> sorted = MapUtil.SortMapByValue(pre_classified_content_count);
////			Iterator<Entry<String, Integer>> si = sorted.iterator();
////			int index = -1;
////			while (si.hasNext()) {
////				index++;
////				Entry<String, Integer> si_e = si.next();
////				String content = si_e.getKey();
////				int content_count = si_e.getValue();
////				String tc = t_key + "#" + content;
////				if (index == 0 && content_count == 1) {
////					Boolean tc_is_leaf = type_content_is_leaf.get(tc);
////					Assert.isNotNull(tc_is_leaf);
////					String new_tc = t_key + "#" + IDManager.DefaultPart;
////					if (!new_tc.equals(tc)) {
////						type_content_is_leaf.remove(tc);
////						type_content_is_leaf.put(new_tc, tc_is_leaf);
////						System.out.println("replaced with default UNK tc:" + tc + ";new_tc:" + new_tc);
////						tc = new_tc;
////					}
////				}
////				type_content_count.put(tc, content_count);
////			}
////		}
//	}

//	public void RefineAllStatistics(int minsupport, int maxcapacity) {
//		TreeMap<String, TreeMap<String, Integer>> all_pre_classified_type_content_count = GenerateTypeContentCount();
//		// now, all data in type_content_count has already been put into pre_classified_type_content_count
//		type_content_count.clear();
//		// trim and put data in pre_classified_type_content_count back to type_content_count
//		Set<String> t_keys = all_pre_classified_type_content_count.keySet();
//		Iterator<String> t_itr = t_keys.iterator();
//		while (t_itr.hasNext()) {
//			String t_key = t_itr.next();
//			int discount = 20;
//			if (t_key.equals(SimpleName.class.getSimpleName())) {
//				discount = 1;
//			}
//			TreeMap<String, Integer> pre_classified_content_count = all_pre_classified_type_content_count.get(t_key);
//			RefineOneContentCount(pre_classified_content_count, minsupport, maxcapacity/discount);
//			Set<String> content_count_keys = pre_classified_content_count.keySet();
//			Iterator<String> content_itr = content_count_keys.iterator();
//			while (content_itr.hasNext()) {
//				String content = content_itr.next();
//				int content_count = pre_classified_content_count.get(content);
//				type_content_count.put(t_key + "#" + content, content_count);
//			}
//		}
////		if (count.size() + safe.size() <= maxcapacity) {
////			return;
////		}
////		Set<String> ckeys = count.keySet();
////		Iterator<String> ckitr = ckeys.iterator();
////		while (ckitr.hasNext()) {
////			String ck = ckitr.next();
////			int ct = count.get(ck);
////			if (ct >= minsupport) {
////				safe.put(ck, ct);
////			}
////		}
////		count.clear();
//	}

//	private TreeMap<String, TreeMap<String, Integer>> GenerateInternalTypeContentCascadedMap() {
//		TreeMap<String, TreeMap<String, Integer>> all_pre_classified_type_content_count = new TreeMap<String, TreeMap<String, Integer>>();
//		Set<String> tc_keys = type_content_count.keySet();
//		Iterator<String> tc_itr = tc_keys.iterator();
//		while (tc_itr.hasNext()) {
//			String tc_key = tc_itr.next();
//			int tc_count = type_content_count.get(tc_key);
//			int pos = tc_key.indexOf('#');
//			String type = tc_key.substring(0, pos);
//			String content = tc_key.substring(pos+1);
//			TreeMap<String, Integer> pre_classified_type_content_count = all_pre_classified_type_content_count.get(type);
//			if (pre_classified_type_content_count == null) {
//				pre_classified_type_content_count = new TreeMap<String, Integer>();
//				all_pre_classified_type_content_count.put(type, pre_classified_type_content_count);
//			}
//			Integer count = pre_classified_type_content_count.get(content);
//			if (count == null) {
//				count = 0;
//			}
//			count += tc_count;
//			pre_classified_type_content_count.put(content, count);
//		}
//		return all_pre_classified_type_content_count;
//	}

//	private void RefineOneContentCount(TreeMap<String, Integer> pre_classified_content_count, int minsupport, int maxcapacity) {
//		int length = pre_classified_content_count.size();
//		if (length <= maxcapacity) {
//			return;
//		}
//		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(pre_classified_content_count.entrySet());
//		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
//			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
//				return o1.getValue().compareTo(o2.getValue());
//			}
//		});
//		pre_classified_content_count.clear();
//		int need_subtract = 0;
//		for (int i = length-maxcapacity; i<length; i++) {
//			Map.Entry<String, Integer> entry = list.get(i);
//			String entry_key = entry.getKey();
//			Integer entry_value = entry.getValue();
//			if (i == length-maxcapacity) {
//				need_subtract = Math.max(0, entry_value - minsupport);// entry_value > minsupport ? minsupport : 0
//			}
//			pre_classified_content_count.put(entry_key, entry_value - need_subtract);
//		}
////		for (Map.Entry<String, Integer> mapping : list) {
////			System.out.println(mapping.getKey() + ":" + mapping.getValue());
////		}
//	}

//	public void FinalRefineAllStatistics(int minsupport, int maxcapacity) {
//		boolean skip_minsupport = count.size() + safe.size() <= maxcapacity;
//		Set<String> ckeys = count.keySet();
//		Iterator<String> ckitr = ckeys.iterator();
//		while (ckitr.hasNext()) {
//			String ck = ckitr.next();
//			int ct = count.get(ck);
//			if (ct >= minsupport || skip_minsupport) {
//				safe.put(ck, ct);
//			}
//		}
//		count.clear();
//	}

//	public void FullFillIDManager(IDManager im) {
////		im.RegistTypeIsLeaf(ast_type_is_leaf);
//		System.out.println("type_content_count.size():" + type_content_count.size());
//		Set<String> skeys = type_content_count.keySet();
//		Iterator<String> skitr = skeys.iterator();
//		while (skitr.hasNext()) {
//			String tc = skitr.next();
//			im.RegistTypeContentID(tc, type_content_is_leaf.get(tc), type_content_count.get(tc));
////			TreeMap<String, Integer> content_count = type_content_count.get(tc);
////			Set<String> ckeys = content_count.keySet();
////			Iterator<String> ckitr = ckeys.iterator();
////			while (ckitr.hasNext()) {
////				String cnt = ckitr.next();
////				im.RegistContentID(cnt, content_count.get(cnt));
////			}
//		}
//		im.GenerateTypeSummary();
//	}

}
