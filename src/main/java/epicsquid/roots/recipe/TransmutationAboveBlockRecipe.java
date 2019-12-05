package epicsquid.roots.recipe;

import epicsquid.roots.util.types.WorldPosStatePredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TransmutationAboveBlockRecipe extends TransmutationRecipe {
  public final Block below;

  public TransmutationAboveBlockRecipe(ResourceLocation name, Block startBlock, ItemStack endState, Block below) {
    super(name, startBlock, endState, null);
    this.below = below;
  }

  public TransmutationAboveBlockRecipe(ResourceLocation name, Block startBlock, BlockState endState, Block below) {
    super(name, startBlock, endState, null);
    this.below = below;
  }

  public TransmutationAboveBlockRecipe(ResourceLocation name, BlockState startState, BlockState endState, Block below) {
    super(name, startState, endState, null);
    this.below = below;
  }

  public TransmutationAboveBlockRecipe(ResourceLocation name, BlockState startState, ItemStack endState, Block below) {
    super(name, startState, endState, null);
    this.below = below;
  }

  public WorldPosStatePredicate getCondition() {
    return (t, u, v) -> t.getBlockState(u.down()).getBlock() == below;
  }
}
