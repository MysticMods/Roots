package mysticmods.roots.api.grove;

import com.google.common.collect.Streams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;

public record GroveNumber(Grove grove, int value, GroveNumberType type) {
  public static final MapCodec<GroveNumber> MAP_CODEC = RecordCodecBuilder.mapCodec(
      c -> c.group(
          RootsRegistries.GROVES.byNameCodec().fieldOf("grove")
              .forGetter(GroveNumber::grove), Codec.INT.fieldOf("value").forGetter(GroveNumber::value), GroveNumberType.CODEC.fieldOf("type").forGetter(GroveNumber::type)
      ).apply(c, GroveNumber::new)
  );
  public static final Codec<GroveNumber> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, GroveNumber> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), GroveNumber::grove, ByteBufCodecs.VAR_INT, GroveNumber::value, GroveNumberType.STREAM_CODEC, GroveNumber::type, GroveNumber::new);

  public GroveNumber(Holder<Grove> grove, int value, GroveNumberType type) {
    this(grove.value(), value, type);
  }

  public static GroveNumber reputation(Holder<Grove> grove, int value) {
    return new GroveNumber(grove, value, GroveNumberType.REPUTATION);
  }

  public static GroveNumber reputation (Grove grove, int value) {
    return new GroveNumber(grove, value, GroveNumberType.REPUTATION);
  }

  public static GroveNumber power(Holder<Grove> grove, int value) {
    return new GroveNumber(grove, value, GroveNumberType.POWER);
  }

  public static GroveNumber power(Grove grove, int value) {
    return new GroveNumber(grove, value, GroveNumberType.POWER);
  }

  public static List<GroveNumber> fromTag(TagKey<Grove> tag, int value, GroveNumberType type) {
    return Streams.stream(RootsRegistries.GROVES.getTagOrEmpty(tag).iterator()).map(g -> new GroveNumber(g, value, type))
        .toList();
  }

  public enum GroveNumberType implements StringRepresentable {
    REPUTATION,
    POWER;

    public static final IntFunction<GroveNumberType> BY_ID = ByIdMap.continuous(GroveNumberType::ordinal, GroveNumberType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<GroveNumberType> CODEC = StringRepresentable.fromEnum(GroveNumberType::values);
    public static final StreamCodec<ByteBuf, GroveNumberType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, GroveNumberType::ordinal);

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase(Locale.ROOT);
    }
  }
}
