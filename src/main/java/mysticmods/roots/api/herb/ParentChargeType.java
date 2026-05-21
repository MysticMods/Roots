package mysticmods.roots.api.herb;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.IntFunction;

public enum ParentChargeType implements StringRepresentable {
  INSTANCE, // Charges per instance
  OPERATION; // Charges amount per operation per instance

  public static final Codec<ParentChargeType> CODEC = StringRepresentable.fromEnum(ParentChargeType::values);
  public static final IntFunction<ParentChargeType> BY_ID = ByIdMap.continuous(ParentChargeType::ordinal, ParentChargeType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
  public static final StreamCodec<ByteBuf, ParentChargeType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ParentChargeType::ordinal);

  @Override
  public String getSerializedName() {
    return this.toString().toLowerCase(Locale.ROOT);
  }
}
