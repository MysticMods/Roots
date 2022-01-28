package mysticmods.roots.api.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

// TODO: Move to NoobUtil
public class PlayerOffhandInventoryHandler implements IInventory {
  private final PlayerEntity player;

  public PlayerOffhandInventoryHandler(PlayerEntity player) {
    this.player = player;
  }

  @Override
  public int getContainerSize() {
    return 1;
  }

  @Override
  public boolean isEmpty() {
    return this.player.getOffhandItem().isEmpty();
  }

  @Override
  public ItemStack getItem(int pIndex) {
    return this.player.getOffhandItem();
  }

  @Override
  public ItemStack removeItem(int pIndex, int pCount) {
    ItemStack item = this.player.getOffhandItem();
    if (item.isEmpty()) {
      return item;
    }
    if (pCount < item.getCount()) {
      ItemStack result = item.split(pCount);
      this.player.setItemInHand(Hand.OFF_HAND, item);
      return result;
    } else {
      this.player.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
      return item.copy();
    }
  }

  @Override
  public ItemStack removeItemNoUpdate(int pIndex) {
    return removeItem(0, this.player.getOffhandItem().getCount());
  }

  @Override
  public void setItem(int pIndex, ItemStack pStack) {
    this.player.setItemInHand(Hand.OFF_HAND, pStack);
  }

  @Override
  public void setChanged() {

  }

  @Override
  public boolean stillValid(PlayerEntity pPlayer) {
    return true;
  }

  @Override
  public void clearContent() {
    this.player.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
  }
}
