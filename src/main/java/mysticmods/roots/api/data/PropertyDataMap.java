package mysticmods.roots.api.data;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;

import java.util.List;

public record PropertyDataMap(List<PropertyHolder<? extends Property<?>>> properties) {
  public static Codec<PropertyDataMap> CODEC = PropertyHolder.FULL_LIST_CODEC.xmap(PropertyDataMap::new, PropertyDataMap::properties);

  public <V, T extends Property<V>> V get(PropertyHolder<T> holder) {
    PropertyHolder<T> myHolder = null;
    for (PropertyHolder<?> property : properties) {
      if (property.id().equals(holder.id())) {
        myHolder = (PropertyHolder<T>) property;
      }
    }
    if (myHolder == null) {
      throw new IllegalStateException("Property does not exist in this PropertyDataMap: " + holder.id());
    }
    return myHolder.value().get();
  }
}
