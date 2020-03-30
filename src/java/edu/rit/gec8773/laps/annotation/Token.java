package edu.rit.gec8773.laps.annotation;

//Creating annotation
import java.lang.annotation.*;
import java.lang.reflect.*;

import java.util.function.Function;

/**
 * Indicates that the annotated field or method is a token. This target must be
 * public and static to be available to the LAPS library
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
	boolean skip() default false;
}
