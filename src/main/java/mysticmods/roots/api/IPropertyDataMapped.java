package mysticmods.roots.api;

import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;

public interface IPropertyDataMapped {
  PropertyDataMap getPropertyMap();

  default <T> T getValue(PropertyHolder<? extends Property<T>> property) {
    PropertyDataMap map = getPropertyMap();
    return map.get(property);
  }
}
