package epicsquid.roots.recipe;

import com.google.common.collect.Sets;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  public BlockStatePredicate getStartPredicate () {
    return start;
  }

  public WorldBlockStatePredicate getCondition () {
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

  public TransmutationRecipe condition(WorldBlockStatePredicate condition) {
    this.condition = condition;
    return this;
  }

  public boolean isItem() {
    return !stack.isEmpty();
  }

  public boolean isState() {
    return state != null;
  }

  public ItemStack getStack () {
    return stack;
  }

  public Optional<IBlockState> getState () {
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

  public interface MatchingStates {
    default List<IBlockState> matchingStates() {
      return Collections.emptyList();
    }
  }

  @FunctionalInterface
  public interface BlockStatePredicate extends MatchingStates {
    BlockStatePredicate TRUE = (o) -> true;

    boolean test(IBlockState state);
  }

  public static class StatePredicate implements BlockStatePredicate {
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

  public static class PropertyPredicate extends StatePredicate {
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
      Collection<IProperty<?>> incoming = state.getPropertyKeys();
      Collection<IProperty<?>> current = this.state.getPropertyKeys();
      return super.test(state) && props.stream().allMatch(prop -> incoming.contains(prop) && current.contains(prop) && state.getValue(prop).equals(this.state.getValue(prop)));
    }
  }

  public static class BlocksPredicate implements BlockStatePredicate {
    private Set<Block> blocks;
    private List<IBlockState> states;

    public BlocksPredicate(Block... blocks) {
      this.blocks = Sets.newHashSet(blocks);
      this.states = Stream.of(blocks).map(Block::getDefaultState).collect(Collectors.toList());
    }

    @Override
    public boolean test(IBlockState state) {
      return blocks.contains(state.getBlock());
    }

    @Override
    public List<IBlockState> matchingStates() {
      return states;
    }
  }

  public static class WaterPredicate extends BlocksPredicate {
    public WaterPredicate() {
      super(Blocks.WATER, Blocks.FLOWING_WATER);
    }

    @Override
    public List<IBlockState> matchingStates() {
      return Collections.singletonList(ModBlocks.fake_water.getDefaultState());
    }
  }

  public static class LavaPredicate extends BlocksPredicate {
    public LavaPredicate() {
      super(Blocks.LAVA, Blocks.FLOWING_LAVA);
    }

    @Override
    public List<IBlockState> matchingStates() {
      return Collections.singletonList(ModBlocks.fake_lava.getDefaultState());
    }
  }

  public static class LeavesPredicate implements BlockStatePredicate {
    public static List<IBlockState> leaves = null;

    @Override
    public boolean test(IBlockState state) {
      return state.getMaterial() == Material.LEAVES;
    }

    @Override
    public List<IBlockState> matchingStates() {
      if (leaves == null) {
        leaves = new ArrayList<>();
        for (ItemStack stack : OreDictionary.getOres("treeLeaves")) {
          if (stack.getItem() instanceof ItemBlock) {
            leaves.add(((ItemBlock) stack.getItem()).getBlock().getDefaultState());
          }
        }
      }
      return leaves;
    }
  }

  public enum StatePosition {
    ABOVE, BELOW, NULL;
  }

  @FunctionalInterface
  public interface WorldBlockStatePredicate extends MatchingStates {
    WorldBlockStatePredicate TRUE = (a, b, c) -> true;

    default StatePosition getPosition() {
      return StatePosition.NULL;
    }

    boolean test(IBlockState state, World world, BlockPos pos);
  }

  @SuppressWarnings("deprecation")
  public static class BlockStateBelow implements WorldBlockStatePredicate {
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

    @Override
    public StatePosition getPosition() {
      return StatePosition.BELOW;
    }
  }

  public static class BlockStateAbove extends BlockStateBelow {
    public BlockStateAbove(BlockStatePredicate state) {
      super(state);
    }

    @Override
    public boolean test(IBlockState state, World world, BlockPos pos) {
      return this.state.test(world.getBlockState(pos.up()));
    }

    @Override
    public StatePosition getPosition() {
      return StatePosition.ABOVE;
    }
  }
}

