package mysticmods.roots.api.modifier;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Set;

public interface IModifier<V, T extends IModifier<V, T>> extends Comparable<IModifier<V, T>> {
  ResourceKey<T> getSelf();

  @Nullable
  ResourceKey<T> getParent();

  @Nullable
  Holder<T> getParentHolder ();

  ResourceKey<V> getApplicable();

  Holder<V> getApplicableHolder ();

  Set<ResourceKey<T>> getConflicts();

  @NonNull
  ItemStack getIcon();

  void setDepth (int depth);

  default int depth () {
    return 0;
  }

  @Override
  default int compareTo(@NotNull IModifier<V, T> o) {
    return getSelf().compareTo(o.getSelf());
  }
}
