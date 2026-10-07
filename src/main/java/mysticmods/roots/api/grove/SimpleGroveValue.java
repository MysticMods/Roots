package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SimpleGroveValue(Grove grove, int value, IGroveValue.Type type) implements IGroveValue {
  public static final MapCodec<SimpleGroveValue> MAP_CODEC = RecordCodecBuilder.mapCodec(
      c -> c.group(
          RootsRegistries.GROVES.byNameCodec().fieldOf("grove")
              .forGetter(SimpleGroveValue::grove), Codec.INT.fieldOf("value")
              .forGetter(SimpleGroveValue::value), IGroveValue.Type.CODEC.fieldOf("type").forGetter(SimpleGroveValue::type)
      ).apply(c, SimpleGroveValue::new)
  );
  public static final Codec<SimpleGroveValue> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, SimpleGroveValue> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), SimpleGroveValue::grove, ByteBufCodecs.VAR_INT, SimpleGroveValue::value, IGroveValue.Type.STREAM_CODEC, SimpleGroveValue::type, SimpleGroveValue::new);

  public static SimpleGroveValue reputation(Holder<Grove> grove, int value) {
    return reputation(grove.value(), value);
  }

  public static SimpleGroveValue reputation(Grove grove, int value) {
    return new SimpleGroveValue(grove, value, value < 0 ? SimpleGroveValue.Type.REPUTATION_LOSS : SimpleGroveValue.Type.REPUTATION_GAIN);
  }

  public static SimpleGroveValue generate(Holder<Grove> grove, int value) {
    return generate(grove.value(), value);
  }

  public static SimpleGroveValue generate(Grove grove, int value) {
    return new SimpleGroveValue(grove, value, Type.POWER_GENERATION);
  }
}
