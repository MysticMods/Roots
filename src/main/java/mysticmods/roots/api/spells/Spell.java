package mysticmods.roots.api.spells;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;

public abstract class Spell extends DescribedRegistryEntry<Spell> {
  public void initialize() {
  }

  @Override
  protected String getDescriptor() {
    return "spell";
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.SPELL_REGISTRY.get().getKey(this);
  }
}
