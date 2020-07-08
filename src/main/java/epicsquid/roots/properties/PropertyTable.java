package epicsquid.roots.properties;

import javax.annotation.Nullable;
import java.util.*;

public class PropertyTable implements Iterable<Map.Entry<String, Property<?>>> {
  private final Map<Property<?>, Object> map = new HashMap<>();
  private final Map<String, Property<?>> reverseMap = new HashMap<>();
  private final Set<String> fetchedKeys = new HashSet<>();

  public PropertyTable() {
  }

  public Collection<Property<?>> getProperties () {
    return reverseMap.values();
  }

  public void addProperties (Property<?>... properties) {
    for (Property prop : properties) {
      map.put(prop, null);
      reverseMap.put(prop.getName(), prop);
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T getValue (String propertyName) {
    fetchedKeys.add(propertyName);
    Property<T> prop = (Property<T>) reverseMap.get(propertyName);
    return get(prop);
  }

  @SuppressWarnings("unchecked")
  public <T> Property<T> get(String propertyName) {
    fetchedKeys.add(propertyName);
    Property<?> prop = reverseMap.get(propertyName);
    if (prop == null) {
      return null;
    }

    return (Property<T>) prop;
  }

  public <T> Property<T> get(String propertyName, T value) throws ClassCastException {
    Property<?> prop = reverseMap.get(propertyName);
    if (!prop.getType().equals(value.getClass())) {
      throw new ClassCastException("Invalid cast: cannot cast " + prop.getType().getSimpleName() + " into " + value.getClass().getSimpleName());
    }
    return get(propertyName);
  }

  public <T> T get(Property<T> property) {
    fetchedKeys.add(property.getName());
    T result = property.cast(map.get(property));
    if (result == null && property.hasDefaultValue()) {
      return property.getDefaultValue();
    } else {
      return result;
    }
  }

  public static class InvalidPropetyValue extends IllegalArgumentException {
    public InvalidPropetyValue() {
    }

    public InvalidPropetyValue(String s) {
      super(s);
    }

    public InvalidPropetyValue(String message, Throwable cause) {
      super(message, cause);
    }

    public InvalidPropetyValue(Throwable cause) {
      super(cause);
    }
  }

  public <T> void set(Property<T> property, T value) {
    if (!property.validate(value)) {
      throw new InvalidPropetyValue("Value " + value + " is not valid for property " + property.getName());
    }
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
    Property<?> pX = get(radiusPrefix + "_x");
    Property<?> pY = get(radiusPrefix + "_y");
    Property<?> pZ = get(radiusPrefix + "_z");
    if (pX == null || pY == null || pZ == null) {
      return null;
    }
    int x, y, z;
    try {
      x = get((Property<Integer>) pX);
      y = get((Property<Integer>) pY);
      z = get((Property<Integer>) pZ);
    } catch (ClassCastException ignored) {
      return null;
    }

    return new int[]{x, y, z};
  }

  public boolean hasProperty (Property<?> property) {
    return map.containsKey(property);
  }

  public boolean hasProperty (String prop) {
    return reverseMap.containsKey(prop);
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

