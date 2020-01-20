package plcc.annotation;

//Creating annotation  
import java.lang.annotation.*;  
import java.lang.reflect.*; 

@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlccPackage {}
