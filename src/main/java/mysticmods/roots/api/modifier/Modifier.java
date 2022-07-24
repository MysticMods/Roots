package mysticmods.roots.api.modifier;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;

public class Modifier extends DescribedRegistryEntry<Modifier> {
  public void initialize() {
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.MODIFIER_REGISTRY.get().getKey(this);
  }

  @Override
  protected String getDescriptor() {
    return "modifier";
  }
}
