package mysticmods.roots.test.decompose;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PriorityAssignableMap<A extends PriorityAssignable> {
  private final A NONE;
  private final Map<Class<?>, A> byClass;
  private final List<A> all = new ArrayList<>();

  public PriorityAssignableMap(A none, boolean concurrent) {
    this.NONE = none;
    this.byClass = concurrent ? new ConcurrentHashMap<>() : new HashMap<>();
  }

  public PriorityAssignableMap(A none) {
    this(none, false);
  }

  public PriorityAssignableMap(A none, Iterable<A> entries) {
    this(none, false);
    entries.forEach(all::add);
    byClass.clear();
  }

  @Deprecated
  public void register(A priorityAssignable) {
    all.add(priorityAssignable);
    byClass.clear();
  }

  @Nullable
  public A get(@Nullable Object type) {
    if (type == null) {
      return null;

    }
    Class<?> clazz = type.getClass();
    A potentialAdapter = byClass.computeIfAbsent(clazz, clazz2 -> {
      A best = null;
      int bestDistance = Integer.MAX_VALUE;
      int bestPriority = Integer.MIN_VALUE;

      for (A adapter : all) {
        if (!adapter.getAssignableClass().isAssignableFrom(clazz2)) {
          continue;
        }

        int d = distance(clazz2, adapter);
        int p = adapter.priority();

        if (d < bestDistance || (d == bestDistance && p > bestPriority)) {
          best = adapter;
          bestDistance = d;
          bestPriority = p;
        }
      }
      return best == null ? NONE : best;
    });
    if (potentialAdapter == NONE) {
      return null;
    }
    return potentialAdapter;
  }

  private static <A extends PriorityAssignable> int distance(Class<?> runtime, A target) {
    Class<?> targetClass = target.getAssignableClass();
    int d = 0;

    for (Class<?> c = runtime; c != null; c = c.getSuperclass()) {
      if (c == targetClass) return d;
      d++;
    }

    return Integer.MAX_VALUE;
  }
}
