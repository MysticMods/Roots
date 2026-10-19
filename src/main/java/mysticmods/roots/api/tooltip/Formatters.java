package mysticmods.roots.api.tooltip;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class Formatters {
  public interface Formatter<T> {
    ResourceLocation id();

    default String key() {
      return Util.makeDescriptionId("formatter", id());
    }

    default String translation() {
      return "%s";
    }

    default MutableComponent component(T value) {
      return Component.translatable(key(), value);
    }

    default MutableComponent thenApply(Formatter<MutableComponent> formatter, T value) {
      return formatter.component(this.component(value));
    }
  }

  public interface BiFormatter<A, B> {
    ResourceLocation id();

    default String key() {
      return Util.makeDescriptionId("formatter", id());
    }

    default String translation() {
      return "%s";
    }

    default MutableComponent component(A value1, B value2) {
      return Component.translatable(key(), value1, value2);
    }

    default MutableComponent thenApply(Formatter<MutableComponent> formatter, A value1, B value2) {
      return formatter.component(this.component(value1, value2));
    }
  }

  public static final Formatter<MutableComponent> BRACKETS = new Formatter<>() {
    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("brackets");
    }

    @Override
    public String translation() {
      return "[%s]";
    }
  };

  public static final Formatter<MutableComponent> MULTIPLY = new Formatter<>() {
    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("multiply");
    }

    @Override
    public String translation() {
      return "×%s";
    }
  };

  public static final Formatter<MutableComponent> DEGREES = new Formatter<>() {

    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("degrees");
    }

    @Override
    public String translation() {
      return "%s°";
    }
  };

  public static final Formatter<Number> TICKS = new Formatter<>() {
    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("ticks");
    }

    @Override
    public String translation() {
      return "%s seconds [%s ticks]";
    }

    @Override
    public MutableComponent component(Number value) {
      return Component.translatable(key(), String.format("%.1f", value.intValue() / 20.0), value.intValue());
    }
  };

  public static final Formatter<Number> SINGLE_DECIMAL = new Formatter<>() {
    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("single_decimal");
    }

    @Override
    public MutableComponent component(Number value) {
      return Component.translatable(key(), String.format("%.1f", value.doubleValue()));
    }
  };

  public static final Formatter<Number> NUMBER = new Formatter<>() {
    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("number");
    }

    @Override
    public MutableComponent component(Number value) {
      return Component.translatable(key(), value.intValue());
    }
  };

  public static final Formatter<Number> PERCENT = new Formatter<>() {
    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("percent");
    }

    @Override
    public String translation() {
      return "%s%%";
    }

    @Override
    public MutableComponent component(Number value) {
      return Component.translatable(key(), String.format("%.1f", value.doubleValue()));
    }
  };

  public static final Formatter<Number> HEARTS = new Formatter<>() {
    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("hearts");
    }

    @Override
    public String translation() {
      return "%s hearts";
    }

    @Override
    public MutableComponent component(Number value) {
      return Component.translatable(key(), value.doubleValue() / 2.0);
    }
  };

  public static final Formatter<Number> LEVEL = new Formatter<>() {
    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("level");
    }

    @Override
    public MutableComponent component(Number value) {
      return Component.translatable(key(), value.intValue() + 1);
    }
  };

  public static final BiFormatter<Number, Number> BLOCK_RANGE = new BiFormatter<>() {
    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("block_range");
    }

    @Override
    public String translation() {
      return "±%s/±%s";
    }

    @Override
    public MutableComponent component(Number value1, Number value2) {
      return Component.translatable(key(), value1.intValue(), value2.intValue());
    }
  };

  public static final BiFormatter<Number, Number> SPRINT_SPEED = new BiFormatter<>() {
    @Override
    public ResourceLocation id() {
      return RootsAPI.rl("sprint_speed");
    }

    @Override
    public String translation() {
      return "×%2$s sprint speed [%1$s]";
    }
  };

  public static <T extends Number> NumberFormatter<T> formatterFor(PropertyHolder<? extends Property<T>> property) {
    return new NumberFormatter<>(property);
  }

  public static <T extends Number> NumberFormatter<T> formatterFor(PropertyHolder<? extends Property<T>> property1, PropertyHolder<? extends Property<T>> property2) {
    return new NumberFormatter<>(property1, property2);
  }
}
