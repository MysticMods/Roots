package epicsquid.roots.handler;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.world.data.PouchHandlerData;
import epicsquid.roots.world.data.PouchHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.function.Function;

public class PouchHandler implements IPouchHandler {

  private IItemHandlerModifiable inventorySlots;
  private IItemHandlerModifiable herbSlots;
  private ItemStack pouch;
  private Runnable markDirty;
  private Function<ItemStack, Integer> filler;

  public PouchHandler(ItemStack pouch) {
    this.pouch = pouch;

    NBTTagCompound tag = ItemUtil.getOrCreateTag(pouch);
    PouchHandlerData data;
    if (tag.hasUniqueId("bag_id")) {
      data = PouchHandlerRegistry.getData(tag.getUniqueId("bag_id"));
    } else {
      data = PouchHandlerRegistry.getNewData();
      tag.setUniqueId("bag_id", data.getUuid());
    }
    this.inventorySlots = data.getInventoryHandler();
    this.herbSlots = data.getHerbHandler();
    markDirty = data::markDirty;
    filler = data::refill;
  }

  @Override
  public int refill(ItemStack herbStack) {
    return this.filler.apply(herbStack);
  }

  @Override
  public boolean isApothecary() {
    return ((ItemPouch) pouch.getItem()).isApothecary();
  }

  @Override
  public IItemHandlerModifiable getInventory() {
    return inventorySlots;
  }

  @Override
  public IItemHandlerModifiable getHerbs() {
    return herbSlots;
  }

  @Override
  public void markDirty() {
    this.markDirty.run();
  }

  @Nullable
  public static PouchHandler getHandler(ItemStack stack) {
    if (!(stack.getItem() instanceof ItemPouch)) {
      return null;
    }
    return new PouchHandler(stack);
  }
}
