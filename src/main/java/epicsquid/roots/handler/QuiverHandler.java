package epicsquid.roots.handler;

import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;
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

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
      super.setStackInSlot(slot, stack);

      QuiverHandler.this.saveToStack();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
      ItemStack result = super.insertItem(slot, stack, simulate);

      if (!simulate) {
        QuiverHandler.this.saveToStack();
      }

      return result;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      ItemStack result = super.extractItem(slot, amount, simulate);

      if (!simulate) {
        QuiverHandler.this.saveToStack();
      }

      return result;
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

  public boolean canConsume(ItemStack stack) {
    return ItemHandlerHelper.insertItemStacked(handler, stack, true).isEmpty();
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
