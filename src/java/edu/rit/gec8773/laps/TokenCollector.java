package edu.rit.gec8773.laps;

import edu.rit.gec8773.laps.annotation.AnnotationUtils;
import edu.rit.gec8773.laps.resources.Resources;

import java.lang.reflect.*;
import java.util.function.Function;

/**
 * Collects any and all valid {@link Token}s into {@link Resources} when
 * annotated with {@link edu.rit.gec8773.laps.annotation.Token}
 */
public class TokenCollector {

    /**
     * Collects all the {@link Token}s annotated with the
     * {@link edu.rit.gec8773.laps.annotation.Token} annotation within a
     * given {@link Class}
     * @param tokenContainer the {@link Class} possibly containing {@link Token}s
     * @throws InvocationTargetException when there is an uncaught exception in
     * in user code
     */
    public static void collect(Class<?> tokenContainer) throws InvocationTargetException {
        collect(tokenContainer.getFields(),
                Field::getModifiers,
                Field::getName,
                field -> field.get(null).toString());
        collect(tokenContainer.getMethods(),
                Method::getModifiers,
                Method::getName,
                method -> method.invoke(null).toString());
    }

    private static int pubStat = Modifier.PUBLIC | Modifier.STATIC;

    /**
     * Necessary to have a lambda expression throw exceptions
     * @see Function
     */
    private interface MyFunction<T,R> {
        R apply(T t) throws InvocationTargetException, IllegalAccessException;
    }

    /**
     * Collects name-value pairs from an array of potential {@link Token}s.
     * The value must be accessible so the modifiers must include {@code public}
     * and {@code static}.
     * @param potentialTokens the array of potential {@link Token}s
     * @param getModifiers a function to get the modifiers of a {@code T} object
     * @param getName a function to get the name of a {@code T} object
     * @param getValue a function to get the value of a {@code T} object
     * @param <T> an {@link AnnotatedElement}, such as a {@link Field} or a
     * {@link Method}
     * @throws InvocationTargetException when there is an uncaught exception in
     * in user code
     */
    private static <T extends AnnotatedElement> void collect(T[] potentialTokens,
                                  Function<T, Integer> getModifiers,
                                  Function<T, String> getName,
                                  MyFunction<T, String> getValue)
            throws InvocationTargetException {
        for (T potentialToken : potentialTokens)
            if (AnnotationUtils.isToken(potentialToken))
                if ((getModifiers.apply(potentialToken) & pubStat) == pubStat)
                    try {
                        String name = getName.apply(potentialToken);
                        String value = getValue.apply(potentialToken);
                        if (AnnotationUtils.isSkip(potentialToken)) {
                            Resources.instance.addSkip(value);
                        } else {
                            Resources.instance.addToken(new Token(name, value));
                        }
                    } catch (IllegalAccessException ignored) {}
    }
}
