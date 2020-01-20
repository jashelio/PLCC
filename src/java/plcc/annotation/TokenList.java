package plcc.annotation;

//Creating annotation
import java.lang.annotation.*;
import java.lang.reflect.*;

import plcc.annotation.PlccClass;

@PlccClass
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenList {}
