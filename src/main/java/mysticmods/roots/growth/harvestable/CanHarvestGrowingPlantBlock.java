package mysticmods.roots.growth.harvestable;

import mysticmods.roots.api.growth.CanHarvestFunction;
import mysticmods.roots.mixin.accessor.AccessorMixinGrowingPlantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record CanHarvestGrowingPlantBlock() implements CanHarvestFunction {
  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    Block block = blockState.getBlock();
    if (!(block instanceof GrowingPlantBlock growing)) {
      return false;
    }
    Direction dir = ((AccessorMixinGrowingPlantBlock) growing).rootsGetGrowthDirection();

    BlockPos relative = blockPos.relative(dir.getOpposite());
    BlockState relativeState = level.getBlockState(relative);
    if (!relativeState.is(growing)) {
      return true;
    }

    return false;
  }
}
