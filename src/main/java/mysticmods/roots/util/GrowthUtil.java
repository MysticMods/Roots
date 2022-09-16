package mysticmods.roots.util;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GrowthUtil {
  public static boolean canGrow(Level level, BlockPos pos, @Nullable BlockState state, @Nullable Player player) {
    if (state == null) {
      state = level.getBlockState(pos);
    }

    if (state.is(RootsAPI.Tags.Blocks.GROWTH_BLACKLIST)) {
      return false;
    }

    if (state.is(RootsAPI.Tags.Blocks.GROWTH_FORCE)) {
      return true;
    }

    Block block = state.getBlock();
    if (block instanceof CropBlock crop) {
      return !crop.isMaxAge(state);
    }

    // TODO: This should probably be checked
    return block instanceof BonemealableBlock;
  }
}
