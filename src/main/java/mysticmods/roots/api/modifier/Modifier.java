package mysticmods.roots.api.modifier;

import mysticmods.roots.api.KeyedRegistryEntry;
import net.minecraft.resources.ResourceKey;

public abstract class Modifier extends KeyedRegistryEntry<Modifier> {
  @Override
  public abstract ResourceKey<Modifier> getKey();

  public void initialize() {
  }
}
