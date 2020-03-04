package epicsquid.roots.recipe;

import com.google.common.collect.Sets;
import epicsquid.mysticallib.util.NullableSupplier;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class TransmutationRecipe extends RegistryItem {
  private BlockStatePredicate start;
  private NullableSupplier<ItemStack> stack;
  private NullableSupplier<IBlockState> state;
  private WorldBlockStatePredicate condition;

  public TransmutationRecipe(BlockStatePredicate start, @Nullable NullableSupplier<ItemStack> stack, @Nullable NullableSupplier<IBlockState> state, @Nullable WorldBlockStatePredicate condition) {
    this.start = start;
    if (stack == null) {
      this.stack = NullableSupplier.nullable(ItemStack.class);
    } else {
      this.stack = stack;
    }
    if (state == null) {
      this.state = NullableSupplier.nullable(IBlockState.class);
    } else {
      this.state = state;
    }
    if (this.stack.isNull() && this.state.isNull()) {
      throw new IllegalStateException("Can't have both state and stack output as null");
    }
    if (condition == null) {
      this.condition = WorldBlockStatePredicate.TRUE;
    } else {
      this.condition = condition;
    }
  }

  @Nullable
  public IBlockState getEndState() {
    return state.get();
  }

  @Nullable
  public ItemStack getEndStack() {
    return stack.get();
  }

  public boolean isItem() {
    return !stack.isNull();
  }

  public boolean isState() {
    return !state.isNull();
  }

  public boolean matches(IBlockState state, World world, BlockPos pos) {
    return start.test(state) && condition.test(state, world, pos);
  }

  public boolean matches(World world, BlockPos pos) {
    return matches(world.getBlockState(pos), world, pos);
  }

  public interface MatchingStates {
    default List<IBlockState> matchingStates() {
      return Collections.emptyList();
    }
  }

  @FunctionalInterface
  private interface BlockStatePredicate extends MatchingStates {
    BlockStatePredicate TRUE = (o) -> true;

    boolean test(IBlockState state);
  }

  public class StatePredicate implements BlockStatePredicate {
    protected IBlockState state;

    public StatePredicate(IBlockState state) {
      this.state = state;
    }

    @Override
    public List<IBlockState> matchingStates() {
      return Collections.singletonList(state);
    }

    @Override
    public boolean test(IBlockState state) {
      return state.getBlock() == this.state.getBlock();
    }
  }

  public class PropertyPredicate extends StatePredicate {
    protected List<IProperty<?>> props;

    public PropertyPredicate(IBlockState state, IProperty<?> prop) {
      super(state);
      this.props = Collections.singletonList(prop);
    }

    public PropertyPredicate(IBlockState state, List<IProperty<?>> props) {
      super(state);
      this.props = props;
    }

    @Override
    public boolean test(IBlockState state) {
      return super.test(state) && props.stream().allMatch(prop -> state.getValue(prop) == this.state.getValue(prop));
    }
  }

  public class BlocksPredicate implements BlockStatePredicate {
    private Set<Block> blocks;

    public BlocksPredicate(Block... blocks) {
      this.blocks = Sets.newHashSet(blocks);
    }

    @Override
    public boolean test(IBlockState state) {
      return blocks.contains(state.getBlock());
    }
  }

  @FunctionalInterface
  public interface WorldBlockStatePredicate extends MatchingStates {
    WorldBlockStatePredicate TRUE = (a, b, c) -> true;

    boolean test(IBlockState state, World world, BlockPos pos);
  }

  @SuppressWarnings("deprecation")
  public class BlockStateBelow implements WorldBlockStatePredicate {
    protected BlockStatePredicate state;

    public BlockStateBelow(BlockStatePredicate state) {
      this.state = state;
    }

    @Override
    public boolean test(IBlockState state, World world, BlockPos pos) {
      return this.state.test(world.getBlockState(pos.down()));
    }

    @Override
    public List<IBlockState> matchingStates() {
      return this.state.matchingStates();
    }
  }

  public class BlockStateAbove extends BlockStateBelow {
    public BlockStateAbove(BlockStatePredicate state) {
      super(state);
    }

    @Override
    public boolean test(IBlockState state, World world, BlockPos pos) {
      return this.state.test(world.getBlockState(pos.up()));
    }
  }
}

