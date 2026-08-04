package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.Util;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface IGroveNumber {
  static <T extends IGroveNumber> Codec<T> codec (Function<GroveNumber, T> builder) {
    return RecordCodecBuilder.create(instance -> instance.group(
            RootsRegistries.GROVES.byNameCodec().fieldOf("grove")
                .forGetter(IGroveNumber::grove), Codec.INT.fieldOf("value")
                .forGetter(IGroveNumber::value), IGroveNumber.Type.CODEC.fieldOf("type").forGetter(IGroveNumber::type)
        ).apply(instance, (a, b, c) -> builder.apply(new GroveNumber(a, b, c))
    ));
  }

  Grove grove();

  int value();

  Type type();

  enum Type implements StringRepresentable {
    REPUTATION,
    POWER;

    public static final IntFunction<Type> BY_ID = ByIdMap.continuous(Type::ordinal, Type.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
    public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::ordinal);

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase(Locale.ROOT);
    }
  }
}
