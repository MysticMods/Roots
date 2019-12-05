package epicsquid.roots.item;

import javax.annotation.Nonnull;

public class ApothecaryPouchItem extends Pouch {
  public ApothecaryPouchItem(@Nonnull String name) {
    super(name);
  }

  @Override
  public boolean isApothecary() {
    return true;
  }
}
