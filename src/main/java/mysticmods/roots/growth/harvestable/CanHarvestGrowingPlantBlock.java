package mysticmods.roots.growth.harvestable;

import mysticmods.roots.api.growth.CanHarvestFunction;
import mysticmods.roots.mixin.accessor.AccessorMixinGrowingPlantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
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

    Block head = ((AccessorMixinGrowingPlantBlock) growing).rootsGetHeadBlock();
    Block body = ((AccessorMixinGrowingPlantBlock) growing).rootsGetBodyBlock();
    Direction dir = ((AccessorMixinGrowingPlantBlock) growing).rootsGetGrowthDirection();

    BlockPos behind = blockPos.relative(dir.getOpposite());
    BlockPos ahead = blockPos.relative(dir);

    BlockState behindState = level.getBlockState(behind);
    BlockState aheadState = level.getBlockState(ahead);

    boolean isBehindPlant = behindState.is(head) || behindState.is(body);
    boolean isAheadPlant = aheadState.is(head) || aheadState.is(body);

    return (!isBehindPlant && isAheadPlant) || (isBehindPlant && !isAheadPlant);
  }
}
