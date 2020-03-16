package edu.rit.gec8773.laps;

import java.lang.reflect.InvocationTargetException;

public interface Consumer<T> {
    void accept(T t) throws InvocationTargetException;
}
