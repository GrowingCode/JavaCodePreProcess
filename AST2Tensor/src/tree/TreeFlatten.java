package tree;

import java.util.ArrayList;

public class TreeFlatten {
	
	public ArrayList<String> skt_one_struct_hint = new ArrayList<String>();
	public ArrayList<String> skt_one_struct = new ArrayList<String>();// only one element // into example tensor
	public ArrayList<Integer> skt_one_struct_count = new ArrayList<Integer>();// into example tensor
	public ArrayList<String> skt_one_token_hint = new ArrayList<String>();
	public ArrayList<String> skt_one_token = new ArrayList<String>();// into example tensor
	public ArrayList<Integer> skt_one_token_count = new ArrayList<Integer>();// into example tensor
	public ArrayList<Integer> skt_one_token_kind = new ArrayList<Integer>();
	public ArrayList<Integer> skt_one_token_is_var = new ArrayList<Integer>();
	
	public ArrayList<ArrayList<String>> skt_one_e_struct = new ArrayList<ArrayList<String>>();// into meta info tensor
//	public ArrayList<String> skt_one_e_struct_tree_uid = new ArrayList<String>();// into meta info tensor
//	public ArrayList<Integer> skt_one_struct_h_count = new ArrayList<Integer>();// into meta info tensor
//	public ArrayList<ArrayList<String>> skt_one_struct_h_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
//	public ArrayList<Integer> skt_one_struct_v_count = new ArrayList<Integer>();// only one element // into meta info tensor
//	public ArrayList<ArrayList<String>> skt_one_struct_v_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
	
	public ArrayList<String> skt_pe_struct_hint = new ArrayList<String>();
	public ArrayList<String> skt_pe_struct = new ArrayList<String>();// into meta info tensor
	public ArrayList<Integer> skt_pe_struct_count = new ArrayList<Integer>();// into example tensor
	public ArrayList<String> skt_pe_token_hint = new ArrayList<String>();
	public ArrayList<String> skt_pe_token = new ArrayList<String>();// into example tensor
	public ArrayList<Integer> skt_pe_token_count = new ArrayList<Integer>();// into example tensor
	public ArrayList<Integer> skt_pe_token_kind = new ArrayList<Integer>();
	public ArrayList<Integer> skt_pe_token_is_var = new ArrayList<Integer>();
	
	public ArrayList<ArrayList<String>> skt_pe_e_struct = new ArrayList<ArrayList<String>>();// into meta info tensor
//	public ArrayList<ArrayList<String>> skt_pe_e_struct_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
//	public ArrayList<Integer> skt_pe_struct_h_count = new ArrayList<Integer>();// into meta info tensor
//	public ArrayList<ArrayList<String>> skt_pe_struct_h_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
//	public ArrayList<Integer> skt_pe_struct_v_count = new ArrayList<Integer>();// into meta info tensor
//	public ArrayList<ArrayList<String>> skt_pe_struct_v_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
	
	public ArrayList<String> skt_e_struct_hint = new ArrayList<String>();
	public ArrayList<String> skt_e_struct = new ArrayList<String>();// into meta info tenso
	public ArrayList<Integer> skt_e_struct_count = new ArrayList<Integer>();// into example tensorr
	public ArrayList<String> skt_e_token_hint = new ArrayList<String>();
	public ArrayList<String> skt_e_token = new ArrayList<String>();// into example tensor
	public ArrayList<Integer> skt_e_token_count = new ArrayList<Integer>();// into example tensor
	public ArrayList<Integer> skt_e_token_kind = new ArrayList<Integer>();
	public ArrayList<Integer> skt_e_token_is_var = new ArrayList<Integer>();
	
	public ArrayList<ArrayList<String>> skt_e_e_struct = new ArrayList<ArrayList<String>>();// into meta info tensor
	
//	public Map<String, Integer> token_kind = new TreeMap<String, Integer>();
//	public Map<String, Integer> token_is_var = new TreeMap<String, Integer>();
//	public ArrayList<Integer> skt_token_is_var = new ArrayList<Integer>();
//	public ArrayList<Integer> skt_token_kind = new ArrayList<Integer>();
//	public ArrayList<String> skt_token_tree_uid = new ArrayList<String>();
	
//	public ArrayList<String> skt_e_struct = new ArrayList<String>();// into example tensor
	// the followings correspond to a e_struct. 
//	public ArrayList<Integer> skt_e_struct_h_count = new ArrayList<Integer>();// into meta info tensor
//	public ArrayList<ArrayList<String>> skt_e_struct_h_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
//	public ArrayList<Integer> skt_e_struct_v_count = new ArrayList<Integer>();// into meta info tensor
//	public ArrayList<ArrayList<String>> skt_e_struct_v_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
//	// the following one is not for prediction but only for accuracy computation (#whole sequence information#).
//	public ArrayList<String> skt_e_struct_tree_uid = new ArrayList<String>();// into example tensor
//	
//	public ArrayList<String> skt_e_struct_token = new ArrayList<String>();// into example tensor
//	// the following one is not for prediction but only for accuracy computation (#whole sequence information#).
//	public ArrayList<String> skt_e_struct_token_tree_uid = new ArrayList<String>();// into example tensor
	
}
