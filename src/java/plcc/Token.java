package plcc;

//Creating annotation
import java.lang.annotation.*;
import java.lang.reflect.*;

import plcc.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
	boolean skip() default false;
}
