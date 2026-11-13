package mysticmods.roots.api.description;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public interface IExtendedDescribed extends ITooltipDescribed {
  String getOrCreateTooltipExtendedDescriptionId();

  default String getTooltipExtendedDescriptionId() {
    return getOrCreateTooltipExtendedDescriptionId();
  }

  default List<Component> getTooltipExtendedDescription() {
    List<Component> result = new ArrayList<>();
    if (getExtendedTooltipLineCount() == 1) {
      result.add(Component.translatable(getOrCreateTooltipExtendedDescriptionId(), (Object[]) getOrCreateDescriptionComponents()));
    } else {
      for (int i = 0; i < getExtendedTooltipLineCount(); i++) {
        result.add(Component.translatable(getOrCreateTooltipExtendedDescriptionId() + "_" + i, (Object[]) getOrCreateDescriptionComponents()));
        result.add(Component.empty());
      }
      result.removeLast();
    }
    return result;
  }

  Component[] getOrCreateDescriptionComponents();

  int getExtendedTooltipLineCount();
}
