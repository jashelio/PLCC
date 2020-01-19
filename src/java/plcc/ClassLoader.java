package plcc;

import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.annotation.*;
import java.lang.reflect.*;

import plcc.*;

public class ClassLoader {
	public static final ClassLoader instance = new ClassLoader();

	private List<String> classPath = new ArrayList<>();
	private List<Class<?>> plccClasses = new ArrayList<>();

	private ClassLoader() {
		findClassPath();
//	DEBUG	for (String path : classPath)
//	DEBUG		System.out.println(path);
		for (Package p : Package.getPackages())
			if (p.getAnnotation(PlccPackage.class) != null)
				addPlccClasses(p.getName());
	}

	private void findClassPath() { 
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

	private void addPlccClasses(String pkgName) { 
		System.out.println(pkgName);
		File path;
		int i;
		for (path = new File("klsadjflkjasdkjflk"), i = 0; 
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
				addPlccClasses(fullname);
			else if (filename.endsWith(CLASS_FILE_SUFFIX))
				try {
					Class<?> cls = Class.forName(fullname.substring(0, fullname.lastIndexOf(".")));
					ANNOTATION_LOOP: for (Annotation a : cls.getDeclaredAnnotations())
						if (a.annotationType().getAnnotation(PlccClass.class) != null) {
							plccClasses.add(cls);
							break ANNOTATION_LOOP;
						}
				} catch (ClassNotFoundException e) {}
		}
	}

	public List<Class<?>> getPlccClasses() { 
		return List.copyOf(plccClasses);
	}
}
