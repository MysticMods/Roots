package mysticmods.roots.api.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public interface IDescribedRegistryEntry {
  ResourceLocation getKey();

  String getOrCreateDescriptionId();

  default String getDescriptionId() {
    return getOrCreateDescriptionId();
  }

  // Stack sensitive?
  default Component getName() {
    return new TranslatableComponent(this.getDescriptionId());
  }
}
