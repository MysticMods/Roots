package epicsquid.mysticallib.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Set;

public interface IBlacklistingTool {
  Set<Block> getBlockBlacklist();

  default boolean isBlacklisted (BlockState state) {
    return isBlacklisted(state.getBlock());
  }

  default boolean isBlacklisted (Block block) {
    return getBlockBlacklist().contains(block);
  }
}
