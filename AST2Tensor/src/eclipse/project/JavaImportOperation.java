package eclipse.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

public class JavaImportOperation {
	
	// Set<String> 
	public static void ImportFileSystem(IJavaProject javaProject, Map<String, TreeMap<String, String>> dir_files_map) throws JavaModelException {
		// Set<String> analysis_classes = new HashSet<String>();
		
		IOverwriteQuery overwriteQuery = new IOverwriteQuery() {
			public String queryOverwrite(String file) {
				return ALL;
			}
		};
		
		IPath src = JavaProjectElementFinder.FindSourceFolder(javaProject);
		
		int index = 0;
		ArrayList<File> filesToImport = new ArrayList<File>();
		Set<String> dirs = dir_files_map.keySet();
		Iterator<String> diritr = dirs.iterator();
		while (diritr.hasNext())
		{
			index++;
			String dir = diritr.next();
			TreeMap<String, String> files = dir_files_map.get(dir);
			Set<String> fkeys = files.keySet();
			Iterator<String> fitr = fkeys.iterator();
			while (fitr.hasNext())
			{
				String fkey = fitr.next();
				String fvalue = files.get(fkey);
				// analysis_classes.add(fkey);
				filesToImport.add(new File(fvalue));
			}

			System.out.println("==== Import " + index + "/" + dir_files_map.size() + "#" + dir + ": " + files.size() + " files ====");
			WaitOverMonitor wom = new WaitOverMonitor();
			ImportOperation importOperation = new ImportOperation(src, new File(dir),
					FileSystemStructureProvider.INSTANCE, overwriteQuery, filesToImport);
			importOperation.setCreateContainerStructure(false);
			try {
				importOperation.run(wom);
			} catch (Exception e) {
				e.printStackTrace();
			}
			wom.WaitToStop();
//			System.out.println("==== End import " + dir_files_map.size() + " files ====");
		}
		// return analysis_classes;
	}
	
}
