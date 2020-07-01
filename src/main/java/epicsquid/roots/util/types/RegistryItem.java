package epicsquid.roots.util.types;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class RegistryItem implements IRegistryItem {
  private ResourceLocation resourceLocation;

  @Override
  public void setRegistryName(ResourceLocation name) {
    this.resourceLocation = name;
  }

  @Override
  @Nonnull
  public ResourceLocation getRegistryName() {
    return this.resourceLocation;
  }
}
