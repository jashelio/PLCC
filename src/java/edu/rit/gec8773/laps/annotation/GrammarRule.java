package edu.rit.gec8773.laps.annotation;

//Creating annotation
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GrammarRule {
}
