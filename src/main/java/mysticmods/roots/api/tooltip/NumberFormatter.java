package mysticmods.roots.api.tooltip;

import com.google.common.base.Suppliers;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class NumberFormatter<T extends Number> extends BasePropertyFormatter<T, NumberFormatter<T>> {
  private final PropertyHolder<? extends Property<T>> property;
  @Nullable
  private final PropertyHolder<? extends Property<T>> property2;

  private final List<Function<Number, Number>> modifyChain = new ArrayList<>();
  private Formatters.Formatter<? super Number> formatter;
  private Formatters.BiFormatter<? super Number, ? super Number> biFormatter;

  protected NumberFormatter(PropertyHolder<? extends Property<T>> property) {
    this.property = property;
    this.property2 = null;
  }

  protected NumberFormatter(PropertyHolder<? extends Property<T>> property1, PropertyHolder<? extends Property<T>> property2) {
    this.property = property1;
    this.property2 = property2;
  }

  public NumberFormatter<T> by(Formatters.Formatter<? super Number> formatter) {
    this.formatter = formatter;
    return this;
  }

  public NumberFormatter<T> by(Formatters.BiFormatter<? super Number, ? super Number> biFormatter) {
    this.biFormatter = biFormatter;
    return this;
  }

  private <N extends Number> Supplier<N> numberGetter(PropertyHolder<? extends Property<N>> property) {
    return Suppliers.memoize(() -> source.getValue(property));
  }

  public <N extends Number> NumberFormatter<T> multipliedByTier () {
    return this.modifyBy(new AddMultiplier(groupValueSupplier()));
  }

  public <N extends Number> NumberFormatter<T> multipliedByTier(PropertyHolder<? extends Property<N>> number) {
    return this.modifyBy(new AddMultiplier(groupValueSupplier(), numberGetter(number)));
  }

  public NumberFormatter<T> modifyBy(Function<Number, Number> modifier) {
    this.modifyChain.add(modifier);
    return this;
  }

  private <N extends Number> N value(PropertyHolder<? extends Property<N>> property) {
    return source.getValue(property);
  }

  private Supplier<Number> groupValueSupplier () {
    return () -> {
      if (NumberFormatter.this.groupValue != null) {
        return NumberFormatter.this.groupValue.get();
      } else {
        return -1;
      }
    };
  }

  private Number modifiedValue(Number value) {
    if (!modifyChain.isEmpty()) {
      for (Function<Number, Number> modifier : modifyChain) {
        value = modifier.apply(value);
      }
    }
    return value;
  }

  public List<MutableComponent> build() {
    if (this.property2 == null) {
      if (formatter != null) {
        return List.of(formatter.component(modifiedValue(value(this.property))));
      } else if (biFormatter != null) {
        Number value = value(this.property);
        Number multipliedValue = modifiedValue(value);
        return List.of(biFormatter.component(value, multipliedValue));
      } else {
        throw new IllegalStateException("No formatter specified for this PropertyFormatter");
      }
    } else {
      Number value1 = modifiedValue(value(this.property));
      Number value2 = modifiedValue(value(this.property2));
      if (formatter != null) {
        return List.of(formatter.component(value1), formatter.component(value2));
      } else if (biFormatter != null) {
        return List.of(biFormatter.component(value1, value2));
      } else {
        throw new IllegalStateException("No formatter specified for this PropertyFormatter");
      }
    }
  }

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

  public static class Multiplier implements Function<Number, Number> {
    private final List<Supplier<? extends Number>> sourceSuppliers;

    @SafeVarargs
    public Multiplier(Supplier<? extends Number>... sourceSuppliers) {
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
  }

  public static class Subtraction implements Function<Number, Number> {
    private final List<Supplier<? extends Number>> sourceSuppliers;

    @SafeVarargs
    public Subtraction(Supplier<? extends Number>... sourceSuppliers) {
      this.sourceSuppliers = Arrays.asList(sourceSuppliers);
    }

    @Override
    public Number apply(Number number) {
      double input = number.doubleValue();

      for (Supplier<? extends Number> source : sourceSuppliers) {
        input -= source.get().doubleValue();
      }

      return input;
    }
  }

  public static class Addition implements Function<Number, Number> {
    private final List<Supplier<? extends Number>> sourceSuppliers;

    @SafeVarargs
    public Addition(Supplier<? extends Number>... sourceSuppliers) {
      this.sourceSuppliers = Arrays.asList(sourceSuppliers);
    }

    @Override
    public Number apply(Number number) {
      double input = number.doubleValue();

      for (Supplier<? extends Number> source : sourceSuppliers) {
        input += source.get().doubleValue();
      }

      return input;
    }
  }
}
