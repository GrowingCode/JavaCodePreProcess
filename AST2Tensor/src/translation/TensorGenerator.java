package translation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;

import translation.tensor.Tensor;

public class TensorGenerator extends ASTVisitor {
	
	LinkedList<Tensor> tensor_list = new LinkedList<Tensor>();
	
	public List<Tensor> GetGeneratedTensors() {
		return tensor_list;
	}
	
}
