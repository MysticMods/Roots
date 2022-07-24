package mysticmods.roots.api;

import net.minecraft.Util;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class DescribedRegistryEntry<T extends IForgeRegistryEntry<T>> extends ForgeRegistryEntry<T> implements IDescribedRegistryEntry {
  protected String descriptionId;

  protected abstract String getDescriptor ();

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId(getDescriptor(), getKey());
    }

    return this.descriptionId;
  }
}
