package mysticmods.roots.api.modifier;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.IntFunction;

public enum ChildChargeType implements StringRepresentable {
  ALWAYS, // Always applies the child cost to the parent
  SPECIFIED; // Only applies the child cost when specified

  public static final Codec<ChildChargeType> CODEC = StringRepresentable.fromEnum(ChildChargeType::values);
  public static final IntFunction<ChildChargeType> BY_ID = ByIdMap.continuous(ChildChargeType::ordinal, ChildChargeType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
  public static final StreamCodec<ByteBuf, ChildChargeType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ChildChargeType::ordinal);

  @Override
  public String getSerializedName() {
    return this.toString().toLowerCase(Locale.ROOT);
  }
}
