package mysticmods.roots.test.decompose;

public interface PriorityAssignable {
  Class<?> getAssignableClass();

  default int priority() {
    return 0;
  }
}
