package edu.rit.gec8773.laps;

import edu.rit.gec8773.laps.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class TokenCollector {
    private final Class<?> tokenContainer;

    public TokenCollector(Class<?> tokenContainer) {
        this.tokenContainer = tokenContainer;
    }
    
    public void collect() throws InvocationTargetException {
        collectFields();
        collectMethods();
    }

    private static int pubStat = Modifier.PUBLIC | Modifier.STATIC;

    private void collectFields() {
        Field[] fields = tokenContainer.getFields();
        for (Field field : fields) {
            if (AnnotationUtils.isToken(field)) {
                if ((field.getModifiers() & pubStat) == pubStat) {
                    String name = field.getName();
                    try {
                        String value = field.get(null).toString();
                        if (AnnotationUtils.isSkip(field)) {
                            Resources.instance.addSkip(value);
                        } else {
                            Resources.instance.addToken(new Token(name, value));
                        }
                    } catch (IllegalAccessException ignored) {}
                }
            }
        }
    }

    private void collectMethods() throws InvocationTargetException {
        Method[] methods = tokenContainer.getMethods();
        for (Method method : methods) {
            if (AnnotationUtils.isToken(method)) {
                if ((method.getModifiers() & pubStat) == pubStat) {
                    String name = method.getName();
                    try {
                        String value = method.invoke(null).toString();
                        if (AnnotationUtils.isSkip(method)) {
                            Resources.instance.addSkip(value);
                        } else {
                            Resources.instance.addToken(new Token(name, value));
                        }
                    } catch (IllegalAccessException ignored) {}
                }
            }
        }
    }
}
