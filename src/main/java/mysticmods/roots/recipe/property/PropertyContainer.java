package mysticmods.roots.recipe.property;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PropertyContainer implements Container {
  @Override
  public int getContainerSize() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public ItemStack getItem(int pIndex) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeItem(int pIndex, int pCount) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeItemNoUpdate(int pIndex) {
    return ItemStack.EMPTY;
  }

  @Override
  public void setItem(int pIndex, ItemStack pStack) {
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

  }
}
