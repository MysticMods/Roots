package mysticmods.roots.api.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spells.Spell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Property<T> {
  private static <T> Class<T> c(Class<?> cls) {
    return (Class<T>) cls;
  }

  protected final T defaultValue;
  protected final Serializer<T> serializer;
  protected T value;

  public Property(T defaultValue, Serializer<T> serializer) {
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

  public JsonElement serializeValueJson() {
    return serializer.jsonWriter.apply(value == null ? defaultValue : value);
  }

  public JsonElement serializeDefaultValueJson() {
    return serializer.jsonWriter.apply(defaultValue);
  }

  public void serializeNetwork (FriendlyByteBuf buf) {
    if (this.value == null) {
      throw new IllegalStateException("Cannot serialize null value: " + this);
    }
    this.serializer.networkWriter.accept(buf, this.value);
  }

  public void reset () {
    this.value = null;
  }

  public boolean shouldSerialize() {
    return this.value == null || !this.value.equals(defaultValue);
  }

  public void updateFromJson(JsonObject object) {
    this.value = this.serializer.jsonReader.apply(object, "value");
  }

  public void updateFromNetwork(FriendlyByteBuf buffer) {
    this.value = this.serializer.networkReader.apply(buffer);
  }

  public record Serializer<T>(Function<FriendlyByteBuf, T> networkReader,
                              BiFunction<JsonObject, String, T> jsonReader, Function<T, JsonElement> jsonWriter, BiConsumer<FriendlyByteBuf, T> networkWriter) {
  }

  public static Serializer<Integer> INTEGER_SERIALIZER = new Serializer<>(FriendlyByteBuf::readVarInt, GsonHelper::getAsInt, JsonPrimitive::new, FriendlyByteBuf::writeVarInt);
  public static Serializer<Boolean> BOOLEAN_SERIALIZER = new Serializer<>(FriendlyByteBuf::readBoolean, GsonHelper::getAsBoolean, JsonPrimitive::new, FriendlyByteBuf::writeBoolean);
  public static Serializer<Float> FLOAT_SERIALIZER = new Serializer<>(FriendlyByteBuf::readFloat, GsonHelper::getAsFloat, JsonPrimitive::new, FriendlyByteBuf::writeFloat);
  public static Serializer<String> STRING_SERIALIZER = new Serializer<>(FriendlyByteBuf::readUtf, GsonHelper::getAsString, JsonPrimitive::new, FriendlyByteBuf::writeUtf);

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

  public static class SpellProperty<V> extends Property<V> implements IForgeRegistryEntry<SpellProperty<?>> {
    private ResourceLocation registryName;
    protected ResourceKey<Spell> spell;

    public SpellProperty(ResourceKey<Spell> spell, V defaultValue, Serializer<V> serializer) {
      super(defaultValue, serializer);
      this.spell = spell;
    }

    public ResourceKey<Spell> getSpell() {
      return spell;
    }

    @Override
    public SpellProperty<?> setRegistryName(ResourceLocation name) {
      this.registryName = name;
      return this;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public ResourceLocation getRegistryName() {
      return this.registryName;
    }

    @Override
    public Class<SpellProperty<?>> getRegistryType() {
      return c(SpellProperty.class);
    }
  }
}
