package mysticmods.roots.api.property;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public abstract class Property<T> {
  protected final T defaultValue;
  protected final Serializer<T> serializer;
  protected String comment;

  public Property(T defaultValue, Serializer<T> serializer, String comment) {
    this.defaultValue = defaultValue;
    this.serializer = serializer;
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  public Serializer<T> getSerializer() {
    return serializer;
  }

  public record Serializer<T>(Codec<T> codec,
                              StreamCodec<? extends ByteBuf, T> streamCodec) {
  }

  public static Serializer<Integer> INTEGER_SERIALIZER = new Serializer<>(Codec.INT, ByteBufCodecs.VAR_INT);
  public static Serializer<Boolean> BOOLEAN_SERIALIZER = new Serializer<>(Codec.BOOL, ByteBufCodecs.BOOL);
  public static Serializer<Float> FLOAT_SERIALIZER = new Serializer<>(Codec.FLOAT, ByteBufCodecs.FLOAT);
  public static Serializer<String> STRING_SERIALIZER = new Serializer<>(Codec.STRING, ByteBufCodecs.STRING_UTF8);

  public static Serializer<Double> DOUBLE_SERIALIZER = new Serializer<>(Codec.DOUBLE, ByteBufCodecs.DOUBLE);

}
