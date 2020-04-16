package epicsquid.roots.handler;

import epicsquid.roots.item.PouchType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

public interface IPouchHandler {

  PouchType getPouchType ();
  void setPouchType ();

  int refill(ItemStack herbStack);

  default boolean isApothecary() {
    return getPouchType() == PouchType.APOTHECARY;
  }

  default boolean isHerb() {
    return getPouchType() == PouchType.HERB;
  }

  default boolean isCreative () {
    return getPouchType() == PouchType.CREATIVE;
  }

  IItemHandlerModifiable getInventory();

  IItemHandlerModifiable getHerbs();

  void markDirty();
}
