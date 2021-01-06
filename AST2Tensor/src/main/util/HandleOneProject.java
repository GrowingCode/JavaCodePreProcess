package main.util;

import statis.trans.project.STProject;
import statistic.IDTools;
import translation.TensorTools;

public interface HandleOneProject {
//	String proj_path, int all_size
	public int Handle(STProject proj, IDTools id_tool, TensorTools tensor_tool);
}

