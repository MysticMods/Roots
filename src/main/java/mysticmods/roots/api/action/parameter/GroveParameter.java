package mysticmods.roots.api.action.parameter;

import net.minecraft.resources.ResourceLocation;

public class GroveParameter<T> {
  private final ResourceLocation name;

  public GroveParameter(ResourceLocation name) {
    this.name = name;
  }

  public ResourceLocation getName () {
    return name;
  }

  @Override
  public String toString() {
    return "<grove parameter " + this.name + ">";
  }
}
