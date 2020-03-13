package plcc.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
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

	public static boolean isGrammarRule(Class<?> c) {
		return c.getAnnotation(GrammarRule.class) != null;
	}		

	public static boolean isToken(Parameter p) {
		return p.getAnnotation(Token.class) != null;
	}

	public static plcc.Token getToken(Parameter p) {
		return Resources.instance.getToken(p.getName());
	}

	public static Consumer<Void> getStaticMethod(Class<?> cls, // TODO add check for multiple methods
			Class<? extends Annotation> annotation) {
		for (Method method : cls.getMethods()) {
			if (method.getAnnotation(annotation) == null)
				continue;
			int modifiers = method.getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
				return ignore -> {
					try {
						method.invoke(null);
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				};
		}
		return ignore -> {};
	}

	public static Consumer<Object> getInstanceMethod(Class<?> cls, // TODO add check for multiple methods
			Class<? extends Annotation> annotation) {
		for (Method method : cls.getMethods()) {
			if (method.getAnnotation(annotation) == null)
				continue;
			int modifiers = method.getModifiers();
			if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
				return object -> {
					try {
//						System.out.println("Calling semantic entry point: " +
//								method.getDeclaringClass().getName() + "." +
//								method.getName() + "()");
						method.invoke(object);
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				};
		}
		return object -> {};
	}
}
