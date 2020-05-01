package translation.tensor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import main.MetaOfApp;
import statistic.id.IDManager;
import translation.tensor.util.ConservedMemoryUtil;
import translation.tensor.util.RepetitionUtil;
import translation.tensor.util.TokenIndexUtil;

public class SequenceTensor extends Tensor {

	ArrayList<String> node_type_content_str = new ArrayList<String>();
	ArrayList<String> node_var_str = new ArrayList<String>();

	ArrayList<Integer> token_en = new ArrayList<Integer>();
	ArrayList<Integer> token_var = new ArrayList<Integer>();
	ArrayList<Integer> token_var_relative = new ArrayList<Integer>();
	ArrayList<Integer> conserved_memory_length = new ArrayList<Integer>();

	public void AppendOneToken(String token_str, String var_str, int type_content_en) {
		node_type_content_str.add(token_str);
		if (MetaOfApp.VariableNoLimit) {
			node_var_str.add(token_str);
		} else {
			node_var_str.add(var_str);
		}
		token_en.add(type_content_en);
	}

	public void HandleAllDevoured(IDManager im) {
		token_var.addAll(TokenIndexUtil.GenerateTokenIndex(node_var_str));
		token_var_relative.addAll(RepetitionUtil.GenerateRepetitionRelative(token_var));
		conserved_memory_length.addAll(ConservedMemoryUtil.GenerateConservedMemory(token_var, token_var_relative,
				MetaOfApp.ConservedContextLength));
	}

	@Override
	public int getSize() {
		return token_en.size();
	}

	@Override
	public String toString() {
		return StringUtils.join(token_en.toArray(), " ") + "#" + StringUtils.join(token_var.toArray(), " ") + "#"
				+ StringUtils.join(token_var_relative.toArray(), " ") + StringUtils.join(conserved_memory_length.toArray(), " ");
	}

	@Override
	public String toDebugString() {
		String separator = System.getProperty("line.separator");
		return StringUtils.join(token_en.toArray(), " ") + separator + StringUtils.join(token_var.toArray(), " ")
				+ separator + StringUtils.join(token_var_relative.toArray(), " ") + StringUtils.join(conserved_memory_length.toArray(), " ");
	}

	@Override
	public String toOracleString() {
		String separator = System.getProperty("line.separator");
		return "str:" + StringUtils.join(node_type_content_str.toArray(), " ") + separator
				+ "en:" + StringUtils.join(token_en.toArray(), " ") + separator 
				+ "var:" + StringUtils.join(token_var.toArray(), " ") + separator 
				+ "var_relative:" + StringUtils.join(token_var_relative.toArray(), " ")
				+ "cnsv_mem_len:" + StringUtils.join(conserved_memory_length.toArray(), " ")
				;
	}

}
