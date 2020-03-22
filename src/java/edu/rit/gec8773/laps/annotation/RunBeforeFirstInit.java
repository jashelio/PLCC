package edu.rit.gec8773.laps.annotation;

//Creating annotation
import java.lang.annotation.*;

/**
 * Indicates that the annotated method should be run before the first
 * instantiation in that method's declaring class as a static method. Note that
 * if more than one method is annotated, there is no guarantee which order the
 * methods will run in.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunBeforeFirstInit {
}
