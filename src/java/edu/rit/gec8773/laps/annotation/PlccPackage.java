package edu.rit.gec8773.laps.annotation;

//Creating annotation  
import java.lang.annotation.*;  
import java.lang.reflect.*; 

@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlccPackage {}
