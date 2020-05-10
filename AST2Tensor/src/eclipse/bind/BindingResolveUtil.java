package eclipse.bind;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

public class BindingResolveUtil {
	
	public static IBinding ResolveVariableBinding(ASTNode node) {
		IVariableBinding v_bind = null;
		if (node instanceof SimpleName) {
			SimpleName sn = (SimpleName) node;
			IBinding bind = sn.resolveBinding();
			if (bind instanceof IVariableBinding) {
				v_bind = (IVariableBinding) bind;
//				System.out.println("resolved binding simple name:" + sn.toString() + "#bind info:" + v_bind.getClass());
			}
		}
		return v_bind;
	}
	
}
