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
  public enum Charge implements StringRepresentable {
    INSTANCE,  // Charges per instance
    OPERATION, // Charges amount per operation per instance
    DEFAULT;  // For modifiers: charges the default spell

    public static final Codec<Charge> CODEC = StringRepresentable.fromEnum(Charge::values);
    public static final IntFunction<Charge> BY_ID = ByIdMap.continuous(Charge::ordinal, Charge.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, Charge> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Charge::ordinal);

    @Override
    public String getSerializedName() {
      return this.toString().toLowerCase(Locale.ROOT);
    }
  }

  public enum Condition implements StringRepresentable {
    ALWAYS, // Always applies the child cost to the parent
    SPECIFIED; // Only applies the child cost when specified

    public static final Codec<Condition> CODEC = StringRepresentable.fromEnum(Condition::values);
    public static final IntFunction<Condition> BY_ID = ByIdMap.continuous(Condition::ordinal, Condition.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, Condition> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Condition::ordinal);

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
