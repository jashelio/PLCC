package edu.rit.gec8773.laps.annotation;

//Creating annotation
import java.lang.annotation.*;

@PlccClass
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenList {}
