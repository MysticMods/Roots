package mysticmods.roots.api.recipe.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public interface IWorldCrafting extends IRootsCrafting<ItemStackHandler> {


  BlockState getBlockState();

  BlockPos getBlockPos();

  @Override
  default boolean isEmpty() {
    return false;
  }

  @Override
  default int size() {
    return 0;
  }

  @Override
  default ItemStack getItem(int pSlot) {
    return ItemStack.EMPTY;
  }

  @Override
  default ItemStackHandler getHandler() {
    return NULL_HANDLER;
  }
}
