package epicsquid.roots.recipe;

import epicsquid.roots.util.StateUtil;
import epicsquid.roots.util.types.RegistryItem;
import epicsquid.roots.util.types.WorldPosStatePredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TransmutationRecipe extends RegistryItem {
  private BlockState startState;
  private Block startBlock;
  private BlockState endState;
  private ItemStack endStack;
  private WorldPosStatePredicate condition = (a, b, c) -> true;

  public TransmutationRecipe(ResourceLocation name, Block startBlock, ItemStack endState, WorldPosStatePredicate condition) {
    this.setRegistryName(name);
    this.startBlock = startBlock;
    this.endStack = endState;
    this.endState = null;
    if (condition != null)
      this.condition = condition;
  }

  public TransmutationRecipe(ResourceLocation name, Block startBlock, BlockState endState, WorldPosStatePredicate condition) {
    this.setRegistryName(name);
    this.startBlock = startBlock;
    this.endState = endState;
    if (condition != null) {
      this.condition = condition;
    }
  }

  public TransmutationRecipe(ResourceLocation name, BlockState startState, BlockState endState, WorldPosStatePredicate condition) {
    this.setRegistryName(name);
    this.startBlock = null;
    this.startState = startState;
    this.endState = endState;
    if (condition != null) {
      this.condition = condition;
    }
  }

  public TransmutationRecipe(ResourceLocation name, BlockState startState, ItemStack endState, WorldPosStatePredicate condition) {
    this.setRegistryName(name);
    this.startBlock = null;
    this.startState = startState;
    this.endStack = endState;
    this.endState = null;
    if (condition != null) {
      this.condition = condition;
    }
  }

  public boolean itemOutput() {
    return this.endStack != null;
  }

  public Block getStartBlock() {
    return startBlock;
  }

  public BlockState getStartState() {
    if (startState == null && startBlock != null) return startBlock.getDefaultState();
    return startState;
  }

  public ItemStack getEndStack() {
    return endStack;
  }

  public BlockState getEndState() {
    return endState;
  }

  public WorldPosStatePredicate getCondition() {
    return condition;
  }

  public boolean matches(Block block) {
    return matches(block.getDefaultState());
  }

  public boolean matches(BlockState start) {
    if (this.startState != null) {
      return StateUtil.compareStates(this.startState, start);
    } else {
      return this.startBlock == start.getBlock();
    }
  }

  public boolean matches(World world, BlockPos pos, BlockState state) {
    return (this.startBlock != null && this.startBlock == state.getBlock() || this.startState != null && StateUtil.compareStates(this.startState, state)) && (this.getCondition() == null || this.getCondition().test(world, pos, state));
  }

  public boolean matches(World world, BlockPos pos) {
    return matches(world, pos, world.getBlockState(pos));
  }

  public String getKey() {
    return "roots.ritual.transmutation." + getRegistryName().getPath();
  }
}
