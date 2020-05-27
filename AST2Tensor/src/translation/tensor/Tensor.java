package translation.tensor;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.Assert;

public abstract class Tensor {

//	IDManager im = null;
//	String origin_file = null;
	int role = -1;
	
	TensorInfo ti = null;

	// processed
	int inner_index = -1;
	Map<Integer, Integer> inner_index_map = new TreeMap<Integer, Integer>();

//	String origin_file, 
	public Tensor(TensorInfo ti) {// IDManager im, int role
//		this.im = im;
//		this.origin_file = origin_file;
//		this.role = role;
		Assert.isTrue(ti != null);
		this.ti = ti;
	}

//	public String GetOriginFile() {
//		return origin_file;
//	}

	public void SetRole(int role) {
		this.role = role;
	}

	public int GetRole() {
		return role;
	}
	
	public TensorInfo GetTensorInfo() {
		return ti;
	}

	public abstract int getSize();

	public abstract String toString();

	public abstract String toDebugString();

	public abstract String toOracleString();

	public Integer GenerateInnerIndexForTypeContent(int type_content_index) {
		Integer inner = inner_index_map.get(type_content_index);
		if (inner == null) {
			inner_index++;
			inner = inner_index;
			inner_index_map.put(type_content_index, inner);
		}
		return inner;
	}

//	public ArrayList<Integer> GenerateInnerIndexesForTypeContents() {
//		ArrayList<Integer> inner_id_type_content_id = new ArrayList<Integer>();
//		List<Entry<Integer, Integer>> inner_index_array = MapUtil.SortMapByValue(inner_index_map);
//		int index = -1;
//		Iterator<Entry<Integer, Integer>> ii_itr = inner_index_array.iterator();
//		while (ii_itr.hasNext()) {
//			index++;
//			Entry<Integer, Integer> e = ii_itr.next();
//			Assert.isTrue(e.getValue() == index);
//			Assert.isTrue(index == inner_id_type_content_id.size());
//			inner_id_type_content_id.add(e.getKey());
//		}
//		return inner_id_type_content_id;
//	}

}
