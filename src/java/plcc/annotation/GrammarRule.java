package plcc.annotation;

//Creating annotation
import java.lang.annotation.*;
import java.lang.reflect.*;

import plcc.annoation.PlccClass;

@PlccClass
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GrammarRule {
	Object[] rules() default new Object[0];
}
