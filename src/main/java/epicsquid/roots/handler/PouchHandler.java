package epicsquid.roots.handler;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.item.PouchType;
import epicsquid.roots.world.data.PouchHandlerData;
import epicsquid.roots.world.data.PouchHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PouchHandler implements IPouchHandler {

  private IItemHandlerModifiable inventorySlots;
  private IItemHandlerModifiable herbSlots;
  private ItemStack pouch;
  private Runnable markDirty;
  private Function<ItemStack, Integer> filler;

  public PouchHandler(ItemStack pouch) {
    this.pouch = pouch;

    CompoundNBT tag = ItemUtil.getOrCreateTag(pouch);
    PouchHandlerData data;
    PouchType type = ItemPouch.getPouchType(pouch);
    if (tag.hasUniqueId("bag_id")) {
      data = PouchHandlerRegistry.getData(tag.getUniqueId("bag_id"), type);
    } else {
      data = PouchHandlerRegistry.getNewData(type);
      tag.setUniqueId("bag_id", data.getUuid());
    }
    this.inventorySlots = data.getInventoryHandler();
    this.herbSlots = data.getHerbHandler();
    markDirty = data::markDirty;
    filler = data::refill;

    if (tag.hasKey("handler", Constants.NBT.TAG_COMPOUND)) {
      OldPouchHandler oldPouch = OldPouchHandler.getHandler(pouch);
      List<ItemStack> oldInventory = new ArrayList<>();
      List<ItemStack> oldHerbs = new ArrayList<>();
      OldPouchHandler.PouchItemHandler current = oldPouch.getInventory();
      for (int i = 0; i < current.getSlots(); i++) {
        ItemStack stack = current.getStackInSlot(i);
        if (!stack.isEmpty()) {
          oldInventory.add(stack);
        }
      }
      current = oldPouch.getHerbs();
      for (int i = 0; i < current.getSlots(); i++) {
        ItemStack stack = current.getStackInSlot(i);
        if (!stack.isEmpty()) {
          oldHerbs.add(stack);
        }
      }
      for (ItemStack stack : oldInventory) {
        ItemStack result = ItemHandlerHelper.insertItemStacked(inventorySlots, stack, false);
        if (!result.isEmpty()) {
          Roots.logger.error("Unable to fully merge itemstack " + stack.toString() + " into new component/apothecary inventory.");
        }
      }
      for (ItemStack stack : oldHerbs) {
        ItemStack result = ItemHandlerHelper.insertItemStacked(herbSlots, stack, false);
        if (!result.isEmpty()) {
          Roots.logger.error("Unable to fully merge itemstack " + stack.toString() + " into new component/apothecary herbs.");
        }
      }
      tag.removeTag("handler");
      markDirty();
    }
  }

  @Override
  public int refill(ItemStack herbStack) {
    return this.filler.apply(herbStack);
  }

  @Override
  public PouchType getPouchType() {
    return ItemPouch.getPouchType(pouch);
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
