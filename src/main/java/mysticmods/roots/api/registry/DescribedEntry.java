package mysticmods.roots.api.registry;

@Deprecated
public abstract class DescribedEntry implements IDescribedRegistryEntry {
  protected String descriptionId;

  protected abstract String getDescriptor();

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      // TODO:
      /*      this.descriptionId = Util.makeDescriptionId(getDescriptor(), getKey());*/
    }

    return this.descriptionId;
  }
}
