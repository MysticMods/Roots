package mysticmods.roots.api;

import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class KeyedRegistryEntry<T extends IForgeRegistryEntry<T>> extends ForgeRegistryEntry<T> {
  public abstract ResourceKey<T> getKey();
}
