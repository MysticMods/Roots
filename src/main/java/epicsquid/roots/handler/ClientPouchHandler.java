package epicsquid.roots.handler;

import epicsquid.roots.item.ItemPouch;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class ClientPouchHandler implements IPouchHandler {
  private ItemStackHandler inventory = new ItemStackHandler(IPouchHandler.APOTHECARY_POUCH_INVENTORY_SLOTS);
  private ItemStackHandler herbs = new ItemStackHandler(IPouchHandler.APOTHECARY_POUCH_HERB_SLOTS);

  private final ItemStack pouch;

  public ClientPouchHandler(ItemStack stack) {
    this.pouch = stack;
  }

  @Override
  public int refill(ItemStack herbStack) {
    return 0;
  }

  @Override
  public boolean isApothecary() {
    return ((ItemPouch) pouch.getItem()).isApothecary();
  }

  @Override
  public IItemHandlerModifiable getInventory() {
    return inventory;
  }

  @Override
  public IItemHandlerModifiable getHerbs() {
    return herbs;
  }

  @Override
  public void markDirty() {
  }
}
