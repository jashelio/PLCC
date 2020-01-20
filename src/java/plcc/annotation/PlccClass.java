package plcc.annotation;

//Creating annotation  
import java.lang.annotation.*;  
import java.lang.reflect.*; 

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface PlccClass {}
