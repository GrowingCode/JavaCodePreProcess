package eclipse.project;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import eclipse.exception.ProjectAlreadyExistsException;
import statis.trans.common.RoleAssigner;
import util.FileUtil;

public class JavaProjectManager {

	private static JavaProjectManager unique = new JavaProjectManager();
	protected Map<String, IJavaProject> projects = new TreeMap<String, IJavaProject>();

	private JavaProjectManager() {
	}
	
//	Map<String, TreeMap<String, String>> dir_files_map
	public IJavaProject CreateJavaProject(String projname, List<IClasspathEntry> entries, Map<String, String> file_full_qualified_name_file_path_map) throws ProjectAlreadyExistsException, CoreException {
		IProject project = null;
		IJavaProject java_project = null;
		{
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			project = root.getProject(projname);
			if (project.exists())
			{
				System.err.println("Project " + projname + " already existed! It will be deleted!");
				DeleteJavaProject(projname);
			}
			project.create(null);
			project.open(null);
		}

		{
			IProjectDescription description = project.getDescription();
			description.setNatureIds(new String[] { JavaCore.NATURE_ID });
			project.setDescription(description, null);
		}

		{
			java_project = JavaCore.create(project);

			IFolder binFolder = project.getFolder("bin");
			binFolder.create(false, true, null);
			java_project.setOutputLocation(binFolder.getFullPath(), null);
			java_project.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
			
			IFolder sourceFolder = project.getFolder("src");
			sourceFolder.create(false, true, null);
			IPath source_folder_path = project.getFullPath().append("src");

			TreeMap<String, TreeMap<String, String>> package_classes = new TreeMap<String, TreeMap<String, String>>();
//			Set<String> dirs = dir_files_map.keySet();
//			Iterator<String> dir_itr = dirs.iterator();
//			while (dir_itr.hasNext()) {
//				String dir = dir_itr.next();
//				TreeMap<String, String> files = dir_files_map.get(dir);
//				Set<String> f_keys = files.keySet();
				Set<String> f_keys = file_full_qualified_name_file_path_map.keySet();
				Iterator<String> f_itr = f_keys.iterator();
				while (f_itr.hasNext()) {
					String f_full_qualified = f_itr.next();
//					String file = files.get(f_full_qualified);
					String file = file_full_qualified_name_file_path_map.get(f_full_qualified);
					int f_idx = f_full_qualified.lastIndexOf('.');
					String package_name = f_full_qualified.substring(0, f_idx);
					String class_name = f_full_qualified.substring(f_idx+1);
					TreeMap<String, String> class_files = package_classes.get(package_name);
					if (class_files == null) {
						class_files = new TreeMap<String, String>();
						package_classes.put(package_name, class_files);
					}
					class_files.put(class_name, file);
				}
//			}

			IPackageFragmentRoot root_src = java_project.getPackageFragmentRoot(sourceFolder);
			IClasspathEntry[] oldEntries = java_project.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newSourceEntry(source_folder_path);
			java_project.setRawClasspath(newEntries, null);
			Assert.isTrue(root_src.getKind() == IPackageFragmentRoot.K_SOURCE);
			
			Set<String> pc_pack = package_classes.keySet();
			Iterator<String> pc_itr = pc_pack.iterator();
			while (pc_itr.hasNext()) {
				String package_name = pc_itr.next();
				IPackageFragment pack = root_src.getPackageFragment(package_name);
				if (pack == null || !pack.exists()) {
					pack = root_src.createPackageFragment(package_name, false, null);
				}
				TreeMap<String, String> class_files = package_classes.get(package_name);
				Set<String> class_names = class_files.keySet();
				Iterator<String> cn_itr = class_names.iterator();
				while (cn_itr.hasNext()) {
					String class_name = cn_itr.next();
					if (class_name.contains("-") || class_name.contains("$") || class_name.contains("#")) {
						continue;
					}
					String file = class_files.get(class_name);
					File ff = new File(file);
//					System.out.println("package_name:" + package_name + "#class_name:" + class_name + "#ff.getAbsolutePath():" + ff.getAbsolutePath());
					ICompilationUnit icu = pack.createCompilationUnit(class_name + ".java", FileUtil.ReadFromFile(ff), false, null);
					assert icu != null;
					RoleAssigner.GetInstance().AssignRole(file, icu);
				}
			}
			
//			System.out.println("sourceFolder.getFullPath().makeAbsolute():" + sourceFolder.getLocation());
//			Set<String> dirs = dir_files_map.keySet();
//			for (String dir : dirs) {
//				try {
//					FileUtil.CopyDir(dir, sourceFolder.getLocation().toString());
//				} catch (IOException e) {
//					e.printStackTrace();
//					System.exit(1);
//				}
//			}
//			System.out.println("last src entry:" + java_project.getRawClasspath()[oldEntries.length]);
		}
		// add to set to record.
		projects.put(projname, java_project);

		return java_project;
	}

	public void DeleteJavaProject(String projname) throws CoreException {
		try {
			IProject project = null;
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			project = root.getProject(projname);
			project.delete(true, true, null);
			Thread.sleep(5000);
			ResourcesPlugin.getWorkspace().save(true, null);
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DeleteAllJavaProjects() throws CoreException {
		List<String> keys = new LinkedList<String>(projects.keySet());
		Iterator<String> kitr = keys.iterator();
		while (kitr.hasNext()) {
			String proj_name = kitr.next();
			projects.remove(proj_name);
			DeleteJavaProject(proj_name);
		}
		projects.clear();
	}
	
	public IJavaProject GetJavaProject(String proj_name) {
		return projects.get(proj_name);
	}

	public static JavaProjectManager UniqueManager() {
		return unique;
	}

}
