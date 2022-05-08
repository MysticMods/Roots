package mysticmods.roots.api.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// TODO: Convert to IRootsCrafting
// TODO: Move to NoobUtil
public class PlayerOffhandInventoryHandler implements Container {
  private final Player player;

  public PlayerOffhandInventoryHandler(Player player) {
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
      this.player.setItemInHand(InteractionHand.OFF_HAND, item);
      return result;
    } else {
      this.player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
      return item.copy();
    }
  }

  @Override
  public ItemStack removeItemNoUpdate(int pIndex) {
    return removeItem(0, this.player.getOffhandItem().getCount());
  }

  @Override
  public void setItem(int pIndex, ItemStack pStack) {
    this.player.setItemInHand(InteractionHand.OFF_HAND, pStack);
  }

  @Override
  public void setChanged() {

  }

  @Override
  public boolean stillValid(Player pPlayer) {
    return true;
  }

  @Override
  public void clearContent() {
    this.player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
  }
}
