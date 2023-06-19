package mysticmods.roots.block.crop;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import java.util.Random;
import java.util.function.Supplier;

public class ElementalCropBlock extends ThreeStageCropBlock {
  public static final int BASE_TICK = 9;
  public static final int ELEMENTAL_TICK = 3;

  public ElementalCropBlock(Properties builder, Supplier<Supplier<? extends ItemLike>> seedProvider) {
    super(builder, seedProvider);
  }

  @Override
  public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    BlockState stateBelow = pLevel.getBlockState(pPos.below());
    if (pRandom.nextInt(stateBelow.is(RootsAPI.Tags.Blocks.ELEMENTAL_SOIL) ? ELEMENTAL_TICK : BASE_TICK) != 0) {
      super.randomTick(pState, pLevel, pPos, pRandom);
    }
  }
}
