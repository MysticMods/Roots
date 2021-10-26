package mysticmods.roots.blocks.crops;

import net.minecraft.block.BlockState;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Supplier;

public class ElementalCropBlock extends ThreeStageCropBlock {
  public ElementalCropBlock(Properties builder, Supplier<? extends IItemProvider> seedProvider) {
    super(builder, seedProvider);
  }

  // TODO: Check for elemental soil
  @Override
  public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
    if (pRandom.nextInt(9) != 0) {
      super.randomTick(pState, pLevel, pPos, pRandom);
    }
  }

  @Override
  // TODO: Check for elemental soil
  protected int getBonemealAgeIncrease(World pLevel) {
    return super.getBonemealAgeIncrease(pLevel) / 9;
  }
}
