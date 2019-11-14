package eclipse.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;

import eclipse.exception.NoAnalysisSourceException;
import eclipse.exception.ProjectAlreadyExistsException;
import eclipse.jdt.JDTLexicalParser;
import eclipse.project.process.PreProcessHelper;
import main.MetaOfApp;
import util.DataSetUtil;
import util.FileIterator;

public class AnalysisEnvironment {
	
	protected static IJavaProject default_project = null;

	private static void InitializeClassPathWithDefaultJRE(List<IClasspathEntry> entries) {
		LibraryLocation[] libs = new LibraryLocation[0];
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		if (vmInstall != null) {
			libs = JavaRuntime.getLibraryLocations(vmInstall);
		}
		for (LibraryLocation element : libs) {
			entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
		}
//		PrintUtil.PrintList(entries, "default_jre_entries");
	}

//	public static IJavaProject GetDefaultAnalysisEnironment() {
//		if (default_project == null) {
//			List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
//			InitializeClassPathWithDefaultJRE(entries);
//			IJavaProject java_project = null;
//			try {
//				java_project = JavaProjectManager.UniqueManager()
//						.CreateJavaProject(FakedProjectEnvironmentMeta.FakedProject, entries);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			default_project = java_project;
//		}
//		return default_project;
//	}

	public static IJavaProject CreateAnalysisEnvironment(ProjectInfo pi)
			throws NoAnalysisSourceException, ProjectAlreadyExistsException, CoreException {
		File dir = null;
		dir = new File(pi.getBasedir());
		if (!dir.exists() || !dir.isDirectory()) {
			throw new NoAnalysisSourceException();
		}
//		File dependency_dir = new File(ResourceMeta.GetAbsolutePathOfProjectDependencyDirectory());
//		if (dependency_dir.exists()) {
//			FileUtil.DeleteFile(dependency_dir);
//		}
//		dependency_dir.mkdir();
//		File faked_proj_dir = new File(FakedProjectEnvironmentMeta.GetFakedEnvironment());
//		if (faked_proj_dir.exists()) {
//			FileUtil.DeleteFile(faked_proj_dir);
//		}
//		faked_proj_dir.mkdir();
//		File gradle_dir = new File(dependency_dir + "/gradle_dependencies");
//		if (gradle_dir.exists()) {
//			FileUtil.DeleteFile(gradle_dir);
//		}
//		gradle_dir.mkdirs();
//		File maven_dir = new File(dependency_dir + "/maven_dependencies");
//		if (maven_dir.exists()) {
//			FileUtil.DeleteFile(maven_dir);
//		}
//		maven_dir.mkdirs();

		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		InitializeClassPathWithDefaultJRE(entries);
//		{
//			FileIterator fi = new FileIterator(dir.getAbsolutePath(), ".+\\.gradle$");
//			Iterator<File> fitr = fi.EachFileIterator();
//			int index = 0;
//			while (fitr.hasNext()) {
//				File f = fitr.next();
//				index++;
//				File f_dir = new File(gradle_dir.getAbsolutePath() + "/" + index);
//				f_dir.mkdirs();
//				// File gradle = new File(f_dir.getAbsolutePath() + "/" + "build.gradle");
//				GradleTransformer seeker = new GradleTransformer();
//				seeker.TransformIntoDirectoryAndExecute(f, f_dir);
//			}
//			IterateAllJarsToFillEntries(gradle_dir, entries);
//		}
//		{
//			FileIterator fi = new FileIterator(dir.getAbsolutePath(), "^pom\\.xml$");
//			Iterator<File> fitr = fi.EachFileIterator();
//			int index = 0;
//			while (fitr.hasNext()) {
//				File f = fitr.next();
//				index++;
//				File f_dir = new File(maven_dir.getAbsolutePath() + "/" + index);
//				f_dir.mkdirs();
//				// File pom = new File(f_dir.getAbsolutePath() + "/" + "pom.xml");
//				// FileUtil.CopyFile(f, pom);
//				PomTransformer transformer = new PomTransformer();
//				transformer.TransformIntoDirectoryAndExecute(f, f_dir);
//			}
//			IterateAllJarsToFillEntries(maven_dir, entries);
//		}
		{
			// Iterator<IClasspathEntry> eitr = entries.iterator();
			// while (eitr.hasNext()) {
			// IClasspathEntry ice = eitr.next();
			// System.err.println(ice.toString());
			// }
			IterateAllJarsToFillEntries(dir, entries);
		}
		
//		IClasspathEntry[] ori_entries = java_project.getRawClasspath();
//		for (IClasspathEntry ice : ori_entries) {
//			System.out.println("one_ice:" + ice);
//		}
//		System.exit(1);
//		entries.addAll(Arrays.asList(ori_entries));

//		IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
//		System.arraycopy(entries, 0, newEntries, 0, entries.length);
//
//		IPath srcPath= java_project.getPath().append("target/generated-sources");
//		IClasspathEntry srcEntry= JavaCore.newSourceEntry(srcPath, null);
//
//		newEntries[entries.length] = JavaCore.newSourceEntry(srcEntry.getPath());
//		java_project.setRawClasspath(newEntries, null);
		
		
//		Map<String, TreeMap<String, String>> dir_files_map = new TreeMap<String, TreeMap<String, String>>();
		Map<String, String> file_full_qualified_name_file_path_map = new TreeMap<String, String>();
		{
			// import legal .java files into IJavaProject.
			FileIterator fi = new FileIterator(dir.getAbsolutePath(), ".+(?<!-yyx-copy)\\.java$");
			Iterator<File> fitr = fi.EachFileIterator();
			while (fitr.hasNext()) {
				File f = fitr.next();
				String f_norm_path = f.getAbsolutePath().trim().replace('\\', '/');
//				DebugLogger.Log("f_norm_path:" + f_norm_path);
				CompilationUnit cu = JDTLexicalParser.ParseJavaFile(f);
				PackageDeclaration pack = cu.getPackage();
				String packagename = "";
				if (pack != null) {
					packagename = pack.getName().toString();
				}
				String fname = f.getName();
				String packagepath = packagename.replace('.', '/');
				String packagepath_with_classfile = packagepath + "/" + fname;
				String file_simple_name_without_suffix = fname.substring(0, fname.lastIndexOf(".java"));
				String r_file_simple_name_without_suffix = DataSetUtil.FilterNumberPrefix(file_simple_name_without_suffix);
				String class_full_qualified_name = packagename + "." + r_file_simple_name_without_suffix;
				if (MetaOfApp.JavaFileNoLimit) {
					UpdateFileQualifiedNameWithFilePathMap(file_full_qualified_name_file_path_map, class_full_qualified_name, f_norm_path);
				} else if (f_norm_path.endsWith(packagepath_with_classfile)) {
//					String f_dir = f_norm_path.substring(0, f_norm_path.lastIndexOf(packagepath_with_classfile))
//							.replace('\\', '/');
//					while (f_dir.endsWith("/")) {
//						f_dir = f_dir.substring(0, f_dir.length() - 1);
//					}
//					TreeMap<String, String> files_in_dir = dir_files_map.get(f_dir);
//					if (files_in_dir == null) {
//						files_in_dir = new TreeMap<String, String>();
//						dir_files_map.put(f_dir, files_in_dir);
//					}
//					UpdateFileQualifiedNameWithFilePathMap(files_in_dir, class_full_qualified_name, f_norm_path, f);
					UpdateFileQualifiedNameWithFilePathMap(file_full_qualified_name_file_path_map, class_full_qualified_name, f_norm_path);
				}
			}
			// Fill the source folder of the project.
		}
		
//		IJavaProject java_project = JavaProjectManager.UniqueManager().CreateJavaProject(pi.getName(), entries, dir_files_map);
		IJavaProject java_project = JavaProjectManager.UniqueManager().CreateJavaProject(pi.getName(), entries, file_full_qualified_name_file_path_map);
		
//		JavaImportOperation.ImportFileSystem(java_project, dir_files_map);
//		Set<String> r_dirs = dir_files_map.keySet();
//		for (String r_dir : r_dirs) {
////			IPath srcPath = java_project.getPath().append("target/generated-sources");
//			IPath srcPath = new Path(r_dir);
//			IClasspathEntry srcEntry= JavaCore.newSourceEntry(srcPath, null);
//			entries.add(JavaCore.newSourceEntry(srcEntry.getPath()));
//		}
//		PrintUtil.PrintList(entries, "entries");
//		java_project.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
//		System.err.println("Debugging, import files:" + dir_files_map);

		PreProcessHelper.PreProcessProject(java_project);
		return java_project;
	}

	private static void IterateAllJarsToFillEntries(File dir, List<IClasspathEntry> entries) {
		FileIterator fi = new FileIterator(dir.getAbsolutePath(), ".+\\.jar$");
		Iterator<File> fitr = fi.EachFileIterator();
		while (fitr.hasNext()) {
			File f = fitr.next();
			entries.add(JavaCore.newLibraryEntry(new Path(f.getAbsolutePath()), null, null));
		}
	}
	
	private static void UpdateFileQualifiedNameWithFilePathMap(Map<String, String> file_full_qualified_name_file_path_map, String class_full_qualified_name, String f_norm_path) {
		// How to judge which java file is more complete? Currently, just judge the last
		// update time of a file.
		if (file_full_qualified_name_file_path_map.containsKey(class_full_qualified_name)) {
			String full_name = file_full_qualified_name_file_path_map.get(class_full_qualified_name);
			File f = new File(f_norm_path);
			File full_f = new File(full_name);
			if (f.lastModified() > full_f.lastModified()) {
				file_full_qualified_name_file_path_map.put(class_full_qualified_name, f_norm_path);
			}
		} else {
			file_full_qualified_name_file_path_map.put(class_full_qualified_name, f_norm_path);
		}
	}

	public static void DeleteAnalysisEnvironment(ProjectInfo pi) throws CoreException {
		JavaProjectManager.UniqueManager().DeleteJavaProject(pi.getName());
	}

	public static void DeleteAllAnalysisEnvironment() throws CoreException {
		JavaProjectManager.UniqueManager().DeleteAllJavaProjects();
	}

}
