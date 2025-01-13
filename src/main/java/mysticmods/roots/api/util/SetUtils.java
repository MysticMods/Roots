package mysticmods.roots.api.util;

import java.util.Set;

public class SetUtils {
  public static <T> boolean containsAny(Set<T> set1, Set<T> set2) {
    if (set2.size() <= set1.size()) {
      for (T t : set2) {
        if (set1.contains(t)) {
          return true;
        }
      }
    } else {
      for (T t : set1) {
        if (set2.contains(t)) {
          return true;
        }
      }
    }
    return false;
  }
}
