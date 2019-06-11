package eclipse.search;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class EclipseSearchForICompilationUnits {

	public static List<ICompilationUnit> SearchForAllICompilationUnits(IJavaProject java_project)
			throws JavaModelException {
//		IClasspathEntry en = java_project.getRawClasspath()[18];
//		IProject someProject = ResourcesPlugin.getWorkspace().getRoot().getProject(en.getPath().toString());
//		IJavaProject someJavaProject = null;
//		try {
//			someJavaProject = (IJavaProject) someProject.getNature(JavaCore.NATURE_ID);
//		} catch (CoreException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//		IPackageFragmentRoot pfr = someJavaProject.getPackageFragmentRoots()[0];
//		String is_k_source = new Boolean(pfr.getKind() == IPackageFragmentRoot.K_SOURCE).toString();
//		System.out.println("is_k_source:" + is_k_source + "#pfr:" + pfr);
//		System.out.println("en:" + en);
//		IPackageFragmentRoot[] r_package_roots = java_project.findPackageFragmentRoots(en);
//		System.out.println("r_package_roots.length:" + r_package_roots.length);
//		for (IPackageFragmentRoot r_package_root : r_package_roots) {
//			System.out.println("r_package_root:" + r_package_root);
//		}
//		System.out.println("r_package_root:" + r_package_root);
//		IJavaElement[] r_fragments = r_package_root.getChildren();
//		System.out.println("src fragments size:" + r_fragments.length);
//		IPackageFragmentRoot[] package_roots = java_project.getPackageFragmentRoots();
		IPackageFragmentRoot[] package_roots = java_project.getAllPackageFragmentRoots();
		List<ICompilationUnit> units = new LinkedList<ICompilationUnit>();
		for (IPackageFragmentRoot package_root : package_roots) {
			if (package_root.getKind() != IPackageFragmentRoot.K_SOURCE) {
				continue;
			}
			IJavaElement[] fragments = package_root.getChildren();
			System.out.println("package_root is open:"+package_root.isOpen() + "#package_root:" + package_root.getElementName() + "#size_of_fragments:" + fragments.length);
			for (int j = 0; j < fragments.length; j++) {
				IPackageFragment fragment = (IPackageFragment) fragments[j];
				IJavaElement[] javaElements = fragment.getChildren();
				System.out.println("fragment:" + fragment + "#fragment.containsJavaResources():" + fragment.containsJavaResources() + "#size_of_javaElements:" + javaElements.length);
				for (int k = 0; k < javaElements.length; k++) {
					IJavaElement javaElement = javaElements[k];
					if (javaElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
						units.add((ICompilationUnit) javaElement);
					}
				}
			}
		}
		return units;
	}

}
