package mysticmods.roots.api.property;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

public interface PropertySerializer<T extends Property<?>> {
  @Nullable
  static PropertySerializer<?> get(ResourceKey<PropertySerializer<?>> id) {
    return RootsRegistries.PROPERTY_SERIALIZERS.get(id);
  }

  MapCodec<T> codec();

  MapCodec<T> fullCodec ();

  StreamCodec<ByteBuf, T> streamCodec();
}
