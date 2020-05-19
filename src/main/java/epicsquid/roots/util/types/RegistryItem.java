package epicsquid.roots.util.types;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class RegistryItem {
  private ResourceLocation resourceLocation;

  public void setRegistryName(ResourceLocation name) {
    this.resourceLocation = name;
  }

  @Nonnull
  public ResourceLocation getRegistryName() {
    return this.resourceLocation;
  }
}
