package dataset;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;

public class ASTScoreGenerator extends ASTVisitor {

	MethodDeclaration wrapper = null;
	Block wrapper_block = null;
	boolean begin_count = false;
	int nodes_in_wrapper = 0;

//	double total_nodes = 0;
	double total_nodes_in_wrapper = 0;
//	ArrayList<Integer> nodes_for_every_method = new ArrayList<Integer>();
	int number_of_method = 0;

	double total_length = 0;
	double literal_length = 0;

	public double GetScore() {
		double average_method_node_count = total_nodes_in_wrapper == 0 ? 0.0
				: total_nodes_in_wrapper / (number_of_method * 1.0);
		double non_literal_rate = 1.0 - (literal_length / total_length);
		return average_method_node_count * non_literal_rate;
	}

	@Override
	public boolean visit(StringLiteral node) {
		if (begin_count) {
			literal_length += node.getLength();
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(NumberLiteral node) {
		if (begin_count) {
			literal_length += node.getLength();
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		if (begin_count) {
			literal_length += node.getLength();
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (wrapper == null) {
			wrapper = node;
			wrapper_block = node.getBody();
			nodes_in_wrapper = 0;
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(Block node) {
		if (wrapper_block != null && node.equals(wrapper_block)) {
			begin_count = true;
			total_length += node.getLength();
		}
		return super.visit(node);
	}

	@Override
	public void endVisit(Block node) {
		if (wrapper_block != null && node.equals(wrapper_block)) {
			begin_count = false;
		}
		super.endVisit(node);
	}

	@Override
	public void postVisit(ASTNode node) {
//		total_nodes++;
		if (node instanceof MethodDeclaration) {
			MethodDeclaration md = (MethodDeclaration) node;
			if (wrapper.equals(md)) {
				number_of_method++;
				if (nodes_in_wrapper > 1) {
					total_nodes_in_wrapper += (nodes_in_wrapper - 1);
//					nodes_for_every_method.add(nodes_in_wrapper - 1);
				}
				wrapper = null;
				wrapper_block = null;
				nodes_in_wrapper = 0;
			}
		}
		if (begin_count) {
			nodes_in_wrapper++;
		}
		super.postVisit(node);
	}

}
