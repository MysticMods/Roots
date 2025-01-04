package mysticmods.roots.api.registry;

import net.minecraft.core.Holder;

public interface IHasHolder<T> {
  Holder.Reference<T> builtInRegistryHolder();
}
