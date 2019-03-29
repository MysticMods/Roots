package epicsquid.roots.recipe;

import epicsquid.roots.util.types.RegistryItem;
import epicsquid.roots.util.types.WorldPosStatePredicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TransmutationRecipe extends RegistryItem {
  private Block startState;
  private IBlockState endState;
  private WorldPosStatePredicate condition = (a, b, c) -> true;

  public TransmutationRecipe(ResourceLocation name,  Block startState, Block endState) {
    this(name, startState, endState.getDefaultState(), null);
  }

  public TransmutationRecipe(ResourceLocation name, Block startState, Block endState, WorldPosStatePredicate condition) {
    this(name, startState, endState.getDefaultState(), condition);
  }

  public TransmutationRecipe(ResourceLocation name, Block startState, IBlockState endState) {
    this(name, startState, endState, null);
  }

  public TransmutationRecipe(ResourceLocation name, Block startState, IBlockState endState, WorldPosStatePredicate condition) {
    this.setRegistryName(name);
    this.startState = startState;
    this.endState = endState;
    if (condition != null) {
      this.condition = condition;
    }
  }

  public Block getStartState() {
    return startState;
  }

  public IBlockState getEndState() {
    return endState;
  }

  public WorldPosStatePredicate getCondition() {
    return condition;
  }

  public boolean matches (World world, BlockPos pos, IBlockState state) {
    return state.getBlock() == this.startState && (this.condition == null || this.condition.test(world, pos, state));
  }

  public boolean matches (World world, BlockPos pos) {
    return matches(world, pos, world.getBlockState(pos));
  }
}
