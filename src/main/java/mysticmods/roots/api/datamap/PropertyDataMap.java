package mysticmods.roots.api.datamap;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PropertyDataMap(List<PropertyHolder<? extends Property<?>>> properties) {
  public static Codec<PropertyDataMap> CODEC = PropertyHolder.FULL_LIST_CODEC.xmap(PropertyDataMap::new, PropertyDataMap::properties);
  public static StreamCodec<RegistryFriendlyByteBuf, PropertyDataMap> STREAM_CODEC = StreamCodec.composite(PropertyHolder.STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.properties, PropertyDataMap::new);

  public PropertyDataMap {
    Set<ResourceLocation> ids = new HashSet<>();
    for (PropertyHolder<?> property : properties) {
      if (ids.contains(property.id())) {
        throw new IllegalArgumentException("Duplicate id found in property map: '" + property.id() + "' already exists!");
      }
      ids.add(property.id());
    }
  }

  public <V, T extends Property<V>> V get(PropertyHolder<T> holder) {
    PropertyHolder<T> myHolder = null;
    for (PropertyHolder<?> property : properties) {
      if (property.id().equals(holder.id())) {
        //noinspection unchecked
        myHolder = (PropertyHolder<T>) property;
        break;
      }
    }
    if (myHolder == null) {
      throw new IllegalStateException("Property does not exist in this PropertyDataMap: " + holder.id());
    }
    return myHolder.value().get();
  }
}
