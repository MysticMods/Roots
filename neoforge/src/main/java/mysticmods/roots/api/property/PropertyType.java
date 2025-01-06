package mysticmods.roots.api.property;

import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

public interface PropertyType<T extends Property<?>> {

  @Nullable
  static PropertyType<?> get(ResourceKey<PropertyType<?>> id) {
    return RootsRegistries.PROPERTY_TYPES.get(id);
  }

  default T cast(Property<?> property) {
    if (property.getType() == this) {
      return (T) property;
    }
    throw new IllegalArgumentException("Incompatible property types '" + property.getType() + "' and '" + this + "'");
  }
}
