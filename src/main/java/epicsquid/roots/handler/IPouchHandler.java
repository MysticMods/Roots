package epicsquid.roots.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IPouchHandler {
  int COMPONENT_POUCH_HERB_SLOTS = 6;
  int COMPONENT_POUCH_INVENTORY_SLOTS = 12;
  int APOTHECARY_POUCH_HERB_SLOTS = 9;
  int APOTHECARY_POUCH_INVENTORY_SLOTS = 18;

  int refill(ItemStack herbStack);

  boolean isApothecary();

  IItemHandlerModifiable getInventory();

  IItemHandlerModifiable getHerbs();

  void markDirty();
}
