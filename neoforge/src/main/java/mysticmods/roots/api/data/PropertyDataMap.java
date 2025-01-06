package mysticmods.roots.api.data;

import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;

public class PropertyDataMap {
  public record PropertyDataMapEntry<V, T extends Property<V>> (PropertyHolder<T> holder, V value) {

  }
}
