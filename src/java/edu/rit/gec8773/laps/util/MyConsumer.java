package edu.rit.gec8773.laps.util;

import java.lang.reflect.InvocationTargetException;

/**
 * Necessary for lambda expressions to throw exceptions
 *
 * @see java.util.function.Consumer
 */
public interface MyConsumer<T> {
    void accept(T t) throws InvocationTargetException;
}
