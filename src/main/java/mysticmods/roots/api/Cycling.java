package mysticmods.roots.api;

import mysticmods.roots.api.registry.IStyled;
import net.minecraft.util.StringRepresentable;

public interface Cycling<T extends Cycling<T>> extends IStyled, StringRepresentable {
  default T next() {
    int ord = this.ordinal();
    T[] values = this.valuesInternal();
    int next = (ord + 1) % values.length;
    return values[next];
  }

  T[] valuesInternal();

  int ordinal();
}