package edu.rit.gec8773.laps.annotation;

//Creating annotation
import java.lang.annotation.*;

/**
 * Indicates that the annotated method should be run before each instantiation
 * in that method's declaring class as a static method
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunBeforeEachInit {
}
