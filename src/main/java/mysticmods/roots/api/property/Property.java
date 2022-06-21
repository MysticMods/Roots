package mysticmods.roots.api.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Property<T> {
  private static <T> Class<T> c(Class<?> cls) {
    return (Class<T>) cls;
  }

  protected final T defaultValue;
  protected final Serializer<T> serializer;
  protected T value;

  public Property(/*PropertyType type, */T defaultValue, Serializer<T> serializer) {
    this.defaultValue = defaultValue;
    this.serializer = serializer;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public T getValue() {
    if (value == null) {
      return defaultValue;
    }

    return value;
  }

  public Serializer<T> getSerializer() {
    return serializer;
  }

  public JsonElement valueToJson() {
    return serializer.jsonWriter.apply(value == null ? defaultValue : value);
  }

  public JsonElement defaultValueToJson () {
    return serializer.jsonWriter.apply(defaultValue);
  }

  public void updateFromJson(JsonObject object) {
    this.value = this.serializer.jsonReader.apply(object, "value");
  }

  public void updateFromNetwork(FriendlyByteBuf buffer) {
    this.value = this.serializer.networkReader.apply(buffer);
  }

  public record Serializer<T>(Function<FriendlyByteBuf, T> networkReader,
                              BiFunction<JsonObject, String, T> jsonReader, Function<T, JsonElement> jsonWriter) {
  }

  public static Serializer<Integer> INTEGER_SERIALIZER = new Serializer<>(FriendlyByteBuf::readVarInt, GsonHelper::getAsInt, JsonPrimitive::new);
  public static Serializer<Boolean> BOOLEAN_SERIALIZER = new Serializer<>(FriendlyByteBuf::readBoolean, GsonHelper::getAsBoolean, JsonPrimitive::new);
  public static Serializer<Float> FLOAT_SERIALIZER = new Serializer<>(FriendlyByteBuf::readFloat, GsonHelper::getAsFloat, JsonPrimitive::new);
  public static Serializer<String> STRING_SERIALIZER = new Serializer<>(FriendlyByteBuf::readUtf, GsonHelper::getAsString, JsonPrimitive::new);

  public static class RitualProperty<V> extends Property<V> implements IForgeRegistryEntry<RitualProperty<?>> {
    private ResourceLocation registryName;
    protected ResourceKey<Ritual> ritual;

    public RitualProperty(ResourceKey<Ritual> ritual, V defaultValue, Serializer<V> serializer) {
      super(defaultValue, serializer);
      this.ritual = ritual;
    }

    public ResourceKey<Ritual> getRitual() {
      return ritual;
    }

    @Override
    public RitualProperty<?> setRegistryName(ResourceLocation name) {
      this.registryName = name;
      return this;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public ResourceLocation getRegistryName() {
      return this.registryName;
    }

    @Override
    public Class<RitualProperty<?>> getRegistryType() {
      return c(RitualProperty.class);
    }
  }
}
