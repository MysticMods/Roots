package mysticmods.roots.block.crop;

import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Supplier;

public class ElementalCropBlock extends ThreeStageCropBlock {
  public static final int BASE_TICK = 9;
  public static final int ELEMENTAL_TICK = 3;

  public ElementalCropBlock(Supplier<? extends ItemLike> seedProvider, Properties builder) {
    super(seedProvider, builder);
  }

  @Override
  public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    // TODO: Properly implement
    BlockState stateBelow = pLevel.getBlockState(pPos.below());
    if (pRandom.nextInt(stateBelow.is(RootsTags.Blocks.ELEMENTAL_SOIL) ? ELEMENTAL_TICK : BASE_TICK) != 0) {
      if (!pLevel.isAreaLoaded(pPos, 1))
        return; // Forge: prevent loading unloaded chunks when checking neighbor's light
      if (pLevel.getRawBrightness(pPos, 0) >= 9) {
        int i = this.getAge(pState);
        if (i < this.getMaxAge()) {
          // TODO: This changed from block to blockstate
          float f = getGrowthSpeed(pState, pLevel, pPos);
          if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(pLevel, pPos, pState, pRandom.nextInt((int) (25.0F / f) + 1) == 0)) {
            BlockState pNewState = this.getStateForAge(i + 1);
            if (pState.hasProperty(BlockStateProperties.WATERLOGGED)) {
              pNewState = pNewState.setValue(BlockStateProperties.WATERLOGGED, pState.getValue(BlockStateProperties.WATERLOGGED));
            }
            pLevel.setBlock(pPos, pNewState, 2);
            net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(pLevel, pPos, pState);
          }
        }
      }
    }
  }

  @Override
  public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
    int i = this.getAge(pState) + this.getBonemealAgeIncrease(pLevel);
    int j = this.getMaxAge();
    if (i > j) {
      i = j;
    }

    BlockState newState = this.getStateForAge(i);
    if (pState.hasProperty(BlockStateProperties.WATERLOGGED)) {
      newState = newState.setValue(BlockStateProperties.WATERLOGGED, pState.getValue(BlockStateProperties.WATERLOGGED));
    }
    pLevel.setBlock(pPos, newState, 2);
  }
}
