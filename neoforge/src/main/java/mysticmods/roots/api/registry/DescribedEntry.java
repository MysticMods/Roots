package mysticmods.roots.api.registry;

import net.minecraft.Util;

@Deprecated
public abstract class DescribedEntry implements IDescribedRegistryEntry {
  protected String descriptionId;

  protected abstract String getDescriptor();

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId(getDescriptor(), getKey());
    }

    return this.descriptionId;
  }
}
