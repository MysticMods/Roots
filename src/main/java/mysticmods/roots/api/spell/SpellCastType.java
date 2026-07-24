package mysticmods.roots.api.spell;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.IntFunction;

public enum SpellCastType implements StringRepresentable {
  INSTANT,
  CONTINUOUS,
  CHARGED,
  TRANSFORMING;

  public static final Codec<SpellCastType> CODEC = StringRepresentable.fromEnum(SpellCastType::values);
  public static final IntFunction<SpellCastType> BY_ID = ByIdMap.continuous(SpellCastType::ordinal, SpellCastType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
  public static final StreamCodec<ByteBuf, SpellCastType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, SpellCastType::ordinal);

  @Override
  public String getSerializedName() {
    return this.toString().toLowerCase(Locale.ROOT);
  }
}
