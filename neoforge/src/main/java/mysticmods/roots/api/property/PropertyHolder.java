package mysticmods.roots.api.property;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public record PropertyHolder<T extends Property<?>>(ResourceLocation id, T value) {
  // TODO:???

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof PropertyHolder<?> propertyHolder)) return false;
    return this.id.equals(propertyHolder.id);
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public String toString() {
    return this.id.toString();
  }
}
