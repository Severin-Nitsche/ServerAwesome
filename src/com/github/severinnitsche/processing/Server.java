package com.github.severinnitsche.processing;

import com.github.severinnitsche.annotated.API;
import com.github.severinnitsche.server.FunctionalServer;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Set;

@SupportedAnnotationTypes({"com.github.severinnitsche.annotated.Server", "com.github.severinnitsche.API"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_11)
public class Server extends AbstractProcessor {
  
  private Filer filer;
  private Messager messager;
  
  private HashMap<String, PrintWriter> map = new HashMap<>();
  
  @Override
  public void init(ProcessingEnvironment env) {
    filer = env.getFiler();
    messager = env.getMessager();
  }
  
  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    Class<com.github.severinnitsche.annotated.Server> serverClass = com.github.severinnitsche.annotated.Server.class;
    
    for (Element element : roundEnvironment.getElementsAnnotatedWith(serverClass)) {
      com.github.severinnitsche.annotated.Server annotation = element.getAnnotation(serverClass);
      if (map.get(annotation.id()) == null) {
        try {
          map.put(annotation.id(), new PrintWriter(filer.createSourceFile(annotation.id()).openOutputStream()));
          InputStream stream = this.getClass().getResourceAsStream("res/Boilerplate.template");
          BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
          //String[] boiled = boilerplate.split("\n");
          while (reader.ready()) {
            String line = reader.readLine();
            if (line.contains("%register_calls")) {
              registerCalls(element);
              continue;
            }
            line = line.replaceAll("%id", annotation.id()).replaceAll("%source_long", element.asType().toString()).replaceAll("%source", element.getSimpleName().toString());
            map.get(annotation.id()).println(line);
          }
          reader.close();
          map.get(annotation.id()).close();
        } catch (IOException e) {
          messager.printMessage(Diagnostic.Kind.ERROR, "cannot create file " + annotation.id() + "\n" + e.getMessage());
          return false;
        }
      } else {
        messager.printMessage(Diagnostic.Kind.WARNING, "multiple Files annotated with id " + annotation.id());
      }
    }
    return true;
  }
  
  public void registerCalls(Element element) {
    com.github.severinnitsche.annotated.Server annotation = element.getAnnotation(com.github.severinnitsche.annotated.Server.class);
    for (Element child : element.getEnclosedElements()) {
      if (child.getAnnotation(API.class) == null) continue;
      API api = child.getAnnotation(API.class);
      //server.register(%method,%path,%protocol,%version,%lambda)
      String format = "    server.register(\"%s\",\"%s\",\"%s\",\"%s\",source::%s);";
      String specific = String.format(format, api.method(), api.path(), api.protocol(), api.version(), child.getSimpleName().toString());
      map.get(annotation.id()).println(specific);
      try {
        FunctionalServer s = new FunctionalServer();
        s.register("method", "address", "protocol", "version", (e) -> {
        });
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
