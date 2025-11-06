package mysticmods.roots.api.modifier;

import net.minecraft.resources.ResourceKey;

import javax.annotation.Nullable;

public interface IModifier<V, T extends IModifier<V, T>> {
  @Nullable
  ResourceKey<T> getParent();

  ResourceKey<V> getApplicable();
}
