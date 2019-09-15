package epicsquid.roots.util.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PropertyTable implements Iterable<Map.Entry<String, Property<?>>> {
  private final HashMap<Property<?>, Object> map = new HashMap<>();
  private final HashMap<String, Property<?>> reverseMap = new HashMap<>();

  public PropertyTable() {
  }

  public void addProperties (Property<?>... properties) {
    for (Property prop : properties) {
      map.put(prop, null);
      reverseMap.put(prop.name, prop);
    }
  }

  @SuppressWarnings("unchecked")
  public <T> Property<T> getProperty (String propertyName) {
    Property<?> prop = reverseMap.get(propertyName);
    if (prop == null) {
      return null;
    }

    return (Property<T>) prop;
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

  @Override
  public Iterator<Map.Entry<String, Property<?>>> iterator() {
    return reverseMap.entrySet().iterator();
  }
}

