package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GroveNumber (Grove grove, int value) {
  public static final MapCodec<GroveNumber> MAP_CODEC = RecordCodecBuilder.mapCodec(
      c -> c.group(
          RootsRegistries.GROVES.byNameCodec().fieldOf("grove").forGetter(GroveNumber::grove),
          com.mojang.serialization.Codec.INT.fieldOf("value").forGetter(GroveNumber::value)
      ).apply(c, GroveNumber::new)
  );
  public static final Codec<GroveNumber> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, GroveNumber> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), GroveNumber::grove, ByteBufCodecs.VAR_INT, GroveNumber::value, GroveNumber::new);
}
