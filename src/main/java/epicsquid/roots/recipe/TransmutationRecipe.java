package epicsquid.roots.recipe;

import epicsquid.roots.recipe.transmutation.BlockStatePredicate;
import epicsquid.roots.recipe.transmutation.StatePredicate;
import epicsquid.roots.recipe.transmutation.WorldBlockStatePredicate;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class TransmutationRecipe extends RegistryItem {
  private BlockStatePredicate start;
  private ItemStack stack = ItemStack.EMPTY;
  private IBlockState state = null;
  private WorldBlockStatePredicate condition = WorldBlockStatePredicate.TRUE;

  public TransmutationRecipe(IBlockState start) {
    this.start = new StatePredicate(start);
  }

  public TransmutationRecipe(BlockStatePredicate start) {
    this.start = start;
  }

  public BlockStatePredicate getStartPredicate() {
    return start;
  }

  public WorldBlockStatePredicate getCondition() {
    return condition;
  }

  public TransmutationRecipe item(ItemStack stack) {
    if (this.state != null) {
      throw new IllegalStateException("Can't have both a state and an itemstack result");
    }
    this.stack = stack;
    return this;
  }

  public TransmutationRecipe state(IBlockState state) {
    if (!this.stack.isEmpty()) {
      throw new IllegalStateException("Can't have both a state and an itemstack result");
    }
    this.state = state;
    return this;
  }

  public TransmutationRecipe condition(@Nullable WorldBlockStatePredicate condition) {
    if (condition == null) {
      this.condition = WorldBlockStatePredicate.TRUE;
    } else {
      this.condition = condition;
    }
    return this;
  }

  public ItemStack getStack() {
    return stack;
  }

  public Optional<IBlockState> getState() {
    if (state == null) {
      return Optional.empty();
    }

    return Optional.of(state);
  }

  public boolean matches(IBlockState state, World world, BlockPos pos) {
    return start.test(state) && (condition != null && condition.test(state, world, pos));
  }

  public boolean matches(World world, BlockPos pos) {
    return matches(world.getBlockState(pos), world, pos);
  }
}

