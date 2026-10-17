package mysticmods.roots.api.property;

import com.google.common.base.Suppliers;
import mysticmods.roots.api.IPropertyDataMapped;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModModifiers;
import mysticmods.roots.init.ModSpells;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

  public static <T extends Number> TooltipBuilder<T> createTooltipFor(PropertyHolder<? extends Property<T>> property) {
    return new TooltipBuilder<>(property);
  }

  public static class TooltipBuilder<T extends Number> {
    private IPropertyDataMapped source;
    private final PropertyHolder<? extends Property<T>> property;

    private final List<Function<Number, Number>> multiplierChain = new ArrayList<>();
    private Formatter<? super Number> formatter;
    private BiFormatter<? super Number, ? super Number> biFormatter;

    protected TooltipBuilder(PropertyHolder<? extends Property<T>> property) {
      this.property = property;
    }

    public TooltipBuilder<T> formattedBy(Formatter<? super Number> formatter) {
      this.formatter = formatter;
      return this;
    }

    public TooltipBuilder<T> formattedBy (BiFormatter<? super Number, ? super Number> biFormatter) {
      this.biFormatter = biFormatter;
      return this;
    }

    private <N extends Number> Supplier<N> numberGetter(PropertyHolder<? extends Property<N>> property) {
      return Suppliers.memoize(() -> source.getValue(property));
    }

    public <N extends Number> TooltipBuilder<T> multipliedByTier(PropertyHolder<? extends Property<N>> number, Holder<? extends Modifier<?, ?>> modifier) {
      return this.multiplyBy(new AddMultiplier(() -> modifier.value().getGroupValue(), numberGetter(number)));
    }

    public TooltipBuilder<T> multiplyBy(Multiplier multiplier) {
      this.multiplierChain.add(multiplier);
      return this;
    }

    public TooltipBuilder<T> spell (ResourceKey<Spell> spell) {
      this.source = RootsRegistries.SPELLS.get(spell);
      if (this.source == null) {
        throw new NullPointerException(spell + " could not be found!");
      }
      return this;
    }

    public TooltipBuilder<T> spell (Holder<Spell> spell) {
      this.source = spell.value();
      return this;
    }

    public TooltipBuilder<T> spell (Spell spell) {
      this.source = spell;
      return this;
    }

    public TooltipBuilder<T> ritual (ResourceKey<Ritual> ritual) {
      this.source = RootsRegistries.RITUALS.get(ritual);
      if (this.source == null) {
        throw new NullPointerException(ritual + " could not be found!");
      }
      return this;
    }

    public TooltipBuilder<T> ritual (Holder<Ritual> ritual) {
      this.source = ritual.value();
      return this;
    }

    public TooltipBuilder<T> ritual (Ritual ritual) {
      this.source = ritual;
      return this;
    }

    private Number value () {
      return source.getValue(property);
    }

    private Number multipliedValue () {
      return multipliedValue(value());
    }

    private Number multipliedValue (Number value) {
      if (!multiplierChain.isEmpty()) {
        for (Function<Number, Number> multiplier : multiplierChain) {
          value = multiplier.apply(value);
        }
      }
      return value;
    }

    public TooltipBuilder<T> build (Consumer<MutableComponent> components) {
      if (formatter != null) {
        components.accept(formatter.component(multipliedValue()));
      } else if (biFormatter != null) {
        Number value = value();
        Number multipliedValue = multipliedValue(value);
        components.accept(biFormatter.component(value, multipliedValue));
      } else {
        throw new IllegalStateException("TooltipBuilder requires a formatter!");
      }

      return this;
    }

    public TooltipBuilder<T> reset () {
      this.biFormatter = null;
      this.formatter = null;
      this.multiplierChain.clear();
      return this;
    }
  }
/*
  static {
    createTooltipFor(ModSpells.SKY_SOARER_DURATION)
        .spell(ModSpells.SKY_SOARER)
        .formattedBy(Formatters.TICKS)
        .multipliedByTier(ModSpells.SKY_SOARER_DURATION_INCREASE, ModModifiers.SKY_SOARER_SPEEDY_1);
  }*/

  public static class Multiplier implements Function<Number, Number> {
    private final List<Supplier<? extends Number>> sourceSuppliers;

    @SafeVarargs
    public Multiplier(Supplier<? extends Number> ... sourceSuppliers) {
      this.sourceSuppliers = Arrays.asList(sourceSuppliers);
    }

    @Override
    public Number apply(Number input) {
      if (sourceSuppliers.size() == 1) {
        return input.doubleValue() * this.sourceSuppliers.getFirst().get().doubleValue();
      } else {
        double multiplier = 0;
        for (Supplier<? extends Number> source : this.sourceSuppliers) {
          double thisValue = source.get().doubleValue();
          if (multiplier == 0) {
            multiplier = thisValue;
          } else {
            multiplier *= thisValue;
          }
        }
        return input.doubleValue() * multiplier;
      }
    }
  };

  public static class AddMultiplier extends Multiplier {
    @SafeVarargs
    public AddMultiplier(Supplier<? extends Number>... sourceSuppliers) {
      super(sourceSuppliers);
    }

    @Override
    public Number apply(Number input) {
      return input.doubleValue() + super.apply(input).doubleValue();
    }
  }
}
