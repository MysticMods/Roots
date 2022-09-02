package mysticmods.roots.util;

import java.util.Set;

public class SetUtils {
  public static <T> boolean containsAny (Set<T> set, Set<T> other) {
    for (T t : other) {
      if (set.contains(t)) {
        return true;
      }
    }
    return false;
  }
}
