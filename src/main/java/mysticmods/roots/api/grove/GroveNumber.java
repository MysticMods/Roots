package mysticmods.roots.api.grove;

import com.google.common.collect.Streams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;

import java.util.List;

public record GroveNumber(Grove grove, int value, IGroveNumber.Type type) implements IGroveNumber {
  public static final MapCodec<GroveNumber> MAP_CODEC = RecordCodecBuilder.mapCodec(
      c -> c.group(
          RootsRegistries.GROVES.byNameCodec().fieldOf("grove")
              .forGetter(GroveNumber::grove), Codec.INT.fieldOf("value")
              .forGetter(GroveNumber::value), IGroveNumber.Type.CODEC.fieldOf("type").forGetter(GroveNumber::type)
      ).apply(c, GroveNumber::new)
  );
  public static final Codec<GroveNumber> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, GroveNumber> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), GroveNumber::grove, ByteBufCodecs.VAR_INT, GroveNumber::value, IGroveNumber.Type.STREAM_CODEC, GroveNumber::type, GroveNumber::new);

  public GroveNumber(Holder<Grove> grove, int value, IGroveNumber.Type type) {
    this(grove.value(), value, type);
  }

  public static GroveNumber reputation(Holder<Grove> grove, int value) {
    return new GroveNumber(grove, value, IGroveNumber.Type.REPUTATION);
  }

  public static GroveNumber reputation(Grove grove, int value) {
    return new GroveNumber(grove, value, IGroveNumber.Type.REPUTATION);
  }

  public static GroveNumber power(Holder<Grove> grove, int value) {
    return new GroveNumber(grove, value, IGroveNumber.Type.POWER);
  }

  public static GroveNumber power(Grove grove, int value) {
    return new GroveNumber(grove, value, IGroveNumber.Type.POWER);
  }

  public static List<GroveNumber> fromTag(TagKey<Grove> tag, int value, IGroveNumber.Type type) {
    return Streams.stream(RootsRegistries.GROVES.getTagOrEmpty(tag).iterator())
        .map(g -> new GroveNumber(g, value, type))
        .toList();
  }

  public static List<GroveNumber> all(int value, IGroveNumber.Type type) {
    return Streams.stream(RootsRegistries.GROVES).map(o -> new GroveNumber(o, value, type)).toList();
  }
}
