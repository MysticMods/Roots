package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.registry.RootsRegistries;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApiStatus.Internal
public class P {
  private static final Set<PropertyHolder<Property.IntegerProperty>> INT_PROPERTIES = new HashSet<>();
  private static final Set<PropertyHolder<Property.FloatProperty>> FLOAT_PROPERTIES = new HashSet<>();
  private static final Set<PropertyHolder<Property.DoubleProperty>> DOUBLE_PROPERTIES = new HashSet<>();
  private static final Set<PropertyHolder<Property.BooleanProperty>> BOOLEAN_PROPERTIES = new HashSet<>();
  private static final Set<PropertyHolder<Property.StringProperty>> STRING_PROPERTIES = new HashSet<>();

  public static PropertyHolder<Property.BooleanProperty> recordProperty(String name, Property.BooleanProperty property) {
    PropertyHolder<Property.BooleanProperty> holder = new PropertyHolder<>(RootsAPI.rl(name), property);
    BOOLEAN_PROPERTIES.add(holder);
    return holder;
  }

  public static PropertyHolder<Property.StringProperty> recordProperty(String name, Property.StringProperty property) {
    PropertyHolder<Property.StringProperty> holder = new PropertyHolder<>(RootsAPI.rl(name), property);
    STRING_PROPERTIES.add(holder);
    return holder;
  }

  public static PropertyHolder<Property.IntegerProperty> recordProperty(String name, Property.IntegerProperty property) {
    PropertyHolder<Property.IntegerProperty> holder = new PropertyHolder<>(RootsAPI.rl(name), property);
    INT_PROPERTIES.add(holder);
    return holder;
  }

  public static PropertyHolder<Property.FloatProperty> recordProperty(String name, Property.FloatProperty property) {
    PropertyHolder<Property.FloatProperty> holder = new PropertyHolder<>(RootsAPI.rl(name), property);
    FLOAT_PROPERTIES.add(holder);
    return holder;
  }

  public static PropertyHolder<Property.DoubleProperty> recordProperty(String name, Property.DoubleProperty property) {
    PropertyHolder<Property.DoubleProperty> holder = new PropertyHolder<>(RootsAPI.rl(name), property);
    DOUBLE_PROPERTIES.add(holder);
    return holder;
  }

  public static List<PropertyHolder<?>> unclaimed () {
    Set<PropertyHolder<?>> CLAIMED = new HashSet<>();
    RootsRegistries.SPELLS.stream().forEach(o -> CLAIMED.addAll(o.getProperties()));
    RootsRegistries.RITUALS.stream().forEach(o -> CLAIMED.addAll(o.getProperties()));
    List<PropertyHolder<?>> unclaimed = new ArrayList<>();
    for (PropertyHolder<?> property : INT_PROPERTIES) {
      if (!CLAIMED.contains(property)) {
        unclaimed.add(property);
      }
    }
    for (PropertyHolder<?> property : FLOAT_PROPERTIES) {
      if (!CLAIMED.contains(property)) {
        unclaimed.add(property);
      }
    }
    for (PropertyHolder<?> property : DOUBLE_PROPERTIES) {
      if (!CLAIMED.contains(property)) {
        unclaimed.add(property);
      }
    }
    for (PropertyHolder<?> property : BOOLEAN_PROPERTIES) {
      if (!CLAIMED.contains(property)) {
        unclaimed.add(property);
      }
    }
    for (PropertyHolder<?> property : STRING_PROPERTIES) {
      if (!CLAIMED.contains(property)) {
        unclaimed.add(property);
      }
    }
    return unclaimed;
  }
}
