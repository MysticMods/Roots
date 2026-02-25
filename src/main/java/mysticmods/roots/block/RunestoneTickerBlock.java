package mysticmods.roots.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RunestoneTickerBlock extends Block {
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public RunestoneTickerBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED);
  }

  @Override
  protected boolean isSignalSource(BlockState state) {
    return true;
  }

  @Override
  protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
    if (state.getValue(POWERED)) {
      return 15;
    }

    return 0;
  }

  @Override
  protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    super.tick(state, level, pos, random);
    if (state.getValue(POWERED)) {
      level.setBlock(pos, state.setValue(POWERED, false), 3);
    } else {
      level.setBlock(pos, state.setValue(POWERED, true), 3);
    }
  }

  @Override
  protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    level.scheduleTick(pos, this, 1);
  }
}
