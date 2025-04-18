package mysticmods.roots.api.registry;

import net.minecraft.core.Holder;

public interface IDataMapInitialize<T extends IDataMapInitialize<T>> {
  void init (Holder<T> holder);

  default void performInit(Holder<?> holder) {
    //noinspection unchecked
    this.init((Holder<T>) holder);
  }
}
