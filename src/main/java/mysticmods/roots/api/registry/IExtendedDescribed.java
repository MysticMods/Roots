package mysticmods.roots.api.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface IExtendedDescribed extends ITooltipDescribed {
  String getOrCreateTooltipExtendedDescriptionId ();

  default String getTooltipExtendedDescriptionId () {
    return getOrCreateTooltipExtendedDescriptionId();
  }

  default MutableComponent getTooltipExtendedDescription () {
    return Component.translatable(getOrCreateTooltipExtendedDescriptionId(), (Object[]) getOrCreateDescriptionComponents());
  }

  Component[] getOrCreateDescriptionComponents();
}
