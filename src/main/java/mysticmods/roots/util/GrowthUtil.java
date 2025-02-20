package mysticmods.roots.util;

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
  public static int growthTicks(Level level, BlockPos pos, @Nullable BlockState state, @Nullable Player player) {
    if (state == null) {
      state = level.getBlockState(pos);
    }

    if (state.is(RootsTags.Blocks.GROWTH_BLACKLIST)) {
      return 0;
    }

    GrowthRecord record = state.getBlockHolder().getData(DataMaps.GROWTH_RECORDS);
    if (record == null && state.getBlock() instanceof CropBlock crop) {
      record = GrowthRecord.ofCrop(crop, null);
    } else {
      return 0;
    }

    if (record.canGrowFunction().test(level, pos, state, record.ageProperty().orElse(null), record.maximumAge())) {
      return record.ticks();
    }

    return 0;
  }
}
