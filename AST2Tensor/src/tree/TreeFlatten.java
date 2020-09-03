package tree;

import java.util.ArrayList;

public class TreeFlatten {
	
	public ArrayList<String> skt_one_struct = new ArrayList<String>();// only one element // into example tensor
	// the followings correspond to a one_struct. 
	public ArrayList<String> skt_one_e_struct = new ArrayList<String>();// into meta info tensor
	public ArrayList<String> skt_one_e_struct_tree_uid = new ArrayList<String>();// into meta info tensor
	public ArrayList<Integer> skt_one_struct_h_count = new ArrayList<Integer>();// into meta info tensor
	public ArrayList<ArrayList<String>> skt_one_struct_h_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
	public ArrayList<Integer> skt_one_struct_v_count = new ArrayList<Integer>();// only one element // into meta info tensor
	public ArrayList<ArrayList<String>> skt_one_struct_v_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
	
	public ArrayList<String> skt_pe_struct = new ArrayList<String>();// into meta info tensor
	// the followings correspond to a pe_struct. 
	public ArrayList<ArrayList<String>> skt_pe_e_struct = new ArrayList<ArrayList<String>>();// into meta info tensor
	public ArrayList<ArrayList<String>> skt_pe_e_struct_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
	public ArrayList<Integer> skt_pe_struct_h_count = new ArrayList<Integer>();// into meta info tensor
	public ArrayList<ArrayList<String>> skt_pe_struct_h_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
	public ArrayList<Integer> skt_pe_struct_v_count = new ArrayList<Integer>();// into meta info tensor
	public ArrayList<ArrayList<String>> skt_pe_struct_v_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor

	public ArrayList<String> skt_token = new ArrayList<String>();// into example tensor
	public ArrayList<Integer> skt_token_is_var = new ArrayList<Integer>();
	public ArrayList<Integer> skt_token_kind = new ArrayList<Integer>();
//	public ArrayList<String> skt_token_tree_uid = new ArrayList<String>();
	
	public ArrayList<String> skt_e_struct = new ArrayList<String>();// into example tensor
	// the followings correspond to a e_struct. 
	public ArrayList<Integer> skt_e_struct_h_count = new ArrayList<Integer>();// into meta info tensor
	public ArrayList<ArrayList<String>> skt_e_struct_h_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
	public ArrayList<Integer> skt_e_struct_v_count = new ArrayList<Integer>();// into meta info tensor
	public ArrayList<ArrayList<String>> skt_e_struct_v_tree_uid = new ArrayList<ArrayList<String>>();// into meta info tensor
	// the following one is not for prediction but only for accuracy computation (#whole sequence information#).
	public ArrayList<String> skt_e_struct_tree_uid = new ArrayList<String>();// into example tensor
	
	public ArrayList<String> skt_e_struct_token = new ArrayList<String>();// into example tensor
	// the following one is not for prediction but only for accuracy computation (#whole sequence information#).
	public ArrayList<String> skt_e_struct_token_tree_uid = new ArrayList<String>();// into example tensor
	
}
