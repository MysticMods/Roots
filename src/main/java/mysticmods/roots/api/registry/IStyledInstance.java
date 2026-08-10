package mysticmods.roots.api.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nullable;

// TODO: Non-instanced overrides default, @Deprecated, defer to 'getRawXYZ'
public interface IStyledInstance<T> extends IStyled {
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

  String getOrCreateDescriptionId(T instance);

  default String getDescriptionId(T instance) {
    return getOrCreateDescriptionId(instance);
  }

  default MutableComponent getName(T instance) {
    return Component.translatable(this.getDescriptionId(instance));
  }

  default MutableComponent getName(T instance, Style style) {
    return getName(instance).setStyle(style);
  }
}
