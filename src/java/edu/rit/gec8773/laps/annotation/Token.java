package edu.rit.gec8773.laps.annotation;

//Creating annotation
import java.lang.annotation.*;
import java.lang.reflect.*;

import java.util.function.Function;

/**
 * Indicates that the annotated field is a token. This field must be public and
 * static to be available to the LAPS library
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
	boolean skip() default false;
}
