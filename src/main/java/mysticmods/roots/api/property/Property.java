package mysticmods.roots.api.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Property<T> {
  Codec<Property<?>> CODEC = RootsRegistries.PROPERTY_SERIALIZERS.byNameCodec().dispatch(Property::getSerializer, PropertySerializer::codec);
  Codec<Property<?>> FULL_CODEC = RootsRegistries.PROPERTY_SERIALIZERS.byNameCodec().dispatch(Property::getSerializer, PropertySerializer::fullCodec);
  StreamCodec<RegistryFriendlyByteBuf, Property<?>> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.PROPERTY_SERIALIZERS).dispatch(Property::getSerializer, PropertySerializer::streamCodec);

  String getComment();

  T getDefaultValue();

  @Nullable
  T getValue();

  @NotNull
  default T get() {
    T val = getValue();
    if (val == null) {
      return getDefaultValue();
    }
    return val;
  }

  PropertySerializer<?> getSerializer();

  PropertyType<?> getType();

  static IntegerProperty ofInt(int value, String comment) {
    return new IntegerProperty(value, comment);
  }

  static BooleanProperty ofBool(boolean value, String comment) {
    return new BooleanProperty(value, comment);
  }

  static FloatProperty ofFloat(float value, String comment) {
    return new FloatProperty(value, comment);
  }

  static DoubleProperty ofDouble(double value, String comment) {
    return new DoubleProperty(value, comment);
  }

  static StringProperty ofString(String value, String comment) {
    return new StringProperty(value, comment);
  }

  record IntegerProperty(int defaultValue, String comment, @Nullable Integer value) implements Property<Integer> {
    public static final ResourceKey<PropertySerializer<?>> SERIALIZER = ResourceKey.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.rl("integer_property"));
    public static final ResourceKey<PropertyType<?>> TYPE = ResourceKey.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.rl("integer_property"));

    public IntegerProperty(int defaultValue, String comment, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Integer> value) {
      this(defaultValue, comment, value.orElse(null));
    }

    public IntegerProperty(int value, String comment) {
      this(value, comment, (Integer) null);
    }

    @Override
    public String getComment() {
      return comment();
    }

    @Override
    public Integer getDefaultValue() {
      return defaultValue();
    }

    @Nullable
    @Override
    public Integer getValue() {
      return value();
    }

    @Override
    public PropertySerializer<?> getSerializer() {
      return PropertySerializer.get(SERIALIZER);
    }

    @Override
    public PropertyType<?> getType() {
      return PropertyType.get(TYPE);
    }

    public static class Serializer implements PropertySerializer<IntegerProperty> {
      public static final MapCodec<IntegerProperty> CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.INT.fieldOf("default_value").forGetter(IntegerProperty::defaultValue),
              Codec.STRING.fieldOf("comment").forGetter(IntegerProperty::comment),
              Codec.INT.optionalFieldOf("value", null).forGetter(o -> o.value)
          ).apply(group, IntegerProperty::new));
      public static final MapCodec<IntegerProperty> FULL_CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.INT.fieldOf("default_value").forGetter(IntegerProperty::defaultValue),
              Codec.STRING.fieldOf("comment").forGetter(IntegerProperty::comment),
              Codec.INT.fieldOf("value").forGetter(o -> o.value == null ? o.defaultValue : o.value)
          ).apply(group, IntegerProperty::new));
      public static final StreamCodec<ByteBuf, IntegerProperty> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.VAR_INT, o -> o.defaultValue,
          ByteBufCodecs.STRING_UTF8, o -> o.comment,
          ByteBufCodecs.VAR_INT.apply(ByteBufCodecs::optional), o -> Optional.ofNullable(o.value),
          IntegerProperty::new
      );

      @Override
      public MapCodec<IntegerProperty> codec() {
        return CODEC;
      }

      @Override
      public MapCodec<IntegerProperty> fullCodec() {
        return FULL_CODEC;
      }

      @Override
      public StreamCodec<ByteBuf, IntegerProperty> streamCodec() {
        return STREAM_CODEC;
      }
    }

    public static class Type implements PropertyType<IntegerProperty> {
      @Override
      public String toString() {
        return "integer_property";
      }
    }
  }

  record BooleanProperty(boolean defaultValue, String comment, Boolean value) implements Property<Boolean> {
    public static final ResourceKey<PropertySerializer<?>> SERIALIZER = ResourceKey.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.rl("boolean_property"));
    public static final ResourceKey<PropertyType<?>> TYPE = ResourceKey.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.rl("boolean_property"));

    public BooleanProperty(boolean defaultValue, String comment, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Boolean> value) {
      this(defaultValue, comment, value.orElse(null));
    }

    public BooleanProperty(boolean value, String comment) {
      this(value, comment, (Boolean) null);
    }

    @Override
    public String getComment() {
      return comment();
    }

    @Override
    public Boolean getDefaultValue() {
      return defaultValue();
    }

    @Override
    public @Nullable Boolean getValue() {
      return value();
    }

    @Override
    public PropertySerializer<?> getSerializer() {
      return PropertySerializer.get(SERIALIZER);
    }

    @Override
    public PropertyType<?> getType() {
      return PropertyType.get(TYPE);
    }

    public static class Serializer implements PropertySerializer<BooleanProperty> {
      public static final MapCodec<BooleanProperty> CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.BOOL.fieldOf("defaultValue").forGetter(BooleanProperty::value),
              Codec.STRING.fieldOf("comment").forGetter(BooleanProperty::comment),
              Codec.BOOL.optionalFieldOf("defaultValue", null).forGetter(o -> o.value)
          ).apply(group, BooleanProperty::new));
      public static final MapCodec<BooleanProperty> FULL_CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.BOOL.fieldOf("default_value").forGetter(BooleanProperty::defaultValue),
              Codec.STRING.fieldOf("comment").forGetter(BooleanProperty::comment),
              Codec.BOOL.fieldOf("value").forGetter(o -> o.value == null ? o.defaultValue : o.value)
          ).apply(group, BooleanProperty::new));
      public static final StreamCodec<ByteBuf, BooleanProperty> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.BOOL, o -> o.value,
          ByteBufCodecs.STRING_UTF8, o -> o.comment,
          ByteBufCodecs.BOOL.apply(ByteBufCodecs::optional), o -> Optional.ofNullable(o.value),
          BooleanProperty::new
      );

      @Override
      public MapCodec<BooleanProperty> codec() {
        return CODEC;
      }

      @Override
      public MapCodec<BooleanProperty> fullCodec() {
        return FULL_CODEC;
      }

      @Override
      public StreamCodec<ByteBuf, BooleanProperty> streamCodec() {
        return STREAM_CODEC;
      }
    }

    public static class Type implements PropertyType<BooleanProperty> {
      @Override
      public String toString() {
        return "boolean_property";
      }
    }
  }

  record FloatProperty(float defaultValue, String comment, Float value) implements Property<Float> {
    public static final ResourceKey<PropertySerializer<?>> SERIALIZER = ResourceKey.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.rl("float_property"));
    public static final ResourceKey<PropertyType<?>> TYPE = ResourceKey.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.rl("float_property"));

    public FloatProperty(float defaultValue, String comment, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Float> value) {
      this(defaultValue, comment, value.orElse(null));
    }

    public FloatProperty(float value, String comment) {
      this(value, comment, (Float) null);
    }

    @Override
    public String getComment() {
      return comment();
    }

    @Override
    public Float getDefaultValue() {
      return defaultValue();
    }

    @Override
    public @Nullable Float getValue() {
      return value();
    }

    @Override
    public PropertySerializer<?> getSerializer() {
      return PropertySerializer.get(SERIALIZER);
    }

    @Override
    public PropertyType<?> getType() {
      return PropertyType.get(TYPE);
    }

    public static class Serializer implements PropertySerializer<FloatProperty> {
      public static final MapCodec<FloatProperty> CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.FLOAT.fieldOf("default_value").forGetter(FloatProperty::defaultValue),
              Codec.STRING.fieldOf("comment").forGetter(FloatProperty::comment),
              Codec.FLOAT.optionalFieldOf("value", null).forGetter(o -> o.value)
          ).apply(group, FloatProperty::new));
      public static final MapCodec<FloatProperty> FULL_CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.FLOAT.fieldOf("default_value").forGetter(FloatProperty::defaultValue),
              Codec.STRING.fieldOf("comment").forGetter(FloatProperty::comment),
              Codec.FLOAT.fieldOf("value").forGetter(o -> o.value == null ? o.defaultValue : o.value)
          ).apply(group, FloatProperty::new));
      public static final StreamCodec<ByteBuf, FloatProperty> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.FLOAT, o -> o.defaultValue,
          ByteBufCodecs.STRING_UTF8, o -> o.comment,
          ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional), o -> Optional.ofNullable(o.value),
          FloatProperty::new
      );

      @Override
      public MapCodec<FloatProperty> codec() {
        return CODEC;
      }

      @Override
      public MapCodec<FloatProperty> fullCodec() {
        return FULL_CODEC;
      }

      @Override
      public StreamCodec<ByteBuf, FloatProperty> streamCodec() {
        return STREAM_CODEC;
      }
    }

    public static class Type implements PropertyType<FloatProperty> {
      @Override
      public String toString() {
        return "float_property";
      }
    }
  }

  record DoubleProperty(double defaultValue, String comment, Double value) implements Property<Double> {
    public static final ResourceKey<PropertySerializer<?>> SERIALIZER = ResourceKey.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.rl("double_property"));
    public static final ResourceKey<PropertyType<?>> TYPE = ResourceKey.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.rl("double_property"));

    public DoubleProperty(double defaultValue, String comment, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Double> value) {
      this(defaultValue, comment, value.orElse(null));
    }

    public DoubleProperty(double defaultValue, String comment) {
      this(defaultValue, comment, (Double) null);
    }

    @Override
    public String getComment() {
      return comment();
    }

    @Override
    public Double getDefaultValue() {
      return defaultValue();
    }

    @Nullable
    @Override
    public Double getValue() {
      return value();
    }

    @Override
    public PropertySerializer<?> getSerializer() {
      return PropertySerializer.get(SERIALIZER);
    }

    @Override
    public PropertyType<?> getType() {
      return PropertyType.get(TYPE);
    }

    public static class Serializer implements PropertySerializer<DoubleProperty> {
      public static final MapCodec<DoubleProperty> CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.DOUBLE.fieldOf("default_value").forGetter(DoubleProperty::defaultValue),
              Codec.STRING.fieldOf("comment").forGetter(DoubleProperty::comment),
              Codec.DOUBLE.optionalFieldOf("value", null).forGetter(o -> o.value)
          ).apply(group, DoubleProperty::new));
      public static final MapCodec<DoubleProperty> FULL_CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.DOUBLE.fieldOf("default_value").forGetter(DoubleProperty::defaultValue),
              Codec.STRING.fieldOf("comment").forGetter(DoubleProperty::comment),
              Codec.DOUBLE.fieldOf("value").forGetter(o -> o.value == null ? o.defaultValue : o.value)
          ).apply(group, DoubleProperty::new));
      public static final StreamCodec<ByteBuf, DoubleProperty> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.DOUBLE, o -> o.defaultValue,
          ByteBufCodecs.STRING_UTF8, o -> o.comment,
          ByteBufCodecs.DOUBLE.apply(ByteBufCodecs::optional), o -> Optional.ofNullable(o.value),
          DoubleProperty::new
      );

      @Override
      public MapCodec<DoubleProperty> codec() {
        return CODEC;
      }

      @Override
      public MapCodec<DoubleProperty> fullCodec() {
        return FULL_CODEC;
      }

      @Override
      public StreamCodec<ByteBuf, DoubleProperty> streamCodec() {
        return STREAM_CODEC;
      }
    }

    public static class Type implements PropertyType<DoubleProperty> {
      @Override
      public String toString() {
        return "double_property";
      }
    }
  }

  record StringProperty(String defaultValue, String comment, String value) implements Property<String> {
    public static final ResourceKey<PropertySerializer<?>> SERIALIZER = ResourceKey.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.rl("string_property"));
    public static final ResourceKey<PropertyType<?>> TYPE = ResourceKey.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.rl("string_property"));

    public StringProperty(String defaultValue, String comment, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<String> value) {
      this(defaultValue, comment, value.orElse(null));
    }

    public StringProperty(String defaultValue, String comment) {
      this(defaultValue, comment, (String) null);
    }

    @Override
    public String getComment() {
      return comment();
    }

    @Override
    public String getDefaultValue() {
      return defaultValue();
    }

    @Override
    @Nullable
    public String getValue() {
      return value();
    }

    @Override
    public PropertySerializer<?> getSerializer() {
      return PropertySerializer.get(SERIALIZER);
    }

    @Override
    public PropertyType<?> getType() {
      return PropertyType.get(TYPE);
    }

    public static class Serializer implements PropertySerializer<StringProperty> {
      public static final MapCodec<StringProperty> CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.STRING.fieldOf("default_value").forGetter(StringProperty::defaultValue),
              Codec.STRING.fieldOf("comment").forGetter(StringProperty::comment),
              Codec.STRING.optionalFieldOf("value", null).forGetter(o -> o.value)
          ).apply(group, StringProperty::new));
      public static final MapCodec<StringProperty> FULL_CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.STRING.fieldOf("default_value").forGetter(StringProperty::defaultValue),
              Codec.STRING.fieldOf("comment").forGetter(StringProperty::comment),
              Codec.STRING.fieldOf("value").forGetter(o -> o.value == null ? o.defaultValue : o.value)
          ).apply(group, StringProperty::new));
      public static final StreamCodec<ByteBuf, StringProperty> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.STRING_UTF8, o -> o.defaultValue,
          ByteBufCodecs.STRING_UTF8, o -> o.comment,
          ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional), o -> Optional.ofNullable(o.value),
          StringProperty::new
      );

      @Override
      public MapCodec<StringProperty> codec() {
        return CODEC;
      }

      @Override
      public MapCodec<StringProperty> fullCodec() {
        return FULL_CODEC;
      }

      @Override
      public StreamCodec<ByteBuf, StringProperty> streamCodec() {
        return STREAM_CODEC;
      }
    }

    public static class Type implements PropertyType<StringProperty> {
      @Override
      public String toString() {
        return "string_property";
      }
    }
  }
}
