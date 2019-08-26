package epicsquid.roots.util.types;

import net.minecraft.util.ResourceLocation;

public abstract class RegistryItem {
  private ResourceLocation resourceLocation;

  public void setRegistryName(ResourceLocation name) {
    this.resourceLocation = name;
  }

  public ResourceLocation getRegistryName() {
    return this.resourceLocation;
  }
}
