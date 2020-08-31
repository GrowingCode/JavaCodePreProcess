package tree;

import java.util.ArrayList;

public class TreeFlatten {
	
	public ArrayList<String> skt_one_struct = new ArrayList<String>();
	public ArrayList<String> skt_one_e_struct = new ArrayList<String>();
	public ArrayList<String> skt_one_e_struct_tree_uid = new ArrayList<String>();
	public ArrayList<Integer> skt_one_struct_v_count = new ArrayList<Integer>();
	public ArrayList<ArrayList<String>> skt_one_struct_v_tree_uid = new ArrayList<ArrayList<String>>();
	
	public ArrayList<String> skt_pe_struct = new ArrayList<String>();
	public ArrayList<ArrayList<String>> skt_pe_e_struct = new ArrayList<ArrayList<String>>();
	public ArrayList<ArrayList<String>> skt_pe_e_struct_tree_uid = new ArrayList<ArrayList<String>>();
	public ArrayList<Integer> skt_pe_struct_h_count = new ArrayList<Integer>();
	public ArrayList<ArrayList<String>> skt_pe_struct_h_tree_uid = new ArrayList<ArrayList<String>>();
	public ArrayList<Integer> skt_pe_struct_v_count = new ArrayList<Integer>();
	public ArrayList<ArrayList<String>> skt_pe_struct_v_tree_uid = new ArrayList<ArrayList<String>>();
	
	public ArrayList<String> skt_token = new ArrayList<String>();
	public ArrayList<Integer> skt_token_is_var = new ArrayList<Integer>();
	public ArrayList<Integer> skt_token_kind = new ArrayList<Integer>();
//	public ArrayList<String> skt_token_tree_uid = new ArrayList<String>();
	 
	public ArrayList<String> skt_e_struct_token = new ArrayList<String>();
	// the following two are not for prediction but only for accuracy computation.
	public ArrayList<String> skt_e_struct_token_tree_uid = new ArrayList<String>();
		
}
