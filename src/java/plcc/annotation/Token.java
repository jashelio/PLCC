package plcc.annotation;

//Creating annotation
import java.lang.annotation.*;
import java.lang.reflect.*;

import java.util.function.Function;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
	boolean skip() default false;
	Function<Object, String> getValue() default Object::toString;
}
