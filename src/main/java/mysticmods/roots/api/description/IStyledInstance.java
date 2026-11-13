package mysticmods.roots.api.description;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import javax.annotation.Nullable;

// TODO: Non-instanced overrides default, @Deprecated, defer to 'getRawXYZ'
public interface IStyledInstance<T> extends IStyled, IDescribedInstance<T> {
  @Nullable
  TextColor getTextColor(T instance);

  Style getOrCreateStyle(T instance);

  default boolean isBold(T instance) {
    return false;
  }

  default MutableComponent getStyledName(T instance) {
    return getStyledName(instance, isBold(instance));
  }

  default MutableComponent getStyledName(T instance, boolean bold) {
    return getName(instance, getStyle(instance).withBold(bold));
  }

  default Style getStyle(T instance) {
    return getOrCreateStyle(instance);
  }

  // TODO: This shouldn't be here
  default MutableComponent getName(T instance, Style style) {
    return getName(instance).setStyle(style);
  }
}
