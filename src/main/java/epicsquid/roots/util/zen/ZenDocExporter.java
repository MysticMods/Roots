package epicsquid.roots.util.zen;

import com.google.common.base.Optional;
import epicsquid.roots.util.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
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

  public void export(Path path, Class[] classes) {

    for (int i = 0; i < classes.length; i++) {
      StringBuilder out = new StringBuilder();

      ZenDocClass zenClass = (ZenDocClass) classes[i].getDeclaredAnnotation(ZenDocClass.class);
      ZenDocAppend zenDocAppend = (ZenDocAppend) classes[i].getDeclaredAnnotation(ZenDocAppend.class);

      if (zenClass == null) {
        continue;
      }

      if (i > 0) {
        out.append("\n");
      }

      // --- Header

      String[] h3 = zenClass.value().split("\\.");
      String zenClassName = h3[h3.length - 1];
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

      out.append("#### Methods\n");
      out.append("\n");

      Method[] methods = classes[i].getDeclaredMethods();
      List<MethodAnnotationPair> methodList = this.getSortedMethodList(methods);

      // Add static methods to new list.
      List<MethodAnnotationPair> staticMethodList = methodList.stream()
          .filter(pair -> Modifier.isStatic(pair.method.getModifiers()))
          .collect(Collectors.toList());

      // Remove static methods from main list.
      methodList = methodList.stream()
          .filter(pair -> !Modifier.isStatic(pair.method.getModifiers()))
          .collect(Collectors.toList());

      // --- Static Methods

      if (!staticMethodList.isEmpty()) {
        this.writeMethodList(out, staticMethodList);
      }

      // --- Methods

      if (!methodList.isEmpty()) {
        this.writeMethodList(out, methodList);
      }

      // --- Append

      if (zenDocAppend != null) {
        String[] toAppend = zenDocAppend.value();

        out.append("\n");

        for (String s : toAppend) {
          Path p = Paths.get(s);

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

      this.writeMethod(out, staticMethodList.get(j).method, staticMethodList.get(j).annotation);
    }
  }

  private void writeMethod(StringBuilder out, Method method, ZenDocMethod annotation) {

    String methodName = method.getName();
    Class<?> returnType = method.getReturnType();
    String returnTypeString = this.getSimpleTypeString(returnType);

    out.append("```zenscript").append("\n");

    /*if (Modifier.isStatic(method.getModifiers())) {
      out.append("static ");
    }*/

    // Method return type and name
    out.append(returnTypeString).append(" ").append(methodName).append("(");

    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    Class[] types = method.getParameterTypes();
    ZenDocArg[] args = annotation.args();

    if (types.length != args.length) {
      throw new IllegalStateException("Wrong number of parameter names found for method: " + methodName);
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

  private String getSimpleTypeString(Class type) {

    String result = type.getSimpleName();

    if (result.startsWith("Zen")) {
      result = result.substring(3);

    } else if (result.startsWith("String")) {
      result = StringHelper.lowercaseFirstLetter(result);
    }
    return result;
  }

  private static class MethodAnnotationPair {

    public final Method method;
    public final ZenDocMethod annotation;

    private MethodAnnotationPair(Method method, ZenDocMethod annotation) {

      this.method = method;
      this.annotation = annotation;
    }
  }

}
