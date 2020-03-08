package epicsquid.roots.recipe.transmutation;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlocksPredicate implements BlockStatePredicate {
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
