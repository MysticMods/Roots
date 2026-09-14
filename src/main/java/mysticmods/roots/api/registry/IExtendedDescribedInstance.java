package mysticmods.roots.api.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface IExtendedDescribedInstance<T> extends IExtendedDescribed {
  String getOrCreateTooltipExtendedDescriptionId(T instance);

  String getOrCreateTooltipDescriptionId(T instance);

  Component[] getOrCreateDescriptionComponents(T instance);

  default String getTooltipDescriptionId(T instance) {
    return getOrCreateTooltipDescriptionId(instance);
  }

  default String getTooltipExtendedDescriptionId(T instance) {
    return getOrCreateTooltipExtendedDescriptionId(instance);
  }

  default MutableComponent getTooltipExtendedDescription(T instance) {
    return Component.translatable(getTooltipExtendedDescriptionId(instance), (Object[]) getOrCreateDescriptionComponents(instance));
  }

  default MutableComponent getTooltipDescription(T instance) {
    return Component.translatable(this.getTooltipDescriptionId(instance));
  }
}
