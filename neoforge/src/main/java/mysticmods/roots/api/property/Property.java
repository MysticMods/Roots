package mysticmods.roots.api.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

public interface Property<T> {
  String getComment();

  T getDefaultValue();

  PropertySerializer<?> getSerializer();

  PropertyType<?> getType();

  static IntegerProperty ofInt (int value, String comment) {
    return new IntegerProperty(value, comment);
  }

  static BooleanProperty ofBool (boolean value, String comment) {
    return new BooleanProperty(value, comment);
  }

  static FloatProperty ofFloat (float value, String comment) {
    return new FloatProperty(value, comment);
  }

  static DoubleProperty ofDouble (double value, String comment) {
    return new DoubleProperty(value, comment);
  }

  static StringProperty ofString (String value, String comment) {
    return new StringProperty(value, comment);
  }

  record IntegerProperty(int value, String comment) implements Property<Integer> {
    public static final ResourceKey<PropertySerializer<?>> SERIALIZER = ResourceKey.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.rl("integer_property"));
    public static final ResourceKey<PropertyType<?>> TYPE = ResourceKey.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.rl("integer_property"));

    @Override
    public String getComment() {
      return comment();
    }

    public Integer getDefaultValue() {
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
      public static MapCodec<IntegerProperty> CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.INT.fieldOf("value").forGetter(IntegerProperty::value),
              Codec.STRING.fieldOf("comment").forGetter(IntegerProperty::comment)
          ).apply(group, IntegerProperty::new));
      public static StreamCodec<ByteBuf, IntegerProperty> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.VAR_INT, o -> o.value,
          ByteBufCodecs.STRING_UTF8, o -> o.comment,
          IntegerProperty::new
      );

      @Override
      public MapCodec<IntegerProperty> codec() {
        return CODEC;
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

  record BooleanProperty (boolean value, String comment) implements Property<Boolean> {
    public static final ResourceKey<PropertySerializer<?>> SERIALIZER = ResourceKey.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.rl("boolean_property"));
    public static final ResourceKey<PropertyType<?>> TYPE = ResourceKey.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.rl("boolean_property"));

    @Override
    public String getComment() {
      return comment();
    }

    public Boolean getDefaultValue() {
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
      public static MapCodec<BooleanProperty> CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.BOOL.fieldOf("value").forGetter(BooleanProperty::value),
              Codec.STRING.fieldOf("comment").forGetter(BooleanProperty::comment)
          ).apply(group, BooleanProperty::new));
      public static StreamCodec<ByteBuf, BooleanProperty> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.BOOL, o -> o.value,
          ByteBufCodecs.STRING_UTF8, o -> o.comment,
          BooleanProperty::new
      );

      @Override
      public MapCodec<BooleanProperty> codec() {
        return CODEC;
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

  record FloatProperty (float value, String comment) implements Property<Float> {
    public static final ResourceKey<PropertySerializer<?>> SERIALIZER = ResourceKey.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.rl("float_property"));
    public static final ResourceKey<PropertyType<?>> TYPE = ResourceKey.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.rl("float_property"));

    @Override
    public String getComment() {
      return comment();
    }

    public Float getDefaultValue() {
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
      public static MapCodec<FloatProperty> CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.FLOAT.fieldOf("value").forGetter(FloatProperty::value),
              Codec.STRING.fieldOf("comment").forGetter(FloatProperty::comment)
          ).apply(group, FloatProperty::new));
      public static StreamCodec<ByteBuf, FloatProperty> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.FLOAT, o -> o.value,
          ByteBufCodecs.STRING_UTF8, o -> o.comment,
          FloatProperty::new
      );

      @Override
      public MapCodec<FloatProperty> codec() {
        return CODEC;
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

  record DoubleProperty (double value, String comment) implements Property<Double> {
    public static final ResourceKey<PropertySerializer<?>> SERIALIZER = ResourceKey.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.rl("double_property"));
    public static final ResourceKey<PropertyType<?>> TYPE = ResourceKey.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.rl("double_property"));

    @Override
    public String getComment() {
      return comment();
    }

    public Double getDefaultValue() {
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
      public static MapCodec<DoubleProperty> CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.DOUBLE.fieldOf("value").forGetter(DoubleProperty::value),
              Codec.STRING.fieldOf("comment").forGetter(DoubleProperty::comment)
          ).apply(group, DoubleProperty::new));
      public static StreamCodec<ByteBuf, DoubleProperty> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.DOUBLE, o -> o.value,
          ByteBufCodecs.STRING_UTF8, o -> o.comment,
          DoubleProperty::new
      );

      @Override
      public MapCodec<DoubleProperty> codec() {
        return CODEC;
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

  record StringProperty (String value, String comment) implements Property<String> {
    public static final ResourceKey<PropertySerializer<?>> SERIALIZER = ResourceKey.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.rl("string_property"));
    public static final ResourceKey<PropertyType<?>> TYPE = ResourceKey.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.rl("string_property"));

    @Override
    public String getComment() {
      return comment();
    }

    public String getDefaultValue() {
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
      public static MapCodec<StringProperty> CODEC = RecordCodecBuilder.mapCodec(
          group -> group.group(
              Codec.STRING.fieldOf("value").forGetter(StringProperty::value),
              Codec.STRING.fieldOf("comment").forGetter(StringProperty::comment)
          ).apply(group, StringProperty::new));
      public static StreamCodec<ByteBuf, StringProperty> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.STRING_UTF8, o -> o.value,
          ByteBufCodecs.STRING_UTF8, o -> o.comment,
          StringProperty::new
      );

      @Override
      public MapCodec<StringProperty> codec() {
        return CODEC;
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
