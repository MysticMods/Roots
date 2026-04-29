package mysticmods.roots.api.modifier;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.Set;

public interface IModifier<V, T extends IModifier<V, T>> {
  @Nullable
  ResourceKey<T> getParent();

  ResourceKey<V> getApplicable();

  Set<ResourceKey<T>> getConflicts();

  Item getIcon();

  boolean isRestricted();
}
