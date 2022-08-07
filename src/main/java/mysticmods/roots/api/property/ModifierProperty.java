package mysticmods.roots.api.property;

import mysticmods.roots.api.IDescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spells.Spell;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

public class ModifierProperty<V> extends Property<V> implements IForgeRegistryEntry<ModifierProperty<?>>, IDescribedRegistryEntry {
  private String descriptionId;
  private ResourceLocation registryName;
  protected Supplier<Spell> spell;

  public ModifierProperty(Supplier<Spell> spell, V defaultValue, Serializer<V> serializer, String comment) {
    super(defaultValue, serializer, comment);
    this.spell = spell;
  }

  public Spell getSpell() {
    return spell.get();
  }

  @Override
  public ModifierProperty<?> setRegistryName(ResourceLocation name) {
    this.registryName = name;
    return this;
  }

  @org.jetbrains.annotations.Nullable
  @Override
  public ResourceLocation getRegistryName() {
    return this.registryName;
  }

  @Override
  public Class<ModifierProperty<?>> getRegistryType() {
    return c(ModifierProperty.class);
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.MODIFIER_PROPERTY_REGISTRY.get().getKey(this);
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("modifier_property", getKey());
    }

    return this.descriptionId;
  }
}
