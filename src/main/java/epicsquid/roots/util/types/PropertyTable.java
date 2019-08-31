package epicsquid.roots.util.types;

import java.util.HashMap;

public class PropertyTable {
  private final HashMap<Property<?>, Object> map = new HashMap<>();

  public PropertyTable() {
  }

  public void addProperties (Property<?>... properties) {
    for (Property prop : properties) {
      map.put(prop, null);
    }
  }

  public <T> T getProperty (Property<T> property) {
    T result = property.cast(map.get(property));
    if (result == null) {
      return property.defaultValue;
    }
    return result;
  }

  public <T> void setProperty (Property<T> property, T value) {
    map.put(property, value);
  }

  public boolean hasProperty (Property<?> property) {
    return map.containsKey(property);
  }
}

