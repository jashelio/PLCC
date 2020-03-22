package edu.rit.gec8773.laps.annotation;

import edu.rit.gec8773.laps.MyConsumer;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link AnnotationUtils} provides utility functions to aid in finding
 * {@link AnnotatedElement}s and converting those elements into more
 * useful types
 */
public class AnnotationUtils {

	/**
	 * Checks if a given {@link Class} is, in fact, a grammar rule
	 *
	 * @param c the {@link Class} to check
	 * @return true if the {@link Class} is a grammar rule
	 */
	public static boolean isGrammarRule(Class<?> c) {
		return c.getAnnotation(GrammarRule.class) != null;
	}

	/**
	 * Checks if a given {@link AnnotatedElement} is a
	 * {@link edu.rit.gec8773.laps.Token}
	 *
	 * @param element the {@link AnnotatedElement} to check
	 * @return true if the {@link AnnotatedElement} is a token
	 */
	public static boolean isToken(AnnotatedElement element) {
		return element.getAnnotation(Token.class) != null;
	}

	/**
	 * Checks if a given {@link AnnotatedElement} is a skip
	 * {@link edu.rit.gec8773.laps.Token}
	 *
	 * @param element the {@link AnnotatedElement} to check
	 * @return true if the {@link AnnotatedElement} is a skip token
	 */
	public static boolean isSkip(AnnotatedElement element) {
		Token token = element.getAnnotation(Token.class);
		return token != null && token.skip();
	}

	/**
	 * Retrieves all the visible static {@link Method}s, from a given
	 * {@link Class}, which are annotated with a given {@link Annotation}.
	 * Finally, stores them to be run at a later time
	 *
	 * @param cls the method container {@link Class}
	 * @param annotation the {@link Annotation} to look for
	 * @return a method which invokes all the found methods
	 */
	public static MyConsumer<Void> getStaticMethod(Class<?> cls,
												   Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<>();
		for (Method method : cls.getDeclaredMethods()) {
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

	/**
	 * Retrieves all the visible instance {@link Method}s, from a given
	 * {@link Class}, which are annotated with a given {@link Annotation}.
	 * Finally, stores them to be run at a later time
	 *
	 * @param cls the method container {@link Class}
	 * @param annotation the {@link Annotation} to look for
	 * @return a method which invokes all the found methods on an object
	 */
	public static MyConsumer<Object> getInstanceMethod(Class<?> cls,
													   Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<>();
		for (Method method : cls.getDeclaredMethods()) {
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
