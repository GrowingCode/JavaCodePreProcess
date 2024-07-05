package eclipse.bind;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.SimpleName;

import main.MetaOfApp;

public class BindingResolveUtil {
	
	private static Set<ITypeBinding> dc_bindings = new HashSet<ITypeBinding>();
	private static Set<IMethodBinding> mi_bindings = new HashSet<IMethodBinding>();
	private static Set<IMethodBinding> md_bindings = new HashSet<IMethodBinding>();
	
	public static IBinding ResolveBinding(ASTNode node) {
		IBinding bind = null;
		if (node instanceof SimpleName) {
			SimpleName sn = (SimpleName) node;
			bind = sn.resolveBinding();
			if (bind != null) {
				Assert.isTrue(bind instanceof IVariableBinding || bind instanceof ITypeBinding || bind instanceof IMethodBinding || bind instanceof IPackageBinding, "wrong binding type:" + bind.getClass() + "#is:" + bind);
				if (MetaOfApp.PrintInfo && MetaOfApp.PrintBindingInfo && MetaOfApp.PrintSimpleNameBindingInfo) {
					System.out.println("Binding discovered:" + bind.getClass().getName() + "#Binding:" + bind.getName() + "$" + bind.getKey());
				}
			}
		} else if (node instanceof MethodInvocation) {
			MethodInvocation mi = (MethodInvocation) node;
			bind = mi.resolveMethodBinding();
			if (bind != null) {
				Assert.isTrue(bind instanceof IMethodBinding);
				if (MetaOfApp.PrintInfo && MetaOfApp.PrintBindingInfo) {
					System.out.println("MethodInvocation Binding discovered:" + bind.getClass().getName() + "#Binding:" + bind.getName() + "$" + bind.getKey());
					IMethodBinding imb = (IMethodBinding) bind;
//					ITypeBinding drt = imb.getDeclaredReceiverType();
//					ITypeBinding dc = imb.getDeclaringClass();
//					IBinding dm = imb.getDeclaringMember();
//					System.out.println("ITypeBinding ReceiverType:" + (drt != null ? drt.getKey() : "null"));
//					System.out.println("ITypeBinding DeclaringClass:" + (dc != null ? dc.getKey() : "null"));
//					System.out.println("IBinding DeclaringMember:" + (dm != null ? dm.getKey() : "null"));
//					dc_bindings.add(dc);
					mi_bindings.add(imb);
				}
			}
		} else if (node instanceof MethodReference) {
			MethodReference mr = (MethodReference) node;
			bind = mr.resolveMethodBinding();
			if (bind != null) {
				Assert.isTrue(bind instanceof IMethodBinding);
//				IMethodBinding imb = (IMethodBinding) bind;
				if (MetaOfApp.PrintInfo && MetaOfApp.PrintBindingInfo) {
					System.out.println("MethodReference Binding discovered:" + bind.getClass().getName() + "#Binding:" + bind.getName() + "$" + bind.getKey());
				}
			}
		} else if (node instanceof MethodDeclaration) {
			MethodDeclaration mr = (MethodDeclaration) node;
			bind = mr.resolveBinding();
			if (bind != null) {
				Assert.isTrue(bind instanceof IMethodBinding);
//				IMethodBinding imb = (IMethodBinding) bind;
				if (MetaOfApp.PrintInfo && MetaOfApp.PrintBindingInfo) {
					System.out.println("MethodDeclaration Binding discovered:" + bind.getClass().getName() + "#Binding:" + bind.getName() + "$" + bind.getKey());
					IMethodBinding imb = (IMethodBinding) bind;
					md_bindings.add(imb);
				}
			}
		}
		return bind;
	}
	
	public static void PrintBindingAssignment() {
		if (MetaOfApp.PrintInfo)
		{
			Iterator<ITypeBinding> dc_itr1 = dc_bindings.iterator();
			while (dc_itr1.hasNext()) {
				ITypeBinding itb1 = dc_itr1.next();
				Iterator<ITypeBinding> dc_itr2 = dc_bindings.iterator();
				while (dc_itr2.hasNext()) {
					ITypeBinding itb2 = dc_itr2.next();
					if (itb1 != itb2) {
						boolean ass_res = itb1.isAssignmentCompatible(itb2);
						System.out.println("ass_comp:" + ass_res + "#itb1:" + itb1.getKey() + "#itb2:" + itb2.getKey());
					}
				}
			}
		}
//		System.out.println("=== split line ===");
		if (MetaOfApp.PrintInfo)
		{
			Iterator<IMethodBinding> md_itr1 = mi_bindings.iterator();
			while (md_itr1.hasNext()) {
				IMethodBinding imb1 = md_itr1.next();
				System.out.println("MethodInvocation md1.getKey():" + imb1.getMethodDeclaration().getKey());
				Iterator<IMethodBinding> md_itr2 = mi_bindings.iterator();
				while (md_itr2.hasNext()) {
					IMethodBinding imb2 = md_itr2.next();
					if (imb1 != imb2) {
//						IMethodBinding md1 = imb1.getMethodDeclaration();
//						IMethodBinding md2 = imb2.getMethodDeclaration();
//						boolean override = imb1.overrides(imb2);
//						boolean override_md = md1.overrides(md2);
//						boolean equal_1_2 = md1.isEqualTo(md2);
//						ITypeBinding dec1 = imb1.getDeclaringClass();
//						ITypeBinding dec2 = imb2.getDeclaringClass();
//						System.out.println("dec1:" + dec1.getKey() + "#dec2:" + dec2.getKey());
//						System.out.println("equal_1_2:" + equal_1_2);
//						System.out.println("MethodInvocation md1.getKey():" + md1.getKey() + "#md2.getKey():" + md2.getKey());
//						System.out.println("override:" + override + "#imb1:" + imb1.getKey() + "#imb2:" + imb2.getKey());
//						System.out.println("override_md:" + override_md + "#md1:" + md1.getKey() + "#md2:" + md2.getKey());
//						System.out.println("=== split line ===");
					}
				}
			}
		}
//		System.out.println("=== split line ===");
		if (MetaOfApp.PrintInfo)
		{
			Iterator<IMethodBinding> md_itr1 = md_bindings.iterator();
			while (md_itr1.hasNext()) {
				IMethodBinding imb1 = md_itr1.next();
				System.out.println("MethodDeclaration md1.getKey():" + imb1.getMethodDeclaration().getKey());
				Iterator<IMethodBinding> md_itr2 = md_bindings.iterator();
				while (md_itr2.hasNext()) {
					IMethodBinding imb2 = md_itr2.next();
					if (imb1 != imb2) {
//						IMethodBinding md1 = imb1.getMethodDeclaration();
//						IMethodBinding md2 = imb2.getMethodDeclaration();
//						boolean override = imb1.overrides(imb2);
//						boolean override_md = md1.overrides(md2);
//						boolean equal_1_2 = md1.isEqualTo(md2);
//						ITypeBinding dec1 = imb1.getDeclaringClass();
//						ITypeBinding dec2 = imb2.getDeclaringClass();
//						System.out.println("dec1:" + dec1.getKey() + "#dec2:" + dec2.getKey());
//						System.out.println("equal_1_2:" + equal_1_2);
//						System.out.println("MethodDeclaration md1.getKey():" + md1.getKey() + "#md2.getKey():" + md2.getKey());
//						System.out.println("override:" + override + "#imb1:" + imb1.getKey() + "#imb2:" + imb2.getKey());
//						System.out.println("override_md:" + override_md + "#md1:" + md1.getKey() + "#md2:" + md2.getKey());
//						System.out.println("=== split line ===");
					}
				}
			}
		}
//		System.out.println("=== split line ===");
		if (MetaOfApp.PrintInfo)
		{
			Iterator<IMethodBinding> mi_itr1 = mi_bindings.iterator();
			while (mi_itr1.hasNext()) {
				IMethodBinding mi1 = mi_itr1.next().getMethodDeclaration();
				Iterator<IMethodBinding> md_itr2 = md_bindings.iterator();
				while (md_itr2.hasNext()) {
					IMethodBinding md2 = md_itr2.next().getMethodDeclaration();
					System.out.println("mi1 equals md2:" + mi1.isEqualTo(md2) + "#mi1.getKey():" + mi1.getMethodDeclaration().getKey() + "#md2.getKey():" + md2.getMethodDeclaration().getKey());
				}
			}
		}
//		System.out.println("=== split line ===");
	}
	
	public static void ClearStorage() {
		dc_bindings.clear();
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
