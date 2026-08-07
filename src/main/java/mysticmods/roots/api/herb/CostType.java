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
  ADDITIVE(),
  MULTIPLICATIVE_BASE(),
  MULTIPLICATIVE_TOTAL(),
  NEGATE_BASE_COST();
  CostType() {
  }

  public boolean isMultiplicative() {
    return this == MULTIPLICATIVE_BASE || this == MULTIPLICATIVE_TOTAL;
  }

  public boolean isNegative () {
    return this == NEGATE_BASE_COST;
  }

  public boolean isAdditive() {
    return !isMultiplicative() && !isNegative();
  }

  public static final Codec<CostType> CODEC = StringRepresentable.fromEnum(CostType::values);
  public static final IntFunction<CostType> BY_ID = ByIdMap.continuous(CostType::ordinal, CostType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
  public static final StreamCodec<ByteBuf, CostType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CostType::ordinal);

  @Override
  public String getSerializedName() {
    return this.toString().toLowerCase(Locale.ROOT);
  }
}
