package mysticmods.roots.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class ThreeStageCropBlock extends BeetrootBlock {
  private final Supplier<? extends ItemLike> seedProvider;

  public ThreeStageCropBlock(Supplier<? extends ItemLike> seedProvider, Properties builder) {
    super(builder);
    this.seedProvider = seedProvider;
  }

  @Override
  public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
    if (pLevel.getRawBrightness(pPos, 0) >= 9) {
      int i = this.getAge(pState);
      if (i < this.getMaxAge()) {
        // TODO: This changed from block to BlockState
        float f = getGrowthSpeed(pState, pLevel, pPos);
        if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(pLevel, pPos, pState, pRandom.nextInt((int) (25.0F / f) + 1) == 0)) {
          pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
          net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(pLevel, pPos, pState);
        }
      }
    }
  }

  @Override
  protected int getBonemealAgeIncrease(Level pLevel) {
    return Mth.nextInt(pLevel.random, 2, 5);
  }

  @Override
  protected ItemLike getBaseSeedId() {
    return seedProvider.get();
  }
}
