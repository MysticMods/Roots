package mysticmods.roots.api.property;

import org.jetbrains.annotations.NotNull;

// TODO: comparable?
public class Property<T> implements Comparable<T> {
  private final Class<T> type;
  private T value;
  private T defaultValue;

  public Property (Class<T> clazz, T value, T defaultValue) {
    this.type = clazz;
    this.value = value;
    this.defaultValue = defaultValue;
  }

  @Override
  public int compareTo(@NotNull T o) {
    return 0;
  }
}
