package mysticmods.roots.api.herb;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.IntFunction;

// TODO: Move from CostInstance to Spell
public enum ChargeType implements StringRepresentable {
  INSTANCE, // Charges per instance
  OPERATION,
  CONDITIONAL; // Charges amount per operation per instance

  public static final Codec<ChargeType> CODEC = StringRepresentable.fromEnum(ChargeType::values);
  public static final IntFunction<ChargeType> BY_ID = ByIdMap.continuous(ChargeType::ordinal, ChargeType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
  public static final StreamCodec<ByteBuf, ChargeType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ChargeType::ordinal);

  @Override
  public String getSerializedName() {
    return this.toString().toLowerCase(Locale.ROOT);
  }
}
