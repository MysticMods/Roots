package mysticmods.roots.api.tooltip;

import com.google.common.base.Suppliers;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BooleanFormatter<T extends Boolean> extends BasePropertyFormatter<T, BooleanFormatter<T>> {
  private final PropertyHolder<? extends Property<T>> property;
  @Nullable
  private final PropertyHolder<? extends Property<T>> property2;

  private Formatters.Formatter<? super Boolean> formatter;
  private Formatters.BiFormatter<? super Boolean, ? super Boolean> biFormatter;

  protected BooleanFormatter(PropertyHolder<? extends Property<T>> property) {
    this.property = property;
    this.property2 = null;
  }

  protected BooleanFormatter(PropertyHolder<? extends Property<T>> property1, PropertyHolder<? extends Property<T>> property2) {
    this.property = property1;
    this.property2 = property2;
  }

  public BooleanFormatter<T> by(Formatters.Formatter<? super Boolean> formatter) {
    this.formatter = formatter;
    return this;
  }

  public BooleanFormatter<T> by(Formatters.BiFormatter<? super Boolean, ? super Boolean> biFormatter) {
    this.biFormatter = biFormatter;
    return this;
  }

  private <N extends Boolean> N value(PropertyHolder<? extends Property<N>> property) {
    return source.getValue(property);
  }

  @Override
  public List<MutableComponent> build() {
    if (this.property2 == null) {
      if (formatter != null) {
        return List.of(formatter.component(value(this.property)));
      } else if (biFormatter != null) {
        Boolean value = value(this.property);
        return List.of(biFormatter.component(value, value));
      } else {
        throw new IllegalStateException("No formatter specified for this PropertyFormatter " + this.property);
      }
    } else {
      Boolean value1 = value(this.property);
      Boolean value2 = value(this.property2);
      if (formatter != null) {
        return List.of(formatter.component(value1), formatter.component(value2));
      } else if (biFormatter != null) {
        return List.of(biFormatter.component(value1, value2));
      } else {
        throw new IllegalStateException("No formatter specified for this PropertyFormatter");
      }
    }
  }
}
