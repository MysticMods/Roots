package epicsquid.roots.handler;

import epicsquid.roots.item.PouchType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IPouchHandler {

  PouchType getPouchType();

  int refill(ItemStack herbStack);

  default boolean isApothecary() {
    return getPouchType() == PouchType.APOTHECARY;
  }

  default boolean isHerb() {
    return getPouchType() == PouchType.HERB;
  }

  default boolean isCreative() {
    return getPouchType() == PouchType.CREATIVE;
  }

  default boolean isFey () {
    return getPouchType() == PouchType.FEY;
  }

  IItemHandlerModifiable getInventory();

  IItemHandlerModifiable getHerbs();

  void markDirty();
}
