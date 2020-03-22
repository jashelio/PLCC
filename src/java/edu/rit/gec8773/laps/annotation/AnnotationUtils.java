package edu.rit.gec8773.laps.annotation;

import edu.rit.gec8773.laps.Consumer;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtils {

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

	public static Consumer<Void> getStaticMethod(Class<?> cls,
									 Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<>();
		for (Method method : cls.getMethods()) {
			if (method.getAnnotation(annotation) == null)
				continue;
			int modifiers = method.getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
				methods.add(method);
		}
		return ignore -> {
			for (Method method : methods) {
				try {
					method.invoke(null);
				} catch (IllegalAccessException ignored) {}
			}
		};
	}

	public static Consumer<Object> getInstanceMethod(Class<?> cls,
									 Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<>();
		for (Method method : cls.getMethods()) {
			if (method.getAnnotation(annotation) == null)
				continue;
			int modifiers = method.getModifiers();
			if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
				methods.add(method);
		}
		return object -> {
			for (Method method : methods) {
				try {
					method.invoke(object);
				} catch (IllegalAccessException ignored) {}
			}
		};
	}
}
