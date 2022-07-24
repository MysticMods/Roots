package mysticmods.roots.api.property;

import mysticmods.roots.api.IDescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RitualProperty<V> extends Property<V> implements IForgeRegistryEntry<RitualProperty<?>>, IDescribedRegistryEntry {
  protected String descriptionId;
  private ResourceLocation registryName;
  protected ResourceKey<Ritual> ritual;

  public RitualProperty(ResourceKey<Ritual> ritual, V defaultValue, Serializer<V> serializer, String comment) {
    super(defaultValue, serializer, comment);
    this.ritual = ritual;
  }

  public ResourceKey<Ritual> getRitual() {
    return ritual;
  }

  @Override
  public mysticmods.roots.api.property.RitualProperty<?> setRegistryName(ResourceLocation name) {
    this.registryName = name;
    return this;
  }

  @org.jetbrains.annotations.Nullable
  @Override
  public ResourceLocation getRegistryName() {
    return this.registryName;
  }

  @Override
  public Class<mysticmods.roots.api.property.RitualProperty<?>> getRegistryType() {
    return c(mysticmods.roots.api.property.RitualProperty.class);
  }

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
