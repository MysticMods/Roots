package mysticmods.roots.api.registry;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public interface IDescribedRegistryEntry {
  ResourceLocation getKey();

  String getOrCreateDescriptionId();

  default String getDescriptionId() {
    return getOrCreateDescriptionId();
  }

  // Stack sensitive?
  default MutableComponent getName() {
    return new TranslatableComponent(this.getDescriptionId());
  }

  default MutableComponent getName(Style style) {
    return getName().setStyle(style);
  }
}
