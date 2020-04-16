package epicsquid.roots.item;

import javax.annotation.Nonnull;

public class ItemApothecaryPouch extends ItemPouch {
  public ItemApothecaryPouch(@Nonnull String name) {
    super(name, PouchType.APOTHECARY);
  }
}
