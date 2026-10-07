package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Locale;
import java.util.function.IntFunction;

public interface IGroveValue {
  static <T extends IGroveValue> MapCodec<T> mapCodec (TriFunction<Grove, Integer, Type, T> builder) {
    return RecordCodecBuilder.mapCodec(instance ->
        instance.group(RootsRegistries.GROVES.byNameCodec().fieldOf("grove").forGetter(IGroveValue::grove), Codec.INT.fieldOf("value").forGetter(IGroveValue::value), Type.CODEC.fieldOf("type").forGetter(IGroveValue::type)).apply(instance, builder::apply));
  }

  static <T extends IGroveValue> StreamCodec<RegistryFriendlyByteBuf, T> streamCodec (TriFunction<Grove, Integer, Type, T> builder) {
    return StreamCodec.composite(
        ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), IGroveValue::grove, ByteBufCodecs.VAR_INT, IGroveValue::value, Type.STREAM_CODEC, IGroveValue::type, builder::apply);
  }

  Grove grove();

  int value();

  Type type();

  enum Type implements StringRepresentable {
    REPUTATION_GAIN,
    REPUTATION_LOSS,
    POWER_GENERATION,
    POWER_CONSUMPTION;

    public static final IntFunction<Type> BY_ID = ByIdMap.continuous(Type::ordinal, Type.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
    public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::ordinal);

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase(Locale.ROOT);
    }
  }
}
