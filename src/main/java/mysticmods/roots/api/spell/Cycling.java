package mysticmods.roots.api.spell;

import mysticmods.roots.api.registry.IStyled;

public interface Cycling<T extends Cycling<T>> extends IStyled {
  default T next() {
    int ord = this.ordinal();
    T[] values = this.values();
    int next = (ord + 1) % values.length;
    return values[next];
  }

  T[] values();

  int ordinal();
}