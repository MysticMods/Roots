package mysticmods.roots.api.property;

import mysticmods.roots.api.modifier.Modifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModifierProperty<V> extends Property<V> implements IForgeRegistryEntry<ModifierProperty<?>> {
  private ResourceLocation registryName;
  protected ResourceKey<Modifier> modifier;

  public ModifierProperty(ResourceKey<Modifier> modifier, V defaultValue, Serializer<V> serializer, String comment) {
    super(defaultValue, serializer, comment);
    this.modifier = modifier;
  }

  public ResourceKey<Modifier> getModifier() {
    return modifier;
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
}
