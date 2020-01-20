package plcc;

import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Package;

import plcc.annotation.AnnotationUtils;

public class ClassLoader {
	private static final List<String> classPath = new ArrayList<>();

	static {
		findClassPath();
	}

	private List<Class<?>> plccClasses = new ArrayList<>();

	public ClassLoader() {
//		findClassPath();
//	DEBUG	for (String path : classPath)
//	DEBUG		System.out.println(path);
		for (Package p : Package.getPackages())
			if (AnnotationUtils.isPlccPackage(p))
				loadPackageClasses(p.getName());
	}

	private static void findClassPath() { 
		String cp = System.getProperties().get("java.class.path").toString();
		String[] classpath = cp.split(":");
		for (String path : classpath) {
			File file = new File(path);
			if (file.exists() && file.isDirectory())
				try {
					classPath.add(file.getAbsoluteFile().getCanonicalPath());
				} catch (IOException e) {}
		}
	}

	private static final char PKG_SEPARATOR = '.';

	private static final char DIR_SEPARATOR = '/';

	private static final String CLASS_FILE_SUFFIX = ".class";

	public void loadPackageClasses(String pkgName) { 
//	DEBUG	System.out.println(pkgName);
		File path;
//	TODO	int i;
		for (path = new File("klsadjflkjasdkjflk"), int i = 0; 
			!path.exists() && i < classPath.size(); 
			path = new File(classPath.get(i++) + DIR_SEPARATOR + pkgName.replace(PKG_SEPARATOR, DIR_SEPARATOR)));
//	DEBUG	System.out.println(path);
		if (!path.exists())
			return;
//	DEBUG	System.out.println(path.listFiles());
//	TODO	for (String fullname : path.list()) { 
		for (File file : path.listFiles()) { 
			String filename = file.getName();
			String fullname = pkgName + PKG_SEPARATOR + filename;
			if (file.isDirectory()) 
				loadPackageClasses(fullname);
			else if (filename.endsWith(CLASS_FILE_SUFFIX))
				try {
					Class<?> cls = Class.forName(fullname.substring(0, fullname.lastIndexOf(".")));
					ANNOTATION_LOOP: for (Annotation a : cls.getDeclaredAnnotations())
						if (AnnotationUtils.isPlccAnnotation(a)) {
							plccClasses.add(cls);
							break ANNOTATION_LOOP; // only needs 1 Plcc annotation to be saved
						}
				} catch (ClassNotFoundException e) {}
		}
	}

	public List<Class<?>> getPlccClasses() { 
		return List.copyOf(plccClasses);
	}
}
