package mysticmods.roots.api.description;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface ITooltipDescribed extends IDescribed {
  String getOrCreateTooltipDescriptionId();

  default String getTooltipDescriptionId() {
    return getOrCreateTooltipDescriptionId();
  }

  default MutableComponent getTooltipDescription() {
    return Component.translatable(this.getTooltipDescriptionId());
  }
}
