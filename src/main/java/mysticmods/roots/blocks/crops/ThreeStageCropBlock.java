package mysticmods.roots.blocks.crops;

import mysticmods.roots.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Supplier;

public class ThreeStageCropBlock extends CropBlock {
  public static IntegerProperty AGE = BlockStateProperties.AGE_3;
  private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)};

  public ThreeStageCropBlock(Properties builder, Supplier<? extends IItemProvider> seedProvider) {
    super(builder, seedProvider);
  }

  @Override
  public IntegerProperty getAgeProperty() {
    return AGE;
  }

  @Override
  public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
    if (pRandom.nextInt(3) != 0) {
      super.randomTick(pState, pLevel, pPos, pRandom);
    }
  }

  @Override
  protected int getBonemealAgeIncrease(World pLevel) {
    return super.getBonemealAgeIncrease(pLevel) / 3;
  }

  @Override
  public int getMaxAge() {
    return 3;
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }

  @Override
  public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
    return SHAPE_BY_AGE[pState.getValue(this.getAgeProperty())];
  }
}
