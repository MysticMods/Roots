package mysticmods.roots.api.description;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public interface IExtendedDescribedInstance<T> extends IExtendedDescribed, IDescribedInstance<T> {
  String getOrCreateTooltipExtendedDescriptionId(T instance);

  String getOrCreateTooltipDescriptionId(T instance);

  Component[] getOrCreateDescriptionComponents(T instance);

  default String getTooltipDescriptionId(T instance) {
    return getOrCreateTooltipDescriptionId(instance);
  }

  default String getTooltipExtendedDescriptionId(T instance) {
    return getOrCreateTooltipExtendedDescriptionId(instance);
  }

  default List<Component> getTooltipExtendedDescription(T instance) {
    if (getExtendedTooltipLineCount() == 1) {
      return List.of(Component.translatable(getTooltipExtendedDescriptionId(instance), (Object[]) getOrCreateDescriptionComponents(instance)));
    } else {
      List<Component> result = new ArrayList<>();
      for (int i = 0; i < getExtendedTooltipLineCount(); i++) {
        result.add(Component.translatable(getTooltipExtendedDescriptionId(instance) + "_" + i, (Object[]) getOrCreateDescriptionComponents(instance)));
        result.add(Component.empty());
      }
      if (!result.isEmpty()) {
        result.removeLast();
      }
      return result;
    }
  }

  default MutableComponent getTooltipDescription(T instance) {
    return Component.translatable(this.getTooltipDescriptionId(instance));
  }
}
