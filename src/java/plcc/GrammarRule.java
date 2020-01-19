package plcc;

//Creating annotation
import java.lang.annotation.*;
import java.lang.reflect.*;

import plcc.*;

@PlccClass
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GrammarRule {}
