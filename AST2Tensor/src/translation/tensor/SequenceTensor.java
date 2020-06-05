package translation.tensor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Assert;

import main.MetaOfApp;
import statistic.id.IDManager;
import translation.tensor.util.ConservedMemoryUtil;
import translation.tensor.util.RepetitionUtil;
import translation.tensor.util.TokenIndexUtil;
import translation.tensor.util.TokenKindUtil;
import util.PrintUtil;

public class SequenceTensor extends Tensor {

	ArrayList<String> node_type_content_str = new ArrayList<String>();
	ArrayList<String> node_var_str = new ArrayList<String>();

	ArrayList<Integer> token_en = new ArrayList<Integer>();
	ArrayList<Integer> token_var = new ArrayList<Integer>();
	ArrayList<Integer> token_var_relative = new ArrayList<Integer>();
	ArrayList<Integer> conserved_memory_length = new ArrayList<Integer>();
	ArrayList<Integer> token_kind = new ArrayList<Integer>();
	
	public SequenceTensor(TensorInfo ti) {
		super(ti);
	}

	public void AppendOneToken(String token_str, String var_str, int type_content_en, int en_kind) {
		node_type_content_str.add(token_str);
		if (MetaOfApp.VariableNoLimit) {
			node_var_str.add(token_str);
		} else {
			node_var_str.add(var_str);
		}
		token_en.add(type_content_en);
		token_kind.add(en_kind);
	}

	public void HandleAllDevoured(IDManager im) {
		token_var.addAll(TokenIndexUtil.GenerateTokenIndex(node_var_str));
		if (MetaOfApp.UseApproximateVariable) {
			Assert.isTrue(token_var.size() == token_kind.size());
			ArrayList<String> te_var_str = TokenKindUtil.GenApproximateVarFromTokenKind(node_type_content_str, token_kind);
			ArrayList<Integer> pre_token_var = new ArrayList<Integer>();
			pre_token_var.addAll(token_var);
			token_var.clear();
			token_var.addAll(TokenIndexUtil.GenerateTokenIndex(te_var_str));
			if (MetaOfApp.PrintTokenKindDebugInfo) {
				PrintUtil.PrintThreeLists(node_type_content_str, pre_token_var, token_var, "token_var cmp approx_token_var", 25);
			}
		}
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
				+ StringUtils.join(token_var_relative.toArray(), " ") + "#" + StringUtils.join(conserved_memory_length.toArray(), " ") + "#" 
				+ StringUtils.join(token_kind.toArray(), " ");
	}

	@Override
	public String toDebugString() {
		String separator = System.getProperty("line.separator");
		return StringUtils.join(token_en.toArray(), " ") + separator + StringUtils.join(token_var.toArray(), " ")
				+ separator + StringUtils.join(token_var_relative.toArray(), " ") + separator + StringUtils.join(conserved_memory_length.toArray(), " ")
				+ separator + StringUtils.join(token_kind.toArray(), " ");
	}

	@Override
	public String toOracleString() {
		String separator = System.getProperty("line.separator");
		return "str:" + StringUtils.join(node_type_content_str.toArray(), " ") + separator
				+ "en:" + StringUtils.join(token_en.toArray(), " ") + separator 
				+ "var:" + StringUtils.join(token_var.toArray(), " ") + separator 
				+ "var_relative:" + StringUtils.join(token_var_relative.toArray(), " ") + separator
				+ "cnsv_mem_len:" + StringUtils.join(conserved_memory_length.toArray(), " ") + separator
				+ "token_kind:" + StringUtils.join(token_kind.toArray(), " ") + separator
				;
	}

}
