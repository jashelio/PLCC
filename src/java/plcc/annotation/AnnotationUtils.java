package plcc.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import plcc.Resources;

public class AnnotationUtils {
	public static boolean isPlccPackage(Package p) {
		return p.getAnnotation(PlccPackage.class) != null;
	}
	
	public static boolean isPlccAnnotation(Annotation a) {
		return a.annotationType().getAnnotation(PlccClass.class) != null;
	}

	@SuppressWarnings("unchecked")
	public static boolean isGrammarRule(Class c) {
		return c.getAnnotation(GrammarRule.class) != null;
	}		

	public static boolean isToken(Parameter p) {
		return p.getAnnotation(Token.class) != null;
	}

	public static plcc.Token getToken(Parameter p) {
		return Resources.instance.getToken(p.getName());
	}
}
