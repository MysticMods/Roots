package mysticmods.roots.api.modifier;

import net.minecraft.resources.ResourceKey;

import javax.annotation.Nullable;

public interface IModifier<T> {
  @Nullable
  ResourceKey<Modifier<T>> getParent();

  ResourceKey<T> getApplicable();
}
