package edu.rit.gec8773.laps.annotation;

import edu.rit.gec8773.laps.parser.Parser;

import java.lang.annotation.*;

/**
 * Gives confirmation to the {@link Parser} that the
 * annotated type is, in fact, a grammar rule
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GrammarRule {
}
