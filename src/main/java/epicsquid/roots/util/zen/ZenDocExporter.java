package epicsquid.roots.util.zen;

import epicsquid.roots.util.StringHelper;
import org.apache.commons.lang3.StringUtils;
import stanhebben.zenscript.annotations.Optional;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// This file and all others in this directory taken from Athenaeeum by CodeTaylor
// Licensed with the Apache License and used with permission
// https://github.com/codetaylor/athenaeum/tree/master/src/main/java/com/codetaylor/mc/athenaeum/tools

@SuppressWarnings("unchecked")
public class ZenDocExporter {
  private static String className = "";

  public void export(Path path, Class<?>[] classes) {
    for (int i = 0; i < classes.length; i++) {
      className = "";
      StringBuilder out = new StringBuilder();

      ZenDocClass zenClass = classes[i].getDeclaredAnnotation(ZenDocClass.class);
      ZenDocAppend zenDocAppend = classes[i].getDeclaredAnnotation(ZenDocAppend.class);

      if (zenClass == null) {
        continue;
      }

      if (i > 0) {
        out.append("\n");
      }

      // --- Header

      String[] h3 = zenClass.value().split("\\.");
      String zenClassName = h3[h3.length - 1];
      className = zenClass.value();
      out.append("### Class\n");
      out.append("\n");

      // --- Import

      out.append("```zenscript").append("\n");
      out.append("import ").append(zenClass.value()).append(";").append("\n");
      out.append("```").append("\n");
      out.append("\n");

      // --- Class Description

      String[] description = zenClass.description();

      if (description.length > 0) {

        for (String line : description) {
          out.append(this.parse(line)).append("\n");
        }
        out.append("\n");
      }

      // --- Methods


      Method[] methods = classes[i].getDeclaredMethods();
      List<MethodAnnotationPair> methodList = this.getSortedMethodList(methods);

      Field[] fields = classes[i].getDeclaredFields();
      List<PropertyAnnotationPair> fieldList = this.getSortedFieldList(fields);

      // Add static methods to new list.
      List<MethodAnnotationPair> staticMethodList = methodList.stream()
          .filter(pair -> Modifier.isStatic(pair.type.getModifiers()))
          .collect(Collectors.toList());

      if (!methodList.isEmpty()) {
        out.append("#### Methods\n");
        out.append("\n");
      }

      // Remove static methods from main list.
      methodList = methodList.stream()
          .filter(pair -> !Modifier.isStatic(pair.type.getModifiers()))
          .collect(Collectors.toList());

      // --- Static Methods

      if (!staticMethodList.isEmpty()) {
        this.writeMethodList(out, staticMethodList);
      }

      // --- Methods

      if (!methodList.isEmpty()) {
        this.writeMethodList(out, methodList);
      }

      // --- Fields

      if (!fieldList.isEmpty()) {
        out.append("### Static Properties\n");
        out.append("\n```zenscript\n");
        this.writePropertyList(out, fieldList);
        out.append("\n```");
      }

      // --- Append

      if (zenDocAppend != null) {
        String[] toAppend = zenDocAppend.value();

        out.append("\n");

        for (String s : toAppend) {
          Path p = Paths.get("../roots/" + s);

          try {
            List<String> lines = Files.readAllLines(p);

            for (String line : lines) {
              out.append(line).append("\n");
            }

          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      // --- Output

      try {
        Files.write(path.resolve(zenClassName.toLowerCase() + ".md"), out.toString().getBytes());

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void writeMethodList(StringBuilder out, List<MethodAnnotationPair> staticMethodList) {

    for (int j = 0; j < staticMethodList.size(); j++) {

      if (j > 0) {
        out.append("\n");
      }

      this.writeMethod(out, staticMethodList.get(j).type, staticMethodList.get(j).annotation);
    }
  }

  private void writePropertyList(StringBuilder out, List<PropertyAnnotationPair> staticPropertyList) {
    for (int j = 0; j < staticPropertyList.size(); j++) {
      if (j > 0) {
        out.append("\n");
      }

      this.writeProperty(out, staticPropertyList.get(j).type, staticPropertyList.get(j).annotation);
    }
  }

  private void writeMethod(StringBuilder out, Method method, ZenDocMethod annotation) {

    String methodName = method.getName();
    Class<?> returnType = method.getReturnType();
    String returnTypeString = this.getSimpleTypeString(returnType);

    out.append("```zenscript").append("\n");

    /*if (Modifier.isStatic(type.getModifiers())) {
      out.append("static ");
    }*/

    // Method return type and name
    out.append(returnTypeString).append(" ").append(methodName).append("(");

    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    Class[] types = method.getParameterTypes();
    ZenDocArg[] args = annotation.args();

    if (types.length != args.length) {
      throw new IllegalStateException("Wrong number of parameter names found for type: " + methodName);
    }

    if (args.length > 0) {
      out.append("\n");
    }

    int largest = 0;
    String[] parameterStrings = new String[types.length];

    // find the largest string
    for (int k = 0; k < types.length; k++) {

      boolean optional = false;
      boolean nullable = false;

      for (Annotation parameterAnnotation : parameterAnnotations[k]) {

        if (parameterAnnotation instanceof Optional) {
          optional = true;
        }

        if (parameterAnnotation instanceof ZenDocNullable) {
          nullable = true;
        }
      }

      String optionalString = optional ? "@Optional " : "";
      String nullableString = nullable ? "@Nullable " : "";
      String typeString = this.getSimpleTypeString(types[k]);
      String nameString = args[k].arg();

      if (k < types.length - 1) {
        parameterStrings[k] = "  " + optionalString + nullableString + typeString + " " + nameString + ",";

      } else {
        parameterStrings[k] = "  " + optionalString + typeString + " " + nameString;
      }

      if (parameterStrings[k].length() > largest) {
        largest = parameterStrings[k].length();
      }
    }

    for (int k = 0; k < parameterStrings.length; k++) {
      parameterStrings[k] = StringUtils.rightPad(parameterStrings[k], largest);
      out.append(parameterStrings[k]);

      if (!args[k].info().isEmpty()) {
        out.append(" // ").append(args[k].info());
      }

      out.append("\n");
    }

    out.append(");\n");

    out.append("```").append("\n\n");

    String[] description = annotation.description();

    if (description.length > 0) {

      for (String line : description) {
        out.append(this.parse(line));
      }
    }

    out.append("\n---\n\n");
  }

  private void writeProperty(StringBuilder out, Field field, ZenDocProperty annotation) {
    String fieldName = field.getName();

    String[] h3 = className.split("\\.");
    String zenClassName = h3[h3.length - 1];
    out.append(zenClassName).append(".").append(fieldName).append(" // ");

    ZenDocProperty propertyAnnotation = field.getAnnotation(ZenDocProperty.class);
    for (String line : propertyAnnotation.description()) {
      out.append(line);
    }

    out.append("\n");
  }

  private String parse(String line) {

    if (line.startsWith("@see")) {
      String[] links = line.substring(4).trim().split(" ");

      StringBuilder sb = new StringBuilder("For more information, see:\n");

      for (String link : links) {
        sb.append("  * [").append(link).append("](").append(link).append(")\n");
      }

      return sb.toString();
    }

    return line + "\n";
  }

  private List<MethodAnnotationPair> getSortedMethodList(Method[] methods) {

    List<MethodAnnotationPair> methodList = new ArrayList<>();

    for (Method method : methods) {
      ZenDocMethod annotation = method.getDeclaredAnnotation(ZenDocMethod.class);

      if (annotation != null) {
        methodList.add(new MethodAnnotationPair(method, annotation));
      }
    }

    methodList.sort(Comparator.comparingInt(o -> o.annotation.order()));
    return methodList;
  }

  private List<PropertyAnnotationPair> getSortedFieldList(Field[] fields) {

    List<PropertyAnnotationPair> fieldList = new ArrayList<>();

    for (Field field : fields) {
      ZenDocProperty annotation = field.getDeclaredAnnotation(ZenDocProperty.class);

      if (annotation != null) {
        fieldList.add(new PropertyAnnotationPair(field, annotation));
      }
    }

    fieldList.sort(Comparator.comparingInt(o -> o.annotation.order()));
    return fieldList;
  }

  private String getSimpleTypeString(Class type) {

    String result = type.getSimpleName();

    if (result.startsWith("Zen")) {
      result = result.substring(3);

    } else if (result.startsWith("String")) {
      result = StringHelper.lowercaseFirstLetter(result);
    }
    return result;
  }

  private static class AnnotationPairBase<T, V> {
    public final T type;
    public final V annotation;

    private AnnotationPairBase(T type, V annotation) {
      this.type = type;
      this.annotation = annotation;
    }
  }

  private static class MethodAnnotationPair extends AnnotationPairBase<Method, ZenDocMethod> {
    private MethodAnnotationPair(Method method, ZenDocMethod annotation) {
      super(method, annotation);
    }
  }

  private static class PropertyAnnotationPair extends AnnotationPairBase<Field, ZenDocProperty> {

    private PropertyAnnotationPair(Field method, ZenDocProperty annotation) {
      super(method, annotation);
    }
  }

}
