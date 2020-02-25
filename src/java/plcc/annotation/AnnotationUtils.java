package plcc.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

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

	public static Consumer<Object> getSemanticEntryPoint() {
		Class<?> cls = Resources.instance.getGrammarHead()
						 .getGrammarClass();
		for (Method method : cls.getMethods()) {
			if (method.getAnnotation(SemanticEntryPoint.class) == null)
				continue;
			int modifiers = method.getModifiers();
			if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
				return object -> {
					try {
						method.invoke(object);
					} catch (Exception e) {}
				};
		}
		return object -> System.out.println("Could not find semantic entry point");
	}
}
