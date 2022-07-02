package mysticmods.roots.api.property;

import mysticmods.roots.api.spells.Spell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class SpellProperty<V> extends Property<V> implements IForgeRegistryEntry<SpellProperty<?>> {
  private ResourceLocation registryName;
  protected ResourceKey<Spell> spell;

  public SpellProperty(ResourceKey<Spell> spell, V defaultValue, Serializer<V> serializer, String comment) {
    super(defaultValue, serializer, comment);
    this.spell = spell;
  }

  public ResourceKey<Spell> getSpell() {
    return spell;
  }

  @Override
  public mysticmods.roots.api.property.SpellProperty<?> setRegistryName(ResourceLocation name) {
    this.registryName = name;
    return this;
  }

  @org.jetbrains.annotations.Nullable
  @Override
  public ResourceLocation getRegistryName() {
    return this.registryName;
  }

  @Override
  public Class<mysticmods.roots.api.property.SpellProperty<?>> getRegistryType() {
    return c(mysticmods.roots.api.property.SpellProperty.class);
  }
}
