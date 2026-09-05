package mysticmods.roots.api;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.IntFunction;

public class SpellType {
  public enum Primary implements StringRepresentable {
    INSTANCE, // Charges per instance
    OPERATION; // Charges amount per operation per instance

    public static final Codec<Primary> CODEC = StringRepresentable.fromEnum(Primary::values);
    public static final IntFunction<Primary> BY_ID = ByIdMap.continuous(Primary::ordinal, Primary.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, Primary> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Primary::ordinal);

    @Override
    public String getSerializedName() {
      return this.toString().toLowerCase(Locale.ROOT);
    }
  }

  public enum Secondary implements StringRepresentable {
    ALWAYS, // Always applies the child cost to the parent
    SPECIFIED; // Only applies the child cost when specified

    public static final Codec<Secondary> CODEC = StringRepresentable.fromEnum(Secondary::values);
    public static final IntFunction<Secondary> BY_ID = ByIdMap.continuous(Secondary::ordinal, Secondary.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, Secondary> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Secondary::ordinal);

    @Override
    public String getSerializedName() {
      return this.toString().toLowerCase(Locale.ROOT);
    }
  }

  public enum Cast implements StringRepresentable {
    INSTANT,
    CONTINUOUS,
    CHARGED;

    public static final Codec<Cast> CODEC = StringRepresentable.fromEnum(Cast::values);
    public static final IntFunction<Cast> BY_ID = ByIdMap.continuous(Cast::ordinal, Cast.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, Cast> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Cast::ordinal);

    @Override
    public String getSerializedName() {
      return this.toString().toLowerCase(Locale.ROOT);
    }
  }
}
