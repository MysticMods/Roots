package mysticmods.roots.api.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface IExtendedDescribedInstance<T> extends IExtendedDescribed {
  default String getOrCreateTooltipExtendedDescriptionId(T instance) {
    return getOrCreateTooltipExtendedDescriptionId();
  }

  default String getTooltipExtendedDescriptionId(T instance) {
    return getOrCreateTooltipExtendedDescriptionId(instance);
  }

  default MutableComponent getTooltipExtendedDescription(T instance) {
    return Component.translatable(getOrCreateTooltipExtendedDescriptionId(instance), (Object[]) getOrCreateDescriptionComponents(instance));
  }

  default Component[] getOrCreateDescriptionComponents(T instance) {
    return getOrCreateDescriptionComponents();
  }

  default String getOrCreateTooltipDescriptionId(T instance) {
    return getOrCreateTooltipDescriptionId();
  }

  default String getTooltipDescriptionId(T instance) {
    return getOrCreateTooltipDescriptionId(instance);
  }

  default MutableComponent getTooltipDescription(T instance) {
    return Component.translatable(this.getTooltipDescriptionId(instance));
  }
}
