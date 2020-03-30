package edu.rit.gec8773.laps.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;
import java.util.function.ToIntFunction;

/**
 * Indicates the priority of a constructor's grammar rule within a class. The
 * smaller (more negative) the value, earlier the grammar rule checks for a
 * match. If not annotated with this, default value is 0.
 *
 * @see Comparator#comparingInt(ToIntFunction)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface Priority {
    int value() default 0;
}
