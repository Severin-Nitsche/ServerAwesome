package com.github.severinnitsche.processing.base;

import com.github.severinnitsche.annotated.Server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractServer<T> {
  
  public abstract T getSource();
  
  public abstract void init();
  
  public abstract void shutdown() throws IOException;
  
  public static <V> AbstractServer<V> get(Class<V> clazz, String config, Object... initArgs) {
    Server annotation = clazz.getAnnotation(Server.class);
    try {
      Class<?> source = Class.forName("com.github.severinnitsche.pre.generated." + annotation.id());
      Object[] args = {config, initArgs};
      return (AbstractServer<V>) source.getConstructor(String.class, Object[].class).newInstance(args);
    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public static <V> AbstractServer<V> get(Class<V> clazz, Object... initArgs) {
    Server annotation = clazz.getAnnotation(Server.class);
    try {
      Class<?> source = Class.forName("com.github.severinnitsche.pre.generated." + annotation.id());
      Object[] args = {initArgs};
      return (AbstractServer<V>) source.getConstructor(Object[].class).newInstance(args);
    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }
  
}
