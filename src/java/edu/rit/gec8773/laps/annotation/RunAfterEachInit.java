package edu.rit.gec8773.laps.annotation;

import java.lang.annotation.*;

/**
 * Indicates that the annotated method should be run after each instantiation
 * in that method's declaring class as an instance method. Note that if more
 * than one method is annotated, there is no guarantee which order the methods
 * will run in.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunAfterEachInit {
}
