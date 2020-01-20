package plcc.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Package;

public class AnnotationUtils {
	public static boolean isPlccPackage(Package p) {
		return p.getAnnotation(PlccPackage.class) != null;
	}
	
	public static boolean isPlccAnnotation(Annotation a) {
		return a.annotationType().getAnnotation(PlccClass.class) != null;
	}
}
