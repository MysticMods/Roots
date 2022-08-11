package epicsquid.roots.util.zen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZenDocMethod {
	
	int order() default 0;
	
	String[] description() default {};
	
	ZenDocArg[] args() default {};
}
