package edu.rit.gec8773.laps.annotation;

//Creating annotation
import java.lang.annotation.*;

/**
 * Gives confirmation to the {@link edu.rit.gec8773.laps.Parser} that the
 * annotated type is, in fact, a grammar rule
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GrammarRule {
}
