package edu.rit.gec8773.laps.annotation;

//Creating annotation
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SemanticEntryPoint {
}
