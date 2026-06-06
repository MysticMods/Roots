package mysticmods.roots.api.herb;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.IntFunction;

public enum CostType implements StringRepresentable {
  ADDITIVE(false),
  MULTIPLICATIVE_BASE(true),
  MULTIPLICATIVE_TOTAL(true);

  private final boolean multiplicative;

  CostType(boolean multiplicative) {
    this.multiplicative = multiplicative;
  }

  public boolean isMultiplicative() {
    return multiplicative;
  }

  public boolean isAdditive() {
    return !multiplicative;
  }

  public static final Codec<CostType> CODEC = StringRepresentable.fromEnum(CostType::values);
  public static final IntFunction<CostType> BY_ID = ByIdMap.continuous(CostType::ordinal, CostType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
  public static final StreamCodec<ByteBuf, CostType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CostType::ordinal);

  @Override
  public String getSerializedName() {
    return this.toString().toLowerCase(Locale.ROOT);
  }
}
