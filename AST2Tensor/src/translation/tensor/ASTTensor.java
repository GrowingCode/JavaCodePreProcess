package translation.tensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Assert;

import main.MetaOfApp;
import statistic.id.IDManager;
import util.BooleanArrayUtil;
import util.SetUtil;

public class ASTTensor extends Tensor {

	public ASTTensor(IDManager im, String origin_file, int role) {
		super(im, origin_file, role);
	}
	
	ArrayList<StatementInfo> si_list = new ArrayList<StatementInfo>();

	Map<StatementInfo, ArrayList<Boolean>> depend_record = new HashMap<StatementInfo, ArrayList<Boolean>>();
	
	// local_token_id means the id for variable (memory address)
	// inner id is the id for current training example (due to for every token, running char sequence is impossible)
	
	// for tensor
	// base data
	// the first two columns are indicators.
	// the first column is the range of specially handled tokens
	// the second column is the range of nodes of statements
	// in the rest, the first row is the type_content_id
	//              the second row is the local_token_id
	// the third row is inner token index 
	// the forth row is api_group this token belongs to 
	
	// stmt info of tokens: ...|...|...
	// memory_index means the local token index also the variable index or type index
	// stmt info of tokens start end: se|se|se
	ArrayList<Integer> stmt_token_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_inner_index_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_variable_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_api_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_api_relative_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_info_start = new ArrayList<Integer>();
	ArrayList<Integer> stmt_token_info_end = new ArrayList<Integer>();
	
	// stmt info of variables: ``|```|``
	// stmt info of variables start end: se|se|se
	ArrayList<Integer> stmt_variable_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_variable_info_start = new ArrayList<Integer>();
	ArrayList<Integer> stmt_variable_info_end = new ArrayList<Integer>();
	
	// following legal stmt index: ,,|,,|,,,
	// following legal stmt index start end: se|se|se
	ArrayList<Integer> stmt_following_legal_info = new ArrayList<Integer>();
	ArrayList<Integer> stmt_following_legal_info_start = new ArrayList<Integer>();
	ArrayList<Integer> stmt_following_legal_info_end = new ArrayList<Integer>();
	
//	ArrayList<Integer> first_row = new ArrayList<Integer>();
//	ArrayList<Integer> second_row = new ArrayList<Integer>();
//	ArrayList<Integer> third_row = new ArrayList<Integer>();
//	ArrayList<Integer> forth_row = new ArrayList<Integer>();
	
	public static double min_rate_of_local_token = Double.MAX_VALUE;
	public static double max_rate_of_local_token = Double.MIN_VALUE;
	
//	ArrayList<Integer> parentTypeIdList = new ArrayList<Integer>();
	// decode data
//	ArrayList<Integer> relative_use = new ArrayList<Integer>();
//	ArrayList<Integer> de_pop_num = new ArrayList<Integer>();
	// encode data
//	ArrayList<Integer> up_relative_use = new ArrayList<Integer>();
//	ArrayList<Integer> up_contingent = new ArrayList<Integer>();
//	ArrayList<Integer> right_relative_use = new ArrayList<Integer>();
//	ArrayList<Integer> right_contingent = new ArrayList<Integer>();
//	ArrayList<Integer> en_pop_num = new ArrayList<Integer>();
//	ArrayList<Integer> seq_en_infer = new ArrayList<Integer>();
	// extra  data
//	ArrayList<Integer> to_encode = new ArrayList<Integer>();
//	ArrayList<Integer> isExistedList = new ArrayList<>();
//	ArrayList<Integer> lastIndexList = new ArrayList<>();
//	ArrayList<Integer> is_real = new ArrayList<Integer>();
	
	// for debug info
//	ArrayList<String> typeIdStrList = new ArrayList<String>();
//	ArrayList<String> parentTypeIdStrList = new ArrayList<String>();
	
	// int relative_use_index, int decode_pop_num, 
//	public int StoreOneNode(IDManager im, TypeContentID t_c, int token_local_id) {// , TypeContentID parent_t_c, int up_relative_use_num, int right_relative_use_num, int node_to_encode, int isExisted, int lastIndex, int node_is_real, int up_contingent_index, int right_contingent_index,
		// base data
//		type_content_id.add(t_c.GetTypeContentID());
//		local_token_id.add(token_local_id);
//		parentTypeIdList.add(parent_t_c.GetTypeContentID());
		// decode data
//		relative_use.add(relative_use_index);
//		de_pop_num.add(decode_pop_num);
		// encode data
//		up_relative_use.add(up_relative_use_num);
//		up_contingent.add(up_contingent_index);
//		right_relative_use.add(right_relative_use_num);
//		right_contingent.add(right_contingent_index);
//		to_encode.add(node_to_encode);
//		seq_en_infer.add(seq_encode_infer);
		// extra data
//        isExistedList.add(isExisted);
//		lastIndexList.add(lastIndex);
//		is_real.add(node_is_real);
		// debug data
//		typeIdStrList.add(t_c.GetTypeContent());
//		parentTypeIdStrList.add(parent_t_c.GetTypeContent());
//		return type_content_id.size()-1;
//	}
	
//	public int GetUpIndex(int node_index) {
//		return up.get(node_index);
//	}
	
	private String ToStmtInfo(String separator) {
		return StringUtils.join(stmt_token_info.toArray(), " ") + separator + stmt_token_inner_index_info + separator + StringUtils.join(stmt_token_variable_info.toArray(), " ") + separator + StringUtils.join(stmt_token_api_info.toArray(), " ") + separator + StringUtils.join(stmt_token_api_relative_info.toArray(), " ") + separator + StringUtils.join(stmt_token_info_start.toArray(), " ") + separator + StringUtils.join(stmt_token_info_end.toArray(), " ") + separator + StringUtils.join(stmt_variable_info.toArray(), " ") + separator + StringUtils.join(stmt_variable_info_start.toArray(), " ") + separator + StringUtils.join(stmt_variable_info_end.toArray(), " ") + separator + StringUtils.join(stmt_following_legal_info.toArray(), " ") + separator + StringUtils.join(stmt_following_legal_info_start.toArray(), " ") + separator + StringUtils.join(stmt_following_legal_info_end.toArray(), " ");
	}
	
	public String toBaseString(String separator) {
		ArrayList<Integer> inner_id_type_content_id = GenerateInnerIndexesForTypeContents();
		String f_string = StringUtils.join(inner_id_type_content_id.toArray(), " ") + separator + ToStmtInfo(separator);
//		StringUtils.join(first_row.toArray(), " ") + separator + StringUtils.join(second_row.toArray(), " ") + separator + StringUtils.join(third_row.toArray(), " ");
		return f_string;
	}
	
	public String toOracleBaseString(String separator) {
		return ToStmtInfo(separator);
	}
	
	public String toBaseExceptString(String separator) {
		// StringUtils.join(up_contingent.toArray(), " ") + separator + 
		// StringUtils.join(right_contingent.toArray(), " ") + separator + 
		// separator + StringUtils.join(seq_en_infer.toArray(), " ") + 
		// StringUtils.join(relative_use.toArray(), " ") + separator + StringUtils.join(de_pop_num.toArray(), " ") + separator + 
//		String f_string = StringUtils.join(up_relative_use.toArray(), " ") + separator + StringUtils.join(right_relative_use.toArray(), " ") + separator + StringUtils.join(to_encode.toArray(), " ") + separator + StringUtils.join(isExistedList.toArray(), " ") + separator + StringUtils.join(lastIndexList.toArray(), " ") + separator + StringUtils.join(is_real.toArray(), " ");
//		return f_string;
		return "";
	}

	@Override
	public String toString() {
		String separator = "#";
		String result = toBaseString(separator) + separator + toBaseExceptString(separator);
		return result.trim();
	}

	@Override
	public String toDebugString() {
		String separator = System.getProperty("line.separator");
		String result = toBaseString(separator) + separator + toBaseExceptString(separator);
		return result.trim();
	}

	@Override
	public String toOracleString() {
		String separator = System.getProperty("line.separator");
		String result = toOracleBaseString(separator) + separator + toBaseExceptString(separator);
		return result.trim();
	}

	@Override
	public int getSize() {
		return stmt_token_info.size();
	}
	
	public void Devour(StatementInfo last_stmt) {
		ArrayList<Boolean> record = depend_record.get(last_stmt);
		Assert.isTrue(record == null);
		record = new ArrayList<Boolean>();
		int dr_size = depend_record.size();
		Assert.isTrue(dr_size == si_list.size());
		for (int i=0;i<dr_size;i++) {
			record.add(false);
		}
		depend_record.put(last_stmt, record);
		// record conflict (encountered)
		Set<Integer> current_not_encountered_variables = new TreeSet<Integer>(last_stmt.var_or_type_ids_in_this_stmt);
		for (int i=dr_size-1;i>=0;i--) {
			if (record.get(i) == false) {
				// not depend
				StatementInfo i_si = si_list.get(i);
				Set<Integer> i_si_vars = i_si.var_or_type_ids_in_this_stmt;
				Set<Integer> encountered_vars = SetUtil.TheElementsInSetOneExistInSetTwo(current_not_encountered_variables, i_si_vars);
				if (encountered_vars.size() > 0) {
					current_not_encountered_variables.removeAll(encountered_vars);
					record.set(i, true);
					ArrayList<Boolean> i_si_record = depend_record.get(i_si);
					Assert.isTrue(i_si_record.size() == i);
					BooleanArrayUtil.BooleanArrayElementOr(record, i_si_record);
					if (current_not_encountered_variables.size() == 0) {
						break;
					}
				}
			}
		}
		// store non-conflict as following_legal
		for (int i=dr_size-1;i>=0;i--) {
			if (record.get(i) == false) {
				// not depend
				StatementInfo i_si = si_list.get(i);
				i_si.following_stmts_same_legal_as_this.add(dr_size);
			} else {
				break;
			}
		}
		si_list.add(last_stmt);
	}

//	private void HandleOneDevouredStatement(StatementInfo last_stmt) {
//		ArrayList<Integer> stmt_first_row = new ArrayList<Integer>();
//		ArrayList<Integer> stmt_second_row = new ArrayList<Integer>();
//		ArrayList<Integer> stmt_third_row = new ArrayList<Integer>();
//		ArrayList<Integer> stmt_forth_row = new ArrayList<Integer>();
//		// get representative token and its ID
//		Assert.isTrue(last_stmt.is_variable.size() == last_stmt.local_token_id.size() && last_stmt.local_token_id.size() == last_stmt.type_content_id.size());
//		int size = last_stmt.local_token_id.size();
//		Map<Integer, TokenInCare> token_in_care = new TreeMap<Integer, TokenInCare>();
//		for (int i=0;i<size;i++) {
//			Integer is_var = last_stmt.is_variable.get(i);
//			if (is_var >= 0) {
//				int l_tk_id = last_stmt.local_token_id.get(i);
//				int tp_cnt_id = last_stmt.type_content_id.get(i);
//				TokenInCare tic = token_in_care.get(tp_cnt_id);
//				if (tic == null) {
//					tic = new TokenInCare(tp_cnt_id, l_tk_id, is_var);
//					token_in_care.put(tp_cnt_id, tic);
//				}
//				tic.EncounterOne(i);
//			}
//		}
//		List<Map.Entry<Integer, TokenInCare>> local_id_represent_list = new ArrayList<Map.Entry<Integer, TokenInCare>>(token_in_care.entrySet());
//        Collections.sort(local_id_represent_list, new Comparator<Map.Entry<Integer, TokenInCare>>() {
//            public int compare(Entry<Integer, TokenInCare> o1,
//                    Entry<Integer, TokenInCare> o2) {
//                return o1.getValue().LocalIDCompareTo(o2.getValue());
//            }
//        });
////		List<Entry<Integer, TokenInCare>> sorted_tics = MapUtil.SortMapByValue(token_in_care);
//        Entry<Integer, TokenInCare> representative_token = null;
//		List<Map.Entry<Integer, TokenInCare>> represent_list = new ArrayList<Map.Entry<Integer, TokenInCare>>(token_in_care.entrySet());
//        Collections.sort(represent_list, new Comparator<Map.Entry<Integer, TokenInCare>>() {
//            public int compare(Entry<Integer, TokenInCare> o1,
//                    Entry<Integer, TokenInCare> o2) {
//                return o1.getValue().RepresentCompareTo(o2.getValue());
//            }
//        });
//		Iterator<Entry<Integer, TokenInCare>> st_itr = represent_list.iterator();
//		while (st_itr.hasNext()) {
//			Entry<Integer, TokenInCare> st_e = st_itr.next();
//			TokenInCare tic = st_e.getValue();
//			if (tic.local_token_id <= last_stmt.max_token_id_before_visiting_this_statement) {
//				representative_token = st_e;
//				break;
//			}
//		}
////		if (representative_token != null) {
////			sorted_tics.remove(representative_token);
////		}
//		local_id_represent_list.add(0, representative_token);
//		if (representative_token == null) {
//			local_id_represent_list.add(0, representative_token);
//		}
//		// generate all ids in statement
//		ArrayList<Integer> ids_first_row = new ArrayList<Integer>();
//		ArrayList<Integer> ids_second_row = new ArrayList<Integer>();
//		ArrayList<Integer> ids_third_row = new ArrayList<Integer>();
//		ArrayList<Integer> ids_forth_row = new ArrayList<Integer>();
////		Iterator<Entry<Integer, TokenInCare>> stcs_itr = sorted_tics.iterator();
//		Iterator<Entry<Integer, TokenInCare>> stcs_itr = local_id_represent_list.iterator();
//		while (stcs_itr.hasNext()) {
//			Entry<Integer, TokenInCare> e = stcs_itr.next();
//			if (e == null) {
//				ids_first_row.add(0);
//			} else {
//				ids_first_row.add(e.getValue().local_token_id);
//			}
//			ids_third_row.add(-1);
//			ids_forth_row.add(-1);
//		}
//		Iterator<Entry<Integer, TokenInCare>> stcs_itr2 = local_id_represent_list.iterator();
//		while (stcs_itr2.hasNext()) {
//			Entry<Integer, TokenInCare> e = stcs_itr2.next();
//			if (e == null) {
//				ids_second_row.add(first_row.size() + 2 + ids_first_row.size());
//			} else {
//				ids_second_row.add(first_row.size() + 2 + ids_first_row.size() + e.getValue().positions.get(0));
//			}
//		}
//		// generate all nodes in statement
//		ArrayList<Integer> nodes_first_row = new ArrayList<Integer>();
//		ArrayList<Integer> nodes_second_row = new ArrayList<Integer>();
//		ArrayList<Integer> nodes_third_row = new ArrayList<Integer>();
//		ArrayList<Integer> nodes_forth_row = new ArrayList<Integer>();
//		for (int i=0;i<size;i++) {
//			int tp_cnt_id = last_stmt.type_content_id.get(i);
//			nodes_first_row.add(tp_cnt_id);
//			nodes_second_row.add(last_stmt.local_token_id.get(i));
//			Integer inner = GenerateInnerIndexForTypeContent(tp_cnt_id);
//			nodes_third_row.add(inner);
//			nodes_forth_row.add(last_stmt.api_group.get(i));
//		}
//		stmt_first_row.add(first_row.size()+2);
//		stmt_second_row.add(first_row.size()+2+ids_first_row.size()-1);
//		stmt_third_row.add(-1);
//		stmt_forth_row.add(-1);
//		stmt_first_row.add(first_row.size()+2+ids_first_row.size());
//		stmt_second_row.add(first_row.size()+2+ids_first_row.size()+nodes_first_row.size()-1);
//		stmt_third_row.add(-1);
//		stmt_forth_row.add(-1);
//		// stmt rows
//		stmt_first_row.addAll(ids_first_row);
//		stmt_second_row.addAll(ids_second_row);
//		stmt_third_row.addAll(ids_third_row);
//		stmt_forth_row.addAll(ids_forth_row);
//		stmt_first_row.addAll(nodes_first_row);
//		stmt_second_row.addAll(nodes_second_row);
//		stmt_third_row.addAll(nodes_third_row);
//		stmt_forth_row.addAll(nodes_forth_row);
//		// final rows
//		first_row.addAll(stmt_first_row);
//		second_row.addAll(stmt_second_row);
//		third_row.addAll(stmt_third_row);
//		forth_row.addAll(stmt_forth_row);
//	}
	
	public void HandleAllDevoured() {
		int i_len = si_list.size();
		for (int i=0;i<i_len;i++) {
			StatementInfo last_stmt = si_list.get(i);
			stmt_token_info_start.add(stmt_token_info.size());
			stmt_token_info.addAll(last_stmt.type_content_id);
			for (Integer tid : last_stmt.type_content_id) {
				stmt_token_inner_index_info.add(GenerateInnerIndexForTypeContent(tid));
			}
			stmt_token_variable_info.addAll(last_stmt.local_token_id);
			stmt_token_api_info.addAll(last_stmt.api_group);
			stmt_token_api_relative_info.addAll(last_stmt.api_relative);
			stmt_token_info_end.add(stmt_token_info.size()-1);
			
			stmt_variable_info_start.add(stmt_variable_info.size());
			stmt_variable_info.addAll(last_stmt.var_or_type_ids_in_this_stmt);
			stmt_variable_info_end.add(stmt_variable_info.size()-1);

			stmt_following_legal_info_start.add(stmt_following_legal_info.size());
			int last_stmt_legal_follows = last_stmt.following_stmts_same_legal_as_this.size();
			{
				System.out.println("stmt:" + last_stmt.stmt + "#last_stmt_legal_follows:" + last_stmt_legal_follows);
				System.out.println("==== follow stmts begin ====");
				for (Integer follow_i : last_stmt.following_stmts_same_legal_as_this) {
					System.out.println("follow stmt:" + si_list.get(follow_i));
				}
				System.out.println("==== follow stmts end ====");
			}
			stmt_following_legal_info.addAll(last_stmt.following_stmts_same_legal_as_this.subList(0, Math.min(last_stmt_legal_follows, MetaOfApp.MaximumFollowingStatements)));
			stmt_following_legal_info_end.add(stmt_following_legal_info.size()-1);
		}
	}
	
	public void Validate() {
//		int node_num = 0;
//		int i=0;
//		while (i < first_row.size()) {
//			Integer local_token_start = first_row.get(i);
//			Integer local_token_end = second_row.get(i);
//			for (int j = local_token_start+1;j<=local_token_end;j++) {
//				Integer position_of_type_content = second_row.get(j);
//				Assert.isTrue(second_row.get(position_of_type_content).equals(first_row.get(j)) || (j == local_token_start+1 && second_row.get(position_of_type_content) == -1));
//			}
//			Integer type_content_start = first_row.get(i+1);
//			Integer type_content_end = second_row.get(i+1);
//			int lc_size = local_token_end - local_token_start;
//			int tc_size = type_content_end - type_content_start + 1;
//			double rate = (lc_size *1.0) / (tc_size * 1.0);
//			if (min_rate_of_local_token > rate) {
//				min_rate_of_local_token = rate;
//			}
//			if (max_rate_of_local_token < rate) {
//				max_rate_of_local_token = rate;
//			}
//			i = type_content_end + 1;
//			node_num += tc_size;
//		}
//		Assert.isTrue(node_num == total_node_num);
		int total_size = 0;
		int i_len = stmt_token_info_start.size();
		for (int i=0;i<i_len;i++) {
			Integer start_idx = stmt_token_info_start.get(i);
			Integer end_idx = stmt_token_info_end.get(i);

			int one_size = end_idx - start_idx + 1;
			total_size += one_size;
			int lc_size = stmt_variable_info_end.get(i) - stmt_variable_info_start.get(i) + 1;
			double rate = (lc_size *1.0) / (one_size * 1.0);
			if (min_rate_of_local_token > rate) {
				min_rate_of_local_token = rate;
			}
			if (max_rate_of_local_token < rate) {
				max_rate_of_local_token = rate;
			}
		}
		Assert.isTrue(total_size == stmt_token_info.size());
	}
	
}

class TokenInCare {
	
	int type_content_id = -1;
	int local_token_id = -1;
	int is_var = -1;
	int count = 0;
	List<Integer> positions = new LinkedList<Integer>();
	
	public TokenInCare(int type_content_id, int local_token_id, int is_var) {
		this.type_content_id = type_content_id;
		this.local_token_id = local_token_id;
		this.is_var = is_var;
	}
	
	public void EncounterOne(int index) {
		count++;
		positions.add(index);
	}
	
	public int RepresentCompareTo(TokenInCare o) {
		int var_cmp = -((Integer)is_var).compareTo(o.is_var);
		if (var_cmp == 0) {
			int count_cmp = ((Integer)count).compareTo(o.count);
			if (count_cmp == 0) {
				return positions.get(0).compareTo(o.positions.get(0));
			} else {
				return count_cmp;
			}
		} else {
			return var_cmp;
		}
	}
	
	public int LocalIDCompareTo(TokenInCare o) {
		int local_cmp = ((Integer)local_token_id).compareTo(o.local_token_id);
		return local_cmp;
	}
	
}

