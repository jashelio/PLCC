package plcc.annotation;

//Creating annotation
import java.lang.annotation.*;
import java.lang.reflect.*;

import plcc.annotation.PlccClass;

@PlccClass
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunBeforeEachInit {
}
