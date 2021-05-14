package eclipse.bind;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.SimpleName;

import main.MetaOfApp;

public class BindingResolveUtil {
	
	public static IBinding ResolveBinding(ASTNode node) {
		IBinding bind = null;
		if (node instanceof SimpleName) {
			SimpleName sn = (SimpleName) node;
			bind = sn.resolveBinding();
			if (bind != null) {
				Assert.isTrue(bind instanceof IVariableBinding || bind instanceof ITypeBinding || bind instanceof IMethodBinding || bind instanceof IPackageBinding, "wrong binding type:" + bind.getClass() + "#is:" + bind);
				if (MetaOfApp.PrintBindingInfo) {
					System.out.println("Binding discovered:" + bind.getClass().getName() + "#Binding:" + bind);
				}
			}
		} else if (node instanceof MethodInvocation) {
			MethodInvocation mi = (MethodInvocation) node;
			bind = mi.resolveMethodBinding();
			if (bind != null) {
				Assert.isTrue(bind instanceof IMethodBinding);
				if (MetaOfApp.PrintBindingInfo) {
					System.out.println("MethodInvocation Binding discovered:" + bind);
				}
			}
		} else if (node instanceof MethodReference) {
			MethodReference mr = (MethodReference) node;
			bind = mr.resolveMethodBinding();
			if (bind != null) {
				Assert.isTrue(bind instanceof IMethodBinding);
				if (MetaOfApp.PrintBindingInfo) {
					System.out.println("MethodReference Binding discovered:" + bind);
				}
			}
		}
		return bind;
	}
	
//	private static IBinding ResolveVariableBinding(ASTNode node) {
//		IVariableBinding v_bind = null;
//		if (node instanceof SimpleName) {
//			SimpleName sn = (SimpleName) node;
//			IBinding bind = sn.resolveBinding();
//			if (bind instanceof IVariableBinding) {
//				v_bind = (IVariableBinding) bind;
////				System.out.println("resolved binding simple name:" + sn.toString() + "#bind info:" + v_bind.getClass());
//			}
//		}
//		return v_bind;
//	}
//	
//	private static IBinding ResolveMethodBinding(ASTNode node) {
//		IMethodBinding v_bind = null;
//		if (node instanceof MethodInvocation) {
//			MethodInvocation mi = (MethodInvocation) node;
//			IBinding bind = mi.resolveMethodBinding();
//			if (bind instanceof IMethodBinding) {
//				v_bind = (IMethodBinding) bind;
//			}
//		}
//		return v_bind;
//	}
//	
//	private static IBinding ResolveMethodReferenceBinding(ASTNode node) {
//		IMethodBinding v_bind = null;
//		if (node instanceof MethodReference) {
//			MethodReference sn = (MethodReference) node;
//			IBinding bind = sn.resolveMethodBinding();
//			if (bind instanceof IMethodBinding) {
//				v_bind = (IMethodBinding) bind;
//			}
//		}
//		return v_bind;
//	}
	
}
