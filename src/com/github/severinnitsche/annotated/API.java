package com.github.severinnitsche.annotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface API {
  String method();
  
  String protocol();
  
  String version();
  
  String path();
}
