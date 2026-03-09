package mysticmods.roots.api.modifier;

import net.minecraft.resources.ResourceKey;

import javax.annotation.Nullable;
import java.util.Set;

public interface IModifier<V, T extends IModifier<V, T>> {
  @Nullable
  ResourceKey<T> getParent();

  ResourceKey<V> getApplicable();

  Set<ResourceKey<T>> getConflicts ();
}
