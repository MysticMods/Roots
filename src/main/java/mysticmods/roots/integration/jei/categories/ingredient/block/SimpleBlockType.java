package mysticmods.roots.integration.jei.categories.ingredient.block;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record SimpleBlockType(BlockState state, ItemStack stack) implements IBlockType {
  public SimpleBlockType(Block block) {
    this(block.defaultBlockState(), new ItemStack(block));
  }
}
