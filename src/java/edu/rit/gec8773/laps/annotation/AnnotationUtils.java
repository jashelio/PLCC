package edu.rit.gec8773.laps.annotation;

import edu.rit.gec8773.laps.Consumer;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

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

	public static boolean isToken(AnnotatedElement element) {
		return element.getAnnotation(Token.class) != null;
	}

	public static boolean isSkip(AnnotatedElement element) {
		Token token = element.getAnnotation(Token.class);
		return token != null && token.skip();
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
					} catch (IllegalAccessException ignored) {}
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
						method.invoke(object);
					} catch (IllegalAccessException ignored) {}
				};
		}
		return object -> {};
	}
}
