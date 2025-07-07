package mysticmods.roots.growth.harvest;

import mysticmods.roots.api.growth.HarvestFunction;
import mysticmods.roots.mixin.accessor.AccessorMixinGrowingPlantBlock;
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

public record HarvestGrowingPlantBlock() implements HarvestFunction {
  @Override
  public void harvest(Level level, BlockPos pos, BlockState state, LivingEntity entity, @Nullable IntegerProperty ageProperty, int maximumAge, @Nullable Item seedItem) {
    Block block = state.getBlock();
    if (!(block instanceof GrowingPlantBlock growing)) {
      return;
    }

    Block body = ((AccessorMixinGrowingPlantBlock) growing).rootsGetBodyBlock();
    Direction dir = ((AccessorMixinGrowingPlantBlock) growing).rootsGetGrowthDirection();

    Block head = ((AccessorMixinGrowingPlantBlock) growing).rootsGetHeadBlock();
    BlockPos first = pos.relative(dir);
    BlockPos last = pos;

    BlockPos cursor = first;
    while (true) {
      BlockState current = level.getBlockState(cursor);
      if (current.is(body) || current.is(head)) {
        last = cursor;
        cursor = cursor.relative(dir);
      } else {
        break;
      }
    }

    BlockPos.MutableBlockPos mPos = last.mutable();
    while (!mPos.equals(pos)) {
      HarvestUtil.adjustOrCapture(new HarvestUtil.DropStuff(mPos, level.dimension()));
      level.destroyBlock(mPos, true, entity);
      mPos.move(dir.getOpposite());
    }
  }
}
