package mysticmods.roots.util;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.growth.GrowthRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GrowthUtil {
  @Nullable
  public static GrowthRecord getGrowthRecord (BlockState state) {
    GrowthRecord record = state.getBlockHolder().getData(DataMaps.GROWTH_RECORDS);
    if (record == null && state.getBlock() instanceof CropBlock crop) {
      record = GrowthRecord.ofCrop(crop);
      RootsAPI.LOG.error("We're guessing a growth record for crop '{}'. This should be added as a growth record.", crop);
    }

    return record;
  }

  public static int growthTicks(Level level, BlockPos pos, @Nullable BlockState state, @Nullable Player player) {
    if (state == null) {
      state = level.getBlockState(pos);
    }

    if (state.is(RootsTags.Blocks.GROWTH_BLACKLIST)) {
      return -1;
    }

    GrowthRecord record = getGrowthRecord(state);

    if (record == null) {
      return -1;
    }

    if (record.canGrow(level, pos, state)) {
      return record.ticks();
    }

    return -1;
  }
}
