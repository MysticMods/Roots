package mysticmods.roots.api.property;

import mysticmods.roots.api.registry.IDescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RitualProperty<V> extends Property<V> implements IDescribedRegistryEntry {
  protected String descriptionId;
  protected Supplier<Ritual> ritual;

  public RitualProperty(Supplier<Ritual> ritual, V defaultValue, Serializer<V> serializer, String comment) {
    super(defaultValue, serializer, comment);
    this.ritual = ritual;
  }

  public Ritual getRitual() {
    return ritual.get();
  }

/*  @Override
  public Class<mysticmods.roots.api.property.RitualProperty<?>> getRegistryType() {
    return c(mysticmods.roots.api.property.RitualProperty.class);
  }*/

  @Override
  public ResourceLocation getKey() {
    return Registries.RITUAL_PROPERTY_REGISTRY.get().getKey(this);
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("ritual_property", getKey());
    }

    return this.descriptionId;
  }
}
