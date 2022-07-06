package mysticmods.roots.api.spells;

import mysticmods.roots.api.KeyedRegistryEntry;
import net.minecraft.resources.ResourceKey;

public abstract class Spell extends KeyedRegistryEntry<Spell> {
  @Override
  public abstract ResourceKey<Spell> getKey();

  public void initialize() {
  }
}
