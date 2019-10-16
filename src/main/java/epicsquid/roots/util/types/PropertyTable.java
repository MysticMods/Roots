package epicsquid.roots.util.types;

import javax.annotation.Nullable;
import java.util.*;

public class PropertyTable implements Iterable<Map.Entry<String, Property<?>>> {
  private final Map<Property<?>, Object> map = new HashMap<>();
  private final Map<String, Property<?>> reverseMap = new HashMap<>();
  private final Set<String> fetchedKeys = new HashSet<>();

  public PropertyTable() {
  }

  public void addProperties (Property<?>... properties) {
    for (Property prop : properties) {
      map.put(prop, null);
      reverseMap.put(prop.getName(), prop);
    }
  }

  @SuppressWarnings("unchecked")
  public <T> Property<T> getProperty (String propertyName) {
    fetchedKeys.add(propertyName);
    Property<?> prop = reverseMap.get(propertyName);
    if (prop == null) {
      return null;
    }

    return (Property<T>) prop;
  }

  public <T> T getProperty (Property<T> property) {
    fetchedKeys.add(property.getName());
    T result = property.cast(map.get(property));
    if (result == null && property.hasDefaultValue()) {
      return property.getDefaultValue();
    } else {
      return result;
    }
  }

  public <T> void setProperty (Property<T> property, T value) {
    map.put(property, value);
  }

  public int[] getRadius () {
    int[] radius = getRadius("radius");
    if (radius == null) {
      throw new IllegalArgumentException("This property table does not contain radius_x, radius_y and/or radius_z");
    }
    return radius;
  }

  @SuppressWarnings("unchecked")
  @Nullable
  public int[] getRadius (String radiusPrefix) {
    Property<?> pX = getProperty(radiusPrefix + "_x");
    Property<?> pY = getProperty(radiusPrefix + "_y");
    Property<?> pZ = getProperty(radiusPrefix + "_z");
    if (pX == null || pY == null || pZ == null) {
      return null;
    }
    int x, y, z;
    try {
      x = getProperty((Property<Integer>) pX);
      y = getProperty((Property<Integer>) pY);
      z = getProperty((Property<Integer>) pZ);
    } catch (ClassCastException ignored) {
      return null;
    }

    return new int[]{x, y, z};
  }

  public boolean hasProperty (Property<?> property) {
    return map.containsKey(property);
  }

  @Override
  public Iterator<Map.Entry<String, Property<?>>> iterator() {
    return reverseMap.entrySet().iterator();
  }

  public List<String> finalise () {
    ArrayList<String> result = new ArrayList<>();
    for (String key : reverseMap.keySet()) {
      if (!fetchedKeys.contains(key)) {
        result.add(key);
      }
    }
    return result;
  }
}

