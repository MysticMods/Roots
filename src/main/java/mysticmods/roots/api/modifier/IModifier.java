package mysticmods.roots.api.modifier;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Set;

public interface IModifier<V, T extends IModifier<V, T>> extends Comparable<IModifier<V, T>> {
  ResourceKey<T> getSelf ();

  @Nullable
  ResourceKey<T> getParent();

  ResourceKey<V> getApplicable();

  Set<ResourceKey<T>> getConflicts();

  @NonNull
  ItemStack getIcon();

  @Override
  default int compareTo(@NotNull IModifier<V, T> o) {
    return getSelf().compareTo(o.getSelf());
  }
}
