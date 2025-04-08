package mysticmods.roots.growth.harvestable;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.growth.CanHarvestFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record CanHarvestOnFarmland() implements CanHarvestFunction {
  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    BlockState belowState = level.getBlockState(blockPos.below());
    return belowState.is(RootsTags.Blocks.FARMLANDS);
  }
}
