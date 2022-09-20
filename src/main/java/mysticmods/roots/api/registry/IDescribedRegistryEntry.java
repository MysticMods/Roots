package mysticmods.roots.api.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public interface IDescribedRegistryEntry extends IKeyedRegistryEntry {

  String getOrCreateDescriptionId();

  default String getDescriptionId() {
    return getOrCreateDescriptionId();
  }

  // Stack sensitive?
  default MutableComponent getName() {
    return Component.translatable(this.getDescriptionId());
  }

  default MutableComponent getName(Style style) {
    return getName().setStyle(style);
  }
}
