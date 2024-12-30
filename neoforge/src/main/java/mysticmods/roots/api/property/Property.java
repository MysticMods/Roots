package mysticmods.roots.api.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

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

  public record Serializer<T>(Function<FriendlyByteBuf, T> networkReader,
                              BiFunction<JsonObject, String, T> jsonReader, Function<T, JsonElement> jsonWriter,
                              BiConsumer<FriendlyByteBuf, T> networkWriter) {
  }

  public static Serializer<Integer> INTEGER_SERIALIZER = new Serializer<>(FriendlyByteBuf::readVarInt, GsonHelper::getAsInt, JsonPrimitive::new, FriendlyByteBuf::writeVarInt);
  public static Serializer<Boolean> BOOLEAN_SERIALIZER = new Serializer<>(FriendlyByteBuf::readBoolean, GsonHelper::getAsBoolean, JsonPrimitive::new, FriendlyByteBuf::writeBoolean);
  public static Serializer<Float> FLOAT_SERIALIZER = new Serializer<>(FriendlyByteBuf::readFloat, GsonHelper::getAsFloat, JsonPrimitive::new, FriendlyByteBuf::writeFloat);
  public static Serializer<String> STRING_SERIALIZER = new Serializer<>(FriendlyByteBuf::readUtf, GsonHelper::getAsString, JsonPrimitive::new, FriendlyByteBuf::writeUtf);

  public static Serializer<Double> DOUBLE_SERIALIZER = new Serializer<>(FriendlyByteBuf::readDouble, GsonHelper::getAsDouble, JsonPrimitive::new, FriendlyByteBuf::writeDouble);

}
