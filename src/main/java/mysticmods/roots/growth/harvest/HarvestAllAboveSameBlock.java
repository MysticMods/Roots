package mysticmods.roots.growth.harvest;

import mysticmods.roots.api.growth.HarvestFunction;
import mysticmods.roots.util.HarvestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record HarvestAllAboveSameBlock() implements HarvestFunction {
  @Override
  public void harvest(Level level, BlockPos pos, BlockState state, LivingEntity entity, @Nullable IntegerProperty ageProperty, int maximumAge, @Nullable Item seedItem) {
    BlockPos abovePos = pos.above();
    BlockState aboveState = level.getBlockState(abovePos);
    if (!aboveState.is(state.getBlock())) {
      return;
    }

    BlockPos newAbovePos = abovePos;
    while (aboveState.is(state.getBlock())) {
      newAbovePos = newAbovePos.above();
      aboveState = level.getBlockState(newAbovePos);
    }

    newAbovePos = newAbovePos.below();
    BlockPos.MutableBlockPos mPos = newAbovePos.mutable();
    for (int y = newAbovePos.getY(); y > pos.getY(); y--) {
      mPos.set(newAbovePos.getX(), y, newAbovePos.getZ());
      HarvestUtil.adjustOrCapture(new HarvestUtil.DropStuff(mPos, level.dimension()));
      level.destroyBlock(mPos, true, entity);
    }
  }
}
