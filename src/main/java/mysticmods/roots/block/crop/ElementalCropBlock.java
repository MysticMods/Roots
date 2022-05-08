package mysticmods.roots.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;
import java.util.function.Supplier;

public class ElementalCropBlock extends ThreeStageCropBlock {
  public ElementalCropBlock(Properties builder, Supplier<Supplier<? extends ItemLike>> seedProvider) {
    super(builder, seedProvider);
  }

  // TODO: Check for elemental soil
  @Override
  public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
    if (pRandom.nextInt(9) != 0) {
      super.randomTick(pState, pLevel, pPos, pRandom);
    }
  }

  @Override
  // TODO: Check for elemental soil
  protected int getBonemealAgeIncrease(Level pLevel) {
    return super.getBonemealAgeIncrease(pLevel) / 9;
  }
}
