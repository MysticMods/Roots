package epicsquid.roots.util.zen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// This file and all others in this directory taken from Athenaeeum by CodeTaylor
// Licensed with the Apache License and used with permission
// https://github.com/codetaylor/athenaeum/tree/master/src/main/java/com/codetaylor/mc/athenaeum/tools

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZenDocAppend {

  String[] value();
}
