package mysticmods.roots.api.property;

import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

public class RitualProperty<V> extends Property<V> {
  protected String descriptionId;
  protected Holder<Ritual> ritual;

  private final Holder.Reference<RitualProperty<?>> builtinRegistryHolder = RootsRegistries.RITUAL_PROPERTIES.createIntrusiveHolder(this);

  public RitualProperty(Holder<Ritual> ritual, V defaultValue, Serializer<V> serializer, String comment) {
    super(defaultValue, serializer, comment);
    this.ritual = ritual;
  }

  public Holder<Ritual> getRitual() {
    return ritual;
  }

  public Holder.Reference<RitualProperty<?>> getBuiltinRegistryHolder() {
    return builtinRegistryHolder;
  }

  public ResourceLocation getKey() {
    return getBuiltinRegistryHolder().getKey().location();
  }

  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("ritual_property", getKey());
    }

    return this.descriptionId;
  }
}
