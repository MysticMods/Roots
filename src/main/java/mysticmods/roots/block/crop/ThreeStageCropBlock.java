package mysticmods.roots.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.libs.noobutil.block.BaseBlocks;

import java.util.function.Supplier;

public class ThreeStageCropBlock extends BaseBlocks.SeededCropsBlock {
  public static IntegerProperty AGE = BlockStateProperties.AGE_3;
  private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)};

  public ThreeStageCropBlock(Properties builder, Supplier<Supplier<? extends ItemLike>> seedProvider) {
    super(builder, seedProvider);
  }

  @Override
  public IntegerProperty getAgeProperty() {
    return AGE;
  }

  @Override
  public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    if (pRandom.nextInt(3) != 0) {
      super.randomTick(pState, pLevel, pPos, pRandom);
    }
  }

  @Override
  protected int getBonemealAgeIncrease(Level pLevel) {
    return super.getBonemealAgeIncrease(pLevel) / 3;
  }

  @Override
  public int getMaxAge() {
    return 3;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return SHAPE_BY_AGE[pState.getValue(this.getAgeProperty())];
  }
}
