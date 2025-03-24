package mysticmods.roots.growth.harvest;

import mysticmods.roots.api.growth.HarvestFunction;
import mysticmods.roots.mixin.AccessorMixinGrowingPlantBlock;
import mysticmods.roots.util.HarvestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record HarvestGrowingPlantBlock () implements HarvestFunction {
  @Override
  public void harvest(Level level, BlockPos pos, BlockState state, LivingEntity entity, @Nullable IntegerProperty ageProperty, int maximumAge, @Nullable Item seedItem) {
    Block block = state.getBlock();
    if (!(block instanceof GrowingPlantBlock growing)) {
      return;
    }

    Direction dir = ((AccessorMixinGrowingPlantBlock) growing).rootsGetGrowthDirection();
    BlockPos abovePos = pos.relative(dir);
    BlockState aboveState = level.getBlockState(abovePos);
    if (!aboveState.is(state.getBlock())) {
      return;
    }

    BlockPos newAbovePos = abovePos;
    while (aboveState.is(state.getBlock())) {
      newAbovePos = newAbovePos.relative(dir);
      aboveState = level.getBlockState(newAbovePos);
    }

    newAbovePos = newAbovePos.relative(dir.getOpposite());
    BlockPos.MutableBlockPos mPos = newAbovePos.mutable();
    if (dir == Direction.UP) {
      for (int y = newAbovePos.getY(); y > pos.getY(); y--) {
        mPos.set(newAbovePos.getX(), y, newAbovePos.getZ());
        HarvestUtil.adjustOrCapture(new HarvestUtil.DropStuff(mPos, level.dimension()));
        level.destroyBlock(mPos, true, entity);
      }
    } else if (dir == Direction.DOWN) {
      for (int y = pos.getY(); y < newAbovePos.getY(); y++) {
        mPos.set(newAbovePos.getX(), y, newAbovePos.getZ());
        HarvestUtil.adjustOrCapture(new HarvestUtil.DropStuff(mPos, level.dimension()));
        level.destroyBlock(mPos, true, entity);
      }
    }
  }
}
