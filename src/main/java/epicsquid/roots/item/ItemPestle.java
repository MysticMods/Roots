package epicsquid.roots.item;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.Roots;

public class ItemPestle extends ItemBase {

  public ItemPestle(@Nonnull String name) {
    super(name);
    setMaxStackSize(1);
    setCreativeTab(Roots.tab);
  }

}
