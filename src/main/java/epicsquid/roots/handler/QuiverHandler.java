package epicsquid.roots.handler;

import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class QuiverHandler implements INBTSerializable<NBTTagCompound> {
  private ItemStack quiver;
  private ItemStackHandler handler = new ItemStackHandler(6) {
    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      return stack.getItem() instanceof ItemArrow;
    }

    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);

      QuiverHandler.this.saveToStack();
    }
  };

  public QuiverHandler(ItemStack quiver) {
    this.quiver = quiver;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    return handler.serializeNBT();
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    handler.deserializeNBT(nbt);
  }

  public ItemStackHandler getInventory() {
    return handler;
  }

  public static QuiverHandler getHandler(ItemStack stack) {
    QuiverHandler handler = new QuiverHandler(stack);
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quiver")) {
        handler.deserializeNBT(stack.getTagCompound().getCompoundTag("quiver"));
      }
    }

    return handler;
  }

  public boolean canConsume() {
    for (int i = 0; i < 6; i++) {
      ItemStack stack = handler.getStackInSlot(i);
      if (stack.isEmpty()) return true;
      if (stack.getItem() == Items.ARROW && stack.getCount() < stack.getMaxStackSize()) return true;
    }

    return false;
  }

  public boolean consume () {
    int arrowSlot = -1;
    int emptySlot = -1;
    for (int i = 0; i < 6; i++) {
      ItemStack stack = handler.getStackInSlot(i);
      if (stack.getItem() == Items.ARROW) {
        arrowSlot = i;
        break;
      } else if (stack.isEmpty() && emptySlot == -1) {
        emptySlot = i;
      }
    }

    if (arrowSlot == -1 && emptySlot == -1) {
      return false;
    }

    ItemStack arrow = new ItemStack(Items.ARROW);
    if (arrowSlot == -1) {
      if (handler.insertItem(emptySlot, arrow, false).isEmpty()) {
        return true;
      }
    } else {
      if (handler.insertItem(arrowSlot, arrow, false).isEmpty()) {
        return true;
      }
    }
    return false;
  }

  public void saveToStack() {
    NBTTagCompound tag = quiver.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      quiver.setTagCompound(tag);
    }

    tag.setTag("quiver", serializeNBT());
  }
}
