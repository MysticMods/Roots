package mysticmods.roots.api.ritual;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class RitualCondition extends DescribedRegistryEntry<RitualCondition> {
  protected String descriptionId;

  @Override
  protected String getDescriptor() {
    return "ritual_condition";
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.RITUAL_CONDITION_REGISTRY.get().getKey(this);
  }
}
