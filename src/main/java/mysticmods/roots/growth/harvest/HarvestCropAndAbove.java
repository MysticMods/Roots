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

public record HarvestCropAndAbove () implements HarvestFunction {
  @Override
  public void harvest(Level level, BlockPos pos, BlockState state, LivingEntity entity, @Nullable IntegerProperty ageProperty, int maximumAge, @Nullable Item seedItem) {
    HarvestUtil.adjustOrCapture(new HarvestUtil.DropStuff(pos, level.dimension(), seedItem, seedItem == null ? 0 : 1));
    HarvestUtil.adjustOrCapture(new HarvestUtil.DropStuff(pos.above(), level.dimension()));
    level.destroyBlock(pos.above(), true, entity);
    level.destroyBlock(pos, true, entity);
  }
}
