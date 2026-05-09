package mysticmods.roots.api;

public interface PriorityAssignable {
  Class<?> getAssignableClass ();
  default int priority () {
    return 0;
  }
}
