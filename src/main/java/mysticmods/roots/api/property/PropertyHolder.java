package mysticmods.roots.api.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record PropertyHolder<T extends Property<?>>(ResourceLocation id, T value) {
  public static final Codec<PropertyHolder<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("id").forGetter(PropertyHolder::id), Property.CODEC.fieldOf("value").forGetter(PropertyHolder::value)).apply(instance, PropertyHolder::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, PropertyHolder<?>> STREAM_CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, PropertyHolder::id, Property.STREAM_CODEC, PropertyHolder::value, PropertyHolder::new);
  public static final Codec<PropertyHolder<?>> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("id").forGetter(PropertyHolder::id), Property.FULL_CODEC.fieldOf("value").forGetter(PropertyHolder::value)).apply(instance, PropertyHolder::new));
  public static final Codec<List<PropertyHolder<?>>> FULL_LIST_CODEC = FULL_CODEC.listOf();

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof PropertyHolder<?> propertyHolder)) return false;
    return this.id.equals(propertyHolder.id);
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public String toString() {
    return this.id.toString();
  }
}
