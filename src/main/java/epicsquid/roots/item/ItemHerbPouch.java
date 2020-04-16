package epicsquid.roots.item;

import javax.annotation.Nonnull;

public class ItemHerbPouch extends ItemPouch {
  public ItemHerbPouch(@Nonnull String name) {
    super(name, PouchType.HERB);
  }
}
