package mysticmods.roots.api.recipe.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.function.Predicate;

public interface IWorldCrafting extends IRootsCraftingBase {
  void setBlockState(BlockState state);

  BlockState getBlockState();

  BlockPos getBlockPos();

  default int getContainerSize() {
    return 0;
  }

  default boolean isEmpty() {
    return false;
  }

  default ItemStack getItem(int pSlot) {
    return ItemStack.EMPTY;
  }


  default ItemStack removeItem(int pSlot, int pAmount) {
    return ItemStack.EMPTY;
  }

  default ItemStack removeItemNoUpdate(int pSlot) {
    return ItemStack.EMPTY;
  }

  default void setItem(int pSlot, ItemStack pStack) {
    return;
  }

  default int getMaxStackSize() {
    return 0;
  }

  default boolean canPlaceItem(int pIndex, ItemStack pStack) {
    return false;
  }

  default int countItem(Item pItem) {
    return 0;
  }

  default boolean hasAnyOf(Set<Item> pSet) {
    return false;
  }

  default boolean hasAnyMatching(Predicate<ItemStack> p_216875_) {
    return false;
  }
}
