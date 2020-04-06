package edu.rit.gec8773.laps.annotation;

//Creating annotation
import edu.rit.gec8773.laps.parser.topDown.TopDownParser;

import java.lang.annotation.*;

/**
 * Gives confirmation to the {@link TopDownParser} that the
 * annotated type is, in fact, a grammar rule
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GrammarRule {
}
